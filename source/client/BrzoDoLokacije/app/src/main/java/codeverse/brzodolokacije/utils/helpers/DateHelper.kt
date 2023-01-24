package codeverse.brzodolokacije.utils.helpers

import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateHelper {

    companion object {

        @RequiresApi(Build.VERSION_CODES.O)
        fun formatApiDate(apiDate: String): String {

            var formatter = DateTimeFormatter.ofPattern("HH:mm', 'dd/MM/yy")
            val date = getLocalDateTime(apiDate)

            val formatedDate = date.format(formatter)

            return formatedDate
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getLocalDateTime(date: String): LocalDateTime{
            val splitString = date.split(".")

            val apiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val date = LocalDateTime.parse(splitString[0] , apiFormat)

            return date
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun formatLocalDateTime(date: LocalDateTime): String? {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'hh:mm:ss")

            val formatedDate = date.format(formatter)

            return formatedDate
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getTimeStamp(date: String): Long {

            val localDate = getLocalDateTime(date)

            val formatedDate = formatLocalDateTime(localDate)
            if(formatedDate != null){
                return Timestamp.valueOf(formatedDate).time
            }
            else return 0L
        }
    }
}