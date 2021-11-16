package com.application.venturaapp.helper

import com.google.gson.annotations.SerializedName

data class AyudaListaDato(
        @SerializedName("Class")
        val classX: Any,
        @SerializedName("Disabled")
        val disabled: Boolean,
        @SerializedName("Id")
        val id: Int,
        @SerializedName("IdEstado")
        val idEstado: Int,
        @SerializedName("LabelCombobox")
        val labelCombobox: Any,
        @SerializedName("NameHtml")
        val nameHtml: Any,
        @SerializedName("Nombre")
        val nombre: String,
        @SerializedName("TextHtml")
        val textHtml: Any,
        @SerializedName("ValorDefecto")
        val valorDefecto: Any
)