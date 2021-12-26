package com.example.gdscandroidproject

import com.google.gson.annotations.SerializedName

data class Cases(
    val active: Int? = null,
    val confirmed: Int? = null,
    val deceased: Int? = null,
    val recovered: Int? = null,
) {
    data class CountryInfo(
        val flag: String,
        @SerializedName("_id")
        val id: Int
    )
}