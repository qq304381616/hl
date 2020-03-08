package com.hl.dotime.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.hl.dotime.IconAdapter
import com.hl.dotime.R
import com.hl.dotime.adapter.TaskGroupAdapter
import com.hl.dotime.base.BaseActivity
import com.hl.dotime.db.entity.Task
import com.hl.dotime.db.entity.TaskGroup
import com.hl.dotime.db.service.TaskGroupService
import com.hl.dotime.db.service.TaskService
import com.hl.dotime.utils.ConstantIcon
import com.hl.dotime.utils.UUIDUtils
import com.hl.dotime.utils.Utils

class ModifyTaskActivity : BaseActivity() {

    private lateinit var et_name: EditText
    private var button: Button? = null
    private lateinit var iv_group: ImageView
    private lateinit var iv_choose_color: ImageView
    private lateinit var iv_choose_icon: ImageView
    private var spinner: Spinner? = null
    private var group: TaskGroup? = null
    private var task: Task? = null
    private lateinit var taskGroupService: TaskGroupService
    private lateinit var adapter: TaskGroupAdapter
    private var defaultColor = ConstantIcon.DEFAULT_ICON_COLOR
    private var defaultIconName = ConstantIcon.DEFAULT_ICON_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_task)

        initToolbar()

        et_name = findViewById(R.id.et_name)
        spinner = findViewById(R.id.spinner)
        button = findViewById(R.id.button)
        iv_group = findViewById(R.id.iv_group)
        iv_choose_color = findViewById(R.id.iv_choose_color)
        iv_choose_icon = findViewById(R.id.iv_choose_icon)

        // 选择图标颜色
        findViewById<View>(R.id.ll_choose_color).setOnClickListener {
            showColorPickerDialog()
        }
        // 选择图标图案
        findViewById<View>(R.id.ll_choose_icon).setOnClickListener {
            showIconDialog()
        }
        val taskService = TaskService(this)
        taskGroupService = TaskGroupService(this)
        adapter = TaskGroupAdapter(this)

        spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                group = adapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                group = null
            }
        }

        // 加载分组
        adapter.data = taskGroupService.queryAll()
        spinner!!.adapter = adapter

        task = intent.getParcelableExtra("task")
        if (task != null) {
            // 修改分类 初始化原数据
            et_name.setText(task?.name)
            et_name.setSelection(et_name.text.length)
            for (i in adapter.data!!.indices) {
                if (task?.groupId.equals(adapter.data!!.get(i).id)) {
                    spinner!!.setSelection(i)
                }
            }
            defaultIconName = task?.iconName!!
            defaultColor = task?.iconColor!!
        }

        button!!.setOnClickListener { _ ->
            val name = et_name.text.toString().trim()
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "请填写名称", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (name.length > 10) {
                Toast.makeText(this, "名称不能超过10个字符", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (group == null) {
                Toast.makeText(this, "请选择或创建分组", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (task != null) {
                // 修改
                task!!.name = name
                task!!.groupId = group?.id
                task!!.groupName = group?.name
                task!!.iconName = defaultIconName
                task!!.iconColor = defaultColor
                taskService.update(task!!)
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show()
            } else {
                // 新增
                task = Task()
                task!!.id = UUIDUtils.uuid
                task!!.name = name
                task!!.groupId = group?.id
                task!!.isDel = 0
                task!!.groupName = group?.name
                task!!.iconName = defaultIconName
                task!!.iconColor = defaultColor
                taskService.insert(task!!)
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent()
            intent.putExtra("task", task!!)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        // 管理分组
        iv_group.setOnClickListener {
            startActivity(Intent(this, ModifyGroupActivity::class.java))
        }

        updateIconAndColor()
    }

    override fun onResume() {
        super.onResume()
        adapter.data = taskGroupService.queryAll()
        spinner!!.adapter = adapter
    }

    private fun showColorPickerDialog() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("选择颜色")
                .initialColor(Color.parseColor(defaultColor))
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorChangedListener {}
                .setOnColorSelectedListener {}
                .setPositiveButton("ok") { d, lastSelectedColor, allColors ->
                    if (allColors != null) {
                        var sb: StringBuilder? = null
                        for (color in allColors) {
                            if (color == null)
                                continue
                            if (sb == null)
                                sb = StringBuilder()
                            sb.append("#" + Integer.toHexString(color).toUpperCase())
                        }

                        if (sb != null) {
                            defaultColor = sb.toString()
                            updateIconAndColor()
                        }
                    }
                }
                .setNegativeButton("cancel", null)
                .showColorEdit(true)
                .setColorEditTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright))
                .build()
                .show()
    }

    private fun showIconDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("选择图标")

        val recycler = RecyclerView(this)
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.layoutManager = GridLayoutManager(this, 5)

        val iconAdapter = IconAdapter(this)
        iconAdapter.mData = ConstantIcon.list
        recycler.adapter = iconAdapter

        builder.setView(recycler)
        builder.setNegativeButton("取消") { _, _ ->
            updateIconAndColor()
        }
        builder.setOnDismissListener {
            updateIconAndColor()
        }
        val dialog = builder.show()

        val params = dialog.window.attributes
        params.width = this.windowManager.defaultDisplay.width
        params.height = this.windowManager.defaultDisplay.height / 3 * 2
        dialog.window.attributes = params

        iconAdapter.setOnItemClick(object : IconAdapter.OnItemClick {
            override fun onItemClick(position: Int) {
                defaultIconName = iconAdapter.mData!!.get(position)
                updateIconAndColor()
                dialog.dismiss()
            }
        })
    }

    private fun updateIconAndColor() {
        iv_choose_color.setBackgroundColor(Color.parseColor(defaultColor))
        Utils.setSVGColor(this@ModifyTaskActivity, iv_choose_icon, ConstantIcon.map[defaultIconName]!!, defaultColor)
    }
}