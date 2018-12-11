package com.hl.dotime.ui.filter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.gson.Gson
import com.hl.dotime.R
import com.hl.dotime.db.entity.FilterList
import com.hl.dotime.entity.ChartOption

class ChartFragment : Fragment() {

    private var wv: WebView? = null
    private var a = "{\"tooltip\":{\"trigger\":\"item\",\"formatter\":\"{a} <br/>{b}: {c} ({d}%)\"},\"legend\":{\"orient\":\"vertical\",\"x\":\"left\",\"data\":[\"直接访问\",\"邮件营销\",\"联盟广告\",\"视频广告\",\"搜索引擎\"]},\"series\":[{\"name\":\"访问来源\",\"type\":\"pie\",\"radius\":[\"50%\",\"70%\"],\"avoidLabelOverlap\":false,\"label\":{\"normal\":{\"show\":false,\"position\":\"center\"},\"emphasis\":{\"show\":true,\"textStyle\":{\"fontSize\":\"30\",\"fontWeight\":\"bold\"}}},\"labelLine\":{\"normal\":{\"show\":false}},\"data\":[{\"value\":335,\"name\":\"直接访问\"},{\"value\":310,\"name\":\"邮件营销\"},{\"value\":234,\"name\":\"联盟广告\"},{\"value\":135,\"name\":\"视频广告\"},{\"value\":1548,\"name\":\"搜索引擎\"}]}]}"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fragment_webview, null)

        wv = contentView.findViewById(R.id.wv)
        val webSettings = wv!!.settings

        webSettings.setJavaScriptEnabled(true)
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true)
        webSettings.setSupportZoom(true)
        webSettings.setDisplayZoomControls(true)

        wv!!.addJavascriptInterface(JsObject(), "Android") // 之前先定义的JS接口
        wv!!.loadUrl("file:///android_asset/echart/echart.html")
        wv!!.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                val a = activity as FilterCountActivity
                updateData(a.getData())
            }
        }

        return contentView
    }

    fun updateData(data: ArrayList<FilterList>?) {
        wv?.loadUrl("javascript:setData('" + handleData(data) + "');")
    }

    private fun handleData(data: List<FilterList>?): String? {
        if (data == null) return null
        val map = hashMapOf<String, ChartOption.Series.Data>()
        val list = ArrayList<String>()
        for (j in data) {
            val before = j.endTime!! - j.startTime!!
            if (map.containsKey(j.taskId)) {
                map.get(j.taskId!!)!!.value = (before + map.get(j.taskId!!)!!.value!!.toLong()).toString()
            } else {
                map.put(j.taskId!!, ChartOption.Series.Data(j.name!!, before.toString()))
                list.add(j.name!!)
            }
        }

        val option = Gson().fromJson<ChartOption>(a, ChartOption::class.java)
        option.legend!!.data = list
        option.series!![0].data = map.values.toList()
        return Gson().toJson(option)
    }

    class JsObject {
        @JavascriptInterface
        fun getData(): String {
            return ""
        }
    }
}