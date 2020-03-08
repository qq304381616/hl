package com.hl.dotime.ui

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import com.hl.dotime.GroupAdapter
import com.hl.dotime.R
import com.hl.dotime.base.BaseActivity
import com.hl.dotime.db.entity.TaskGroup
import com.hl.dotime.db.service.TaskGroupService
import com.hl.dotime.utils.UUIDUtils

/**
 * 分组管理
 */
class ModifyGroupActivity : BaseActivity() {

    private lateinit var taskGroupService: TaskGroupService
    private lateinit var groupAdapter: GroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_group)

        initToolbar()

        taskGroupService = TaskGroupService(this)
        groupAdapter = GroupAdapter(this)

        val rv_group_list = findViewById<RecyclerView>(R.id.rv_group_list)
        val fab_add = findViewById<FloatingActionButton>(R.id.fab_add)

        groupAdapter.setOnItemClick(object : GroupAdapter.OnItemClick {
            override fun onItemClick(position: Int) {
                // 修改
                showModifyDialog(groupAdapter.mData!!.get(position))
            }
        })

        groupAdapter.setOnItemLongClick(object : GroupAdapter.OnItemLongClick {
            override fun onItemLongClick(position: Int) {
                // 删除
                showDelDialog(position)
            }
        })

        fab_add.setOnClickListener {
            showCreateDialog()
        }

        val mLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_group_list.itemAnimator = DefaultItemAnimator()
        rv_group_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rv_group_list.layoutManager = mLinearLayoutManager

        groupAdapter.mData = taskGroupService.queryAll()
        rv_group_list.adapter = groupAdapter
    }

    private fun showDelDialog(position: Int) {
        val group = groupAdapter.mData!!.get(position)
        val editDialog = AlertDialog.Builder(this)
        editDialog.setTitle("确认删除？")
        editDialog.setIcon(R.mipmap.ic_launcher_round)
        editDialog.setPositiveButton("确认") { _, _ ->
            group.isDel = 1
            group.updateTime = System.currentTimeMillis()
            taskGroupService.updateDel(group)
            groupAdapter.removeItem(position)
        }
        editDialog.create().show()
    }

    private fun showCreateDialog() {
        val edit = EditText(this)
        val editDialog = AlertDialog.Builder(this)
        editDialog.setTitle("增加分组")
        editDialog.setIcon(R.mipmap.ic_launcher_round)
        editDialog.setView(edit)
        editDialog.setPositiveButton("确认") { _, _ ->
            val name = edit.text.toString().trim()
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "请填写名称", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }
            if (name.length > 10) {
                Toast.makeText(this, "名称不能超过10个字符", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }
            val group = TaskGroup()
            group.id = UUIDUtils.uuid
            group.name = name
            group.isDel = 0
            group.createTime = System.currentTimeMillis()
            group.updateTime = System.currentTimeMillis()
            taskGroupService.insert(group)
            groupAdapter.insertItem(groupAdapter.mData!!.size, group)
        }
        editDialog.create().show()
    }

    private fun showModifyDialog(group: TaskGroup) {
        val edit = EditText(this)
        edit.setText(group.name)
        edit.extendSelection(group.name!!.length)
        val editDialog = AlertDialog.Builder(this)
        editDialog.setTitle("修改名称")
        editDialog.setIcon(R.mipmap.ic_launcher_round)
        editDialog.setView(edit)
        editDialog.setPositiveButton("确认") { _, _ ->
            val name = edit.text.toString().trim()
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "请填写名称", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }
            if (name.length > 10) {
                Toast.makeText(this, "名称不能超过10个字符", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }
            group.name = name
            group.updateTime = System.currentTimeMillis()
            taskGroupService.updateName(group)
            groupAdapter.notifyDataSetChanged()
        }
        editDialog.create().show()
    }
}