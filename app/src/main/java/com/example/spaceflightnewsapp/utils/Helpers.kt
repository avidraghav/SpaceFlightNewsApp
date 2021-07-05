package com.example.spaceflightnewsapp.utils

import com.example.spaceflightnewsapp.utils.Constants.Companion.DATE_OUTPUT_FORMAT
import com.example.spaceflightnewsapp.utils.Constants.Companion.LAUNCH_DATE_INPUT_FORMAT
import java.text.SimpleDateFormat
import java.util.*

class Helpers {

    companion object{
        fun String.toDate(dateFormat: String, timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date {
            val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
            parser.timeZone = timeZone
            return parser.parse(this)
        }

        fun Date.formatTo(dateFormat: String , timeZone: TimeZone = TimeZone.getDefault()): String {
            val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
            formatter.timeZone = timeZone
            return formatter.format(this)
        }


    }


}