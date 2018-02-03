package com.sai.weatherio.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by sai on 2/3/18.
 */
@Entity data class SingleDayForecast(
        @PrimaryKey(autoGenerate = true) var id: Long,
        var dayName: String,
        var iconUrl: String,
        var description: String,
        var city: String,
        var state: String
) {
    constructor(): this(0, "", "", "", "", "")
}