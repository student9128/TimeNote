package com.kevin.timenote.common.util

object ChineseLunarParser {

    private val CN_NUMERIC = mapOf(
        '〇' to 0, '零' to 0,
        '一' to 1, '二' to 2, '三' to 3, '四' to 4, '五' to 5,
        '六' to 6, '七' to 7, '八' to 8, '九' to 9, '十' to 10,
        '正' to 1, '冬' to 11, '腊' to 12, '初' to 0, '廿' to 20, '卅' to 30
    )

    /**
     * 解析农历中文字符串
     * 输入示例: "二〇二五年冬月初九"
     * 输出: Triple(2025, 11, 9) (年, 月, 日)
     */
    fun parse(lunarStr: String): Triple<Int, Int, Int>? {
        try {
            // 简单正则匹配：xxxx年xx月xx
            // 更加健壮的正则，考虑到"闰"字
            val regex =
                Regex("([〇一二三四五六七八九零]+)年(闰?)([正一二三四五六七八九十冬腊]+)月([初十廿卅][一二三四五六七八九十]?)")
            val matchResult = regex.find(lunarStr) ?: return null

            val (yearStr, leapStr, monthStr, dayStr) = matchResult.destructured

            val year = parseYear(yearStr)
            var month = parseMonth(monthStr)
            val day = parseDay(dayStr)

            // 如果是闰月，但在数字表示中通常库会用负数或其他方式标记，这里暂时只返回正数月份
            // 如果你的 Lunar 库需要特殊处理闰月（比如 month = -11），请在此调整
            // com.nlf.calendar.Lunar 库通常通过 Lunar.fromYmd(year, -month, day) 来表示闰月
            if (leapStr.isNotEmpty()) {
                month = -month
            }

            return Triple(year, month, day)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun parseYear(cnYear: String): Int {
        var year = 0
        cnYear.forEach { char ->
            val num = CN_NUMERIC[char] ?: 0
            if (num > 9) return@forEach // 年份通常是按位读的，不应该有十/百
            year = year * 10 + num
        }
        return year
    }

    private fun parseMonth(cnMonth: String): Int {
        return CN_NUMERIC[cnMonth[0]] ?: 1 // "冬" -> 11, "正" -> 1
    }

    private fun parseDay(cnDay: String): Int {
        if (cnDay.isEmpty()) return 1

        // 处理 "初九", "十五", "二十", "廿一", "三十"
        val firstChar = cnDay[0]
        val secondChar = if (cnDay.length > 1) cnDay[1] else null

        val v1 = CN_NUMERIC[firstChar] ?: 0

        if (secondChar == null) {
            // 只有一位的情况，极少见，除非是"三十"写成了"卅"
            return v1
        }

        val v2 = CN_NUMERIC[secondChar] ?: 0

        return when (firstChar) {
            '初' -> v2
            '十' -> 10 + v2
            '廿' -> 20 + v2
            '卅' -> 30 + v2
            '二' -> if (secondChar == '十') 20 else 20 + v2 // 容错 "二十X"
            '三' -> if (secondChar == '十') 30 else 30 + v2 // 容错 "三十"
            else -> v1 + v2
        }
    }
}