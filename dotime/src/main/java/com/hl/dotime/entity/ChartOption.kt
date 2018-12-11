package com.hl.dotime.entity

class ChartOption {
    var tooltip: Tooltip? = null
    var legend: Legend? = null
    var series: List<Series>? = null

    class Tooltip {
        var trigger: String? = null
        var formatter: String? = null
    }

    class Legend {
        var orient: String? = null
        var x: String? = null
        var data: List<String>? = null  // ["直接访问", "邮件营销", "联盟广告", "视频广告", "搜索引擎"]
    }

    class Series {
        var name: String? = null
        var type: String? = "pie"
        var radius: List<String>? = null
        var avoidLabelOverlap: Boolean? = null
        var label: Label? = null
        var labelLine: LabelLine? = null
        var data: List<Data>? = null

        class Label {
            var normal: Normal? = null
            var emphasis: Emphasis? = null

            class Normal {
                var show: Boolean? = null
                var position: String? = null
            }

            class Emphasis {
                var show: Boolean? = null
                var textStyle: TextStyle? = null

                class TextStyle {
                    var fontSize: String? = null
                    var fontWeight: String? = null
                }
            }
        }

        class LabelLine {
            var normal: Normal? = null

            class Normal {
                var show: Boolean? = null
            }
        }

        class Data(var name: String?, var value: String?) {

        }
    }
}

