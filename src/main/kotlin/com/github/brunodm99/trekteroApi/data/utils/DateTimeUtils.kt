package com.github.brunodm99.trekteroApi.data.utils

import org.apache.tomcat.jni.Local
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


object DateTimeUtils {

    private val DTF = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

    /**
     * Get the current date and time formatted as String
     *
     * @return Returns the date and time formatted as String
     */
    fun currentDateTime(): String = DTF.format(LocalDateTime.now())

    fun asString(dateTime: LocalDateTime): String{
        return DTF.format(dateTime)
    }

    /**
     * Extension function that transforms a String to LocalDateTime
     *
     * @return Returns one LocalDateTime from a String
     */
    fun String.toLocalDateTime(): LocalDateTime{
        return LocalDateTime.from(DTF.parse(this))
    }

    /**
     * Extension function that transforms a LocalDateTime to Date
     *
     * @return Return s The LocalDateTime as Date
     */
    fun LocalDateTime.toDate() = Date.from(this.atZone(ZoneId.systemDefault()).toInstant())

    fun isSameDay(dateTime1: LocalDateTime, dateTime2: LocalDateTime): Boolean{
        return dateTime1.toLocalDate().isEqual(dateTime2.toLocalDate())
    }
}