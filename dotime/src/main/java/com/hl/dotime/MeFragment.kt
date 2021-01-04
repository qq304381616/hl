package com.hl.dotime

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.hl.dotime.db.MySQLiteOpenHelper
import com.hl.dotime.utils.Utils

class MeFragment : Fragment() {

    companion object {
        private const val EXTRA_CONTENT = "content"

        fun newInstance(content: String): MeFragment {
            val arguments = Bundle()
            arguments.putString(EXTRA_CONTENT, content)
            val tabContentFragment = MeFragment()
            tabContentFragment.arguments = arguments
            return tabContentFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fragment_me, container, false)

        contentView.findViewById<TextView>(R.id.tv_backup_db).setOnClickListener {
            if (Utils.copyFile(activity!!.getDatabasePath(MySQLiteOpenHelper.dbName), Utils.getAppSDCardPath(MySQLiteOpenHelper.dbName))) {
                Toast.makeText(context, "备份成功, 路径：" + Utils.getAppSDCardPath(MySQLiteOpenHelper.dbName), Toast.LENGTH_SHORT).show()
            }
        }
        contentView.findViewById<TextView>(R.id.tv_recover_db).setOnClickListener {
            val dialog = AlertDialog.Builder(activity!!)
            dialog.setTitle("提示？")
            dialog.setMessage("此操作将恢复数据到最近一次的备份！恢复成功后请重新启动。")
            dialog.setIcon(R.mipmap.ic_launcher_round)
            dialog.setPositiveButton("确认") { _, _ ->
                if (!Utils.copyFile(Utils.getAppSDCardPath(MySQLiteOpenHelper.dbName), activity!!.getDatabasePath(MySQLiteOpenHelper.dbName))) {
                    Toast.makeText(context, "恢复失败，未找到备份文件", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "恢复成功", Toast.LENGTH_SHORT).show()
                    System.exit(0)
                }
            }
            dialog.setNegativeButton("取消", null)
            dialog.create().show()
        }
        contentView.findViewById<TextView>(R.id.tv_about).setOnClickListener {
            Toast.makeText(context, Utils.getAppVersionCode(context!!), Toast.LENGTH_SHORT).show()
        }
        return contentView
    }
}