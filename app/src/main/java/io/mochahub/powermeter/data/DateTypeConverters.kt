package io.mochahub.powermeter.data

import androidx.room.TypeConverters
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object DateTypeConverters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverters
    @JvmStatic
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return formatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverters
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(formatter)
    }
}