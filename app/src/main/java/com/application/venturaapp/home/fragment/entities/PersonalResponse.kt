package com.application.venturaapp.home.fragment.entities

import com.google.gson.annotations.SerializedName


data class PersonalResponse<T> (

        @SerializedName("odata.metadata")
        val metadata: String,
        @SerializedName("value")
        val datos: List<T>
)