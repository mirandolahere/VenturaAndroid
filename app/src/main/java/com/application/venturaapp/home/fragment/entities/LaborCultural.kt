package com.application.venturaapp.home.fragment.entities

import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

data class LaborCultural (

        @SerializedName("odata.metadata")
        val metadata: String,
        @SerializedName("Code")
        val Code: String,
        @SerializedName("Name")
        val Name: String,
        @SerializedName("DocEntry")
        val Cabecera: String,
        @SerializedName("Canceled")
        val Cancelado: String,
        @SerializedName("Object")
        val Objecto: String,
        @SerializedName("LogInst")
        val LogInst: String,
        @SerializedName("UserSign")
        val UserSign: String,
        @SerializedName("Transfered")
        val Transfered: String,
        @SerializedName("CreateDate")
        val CreateDate: String,
        @SerializedName("CreateTime")
        val CreateTime: String,
        @SerializedName("UpdateDate")
        val UpdateDate: String,
        @SerializedName("UpdateTime")
        val UpdateTime: String,
        @SerializedName("DataSource")
        val DataSource: String,
        @SerializedName("U_VS_AGR_COJR")
        val CodigoRecursoJornal: String,
        @SerializedName("U_VS_AGR_DEJR")
        val DescrRecursoJornal: String,
        @SerializedName("U_VS_AGR_CXJR")
        val CostoXJornal: Double,
        @SerializedName("U_VS_AGR_TOHR")
        val TotalHorasXJornal: Double,
        @SerializedName("U_VS_AGR_HEXT")
        val CodigoRecursoHorExtr: String,
        @SerializedName("U_VS_AGR_DEHR")
        val DescrRecursoHoraExtr: String,
        @SerializedName("U_VS_AGR_ACTV")
        val Activo: String,
        @SerializedName("U_VS_AGR_USCA")
        val UsuarioCreadorApp: String,
        @SerializedName("U_VS_AGR_USAA")
        val UsuarioActualizadorApp: String,
        @SerializedName("U_VS_AGR_RORI")
        val OrigenRegistro: String,
        @SerializedName("U_VS_AGR_CDRJ")
        val CodigoRecursoJornal2 : String,
        @SerializedName("U_VS_AGR_DSRJ")
        val DescrRecursoJornal2: String,
        @SerializedName("U_VS_AGR_CDRX")
        val CodigoRecursoHorExtr2 : String,
        @SerializedName("U_VS_AGR_DSRX")
        val DescrRecursoHoraExtr2: String

)