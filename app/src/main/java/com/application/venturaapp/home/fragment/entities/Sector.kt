package com.application.venturaapp.home.fragment.entities

import com.google.gson.annotations.SerializedName

data class Sector (

        @SerializedName("Canceled")
         val  Canceled: String, 
        @SerializedName("Code")
         val  Code: String, 
        @SerializedName("CreateDate")
         val  CreateDate: String, 
        @SerializedName("CreateTime")
         val  CreateTime: String, 
        @SerializedName("DataSource")
         val  DataSource: String, 
        @SerializedName("DocEntry")
         val  DocEntry: String, 
        @SerializedName("Name")
         val  Name: String, 
        @SerializedName("Object")
         val  Object: String, 
        @SerializedName("U_VS_AGR_ACTV")
         val  U_VS_AGR_ACTV: String, 
        @SerializedName("U_VS_AGR_CDFD")
         val  U_VS_AGR_CDFD: String, 
        @SerializedName("U_VS_AGR_FNDO")
         val  U_VS_AGR_FNDO: String, 
        @SerializedName("U_VS_AGR_RORI")
         val  U_VS_AGR_RORI: String, 
        @SerializedName("U_VS_AGR_USAA")
         val  U_VS_AGR_USAA: String, 
        @SerializedName("U_VS_AGR_USCA")
         val  U_VS_AGR_USCA: String, 
        @SerializedName("UpdateDate")
         val  UpdateDate: String, 
        @SerializedName("UpdateTime")
         val  UpdateTime: String, 
        @SerializedName("UserSign")
         val  UserSign: String, 
        @SerializedName("VS_AGR_LOTECollection")
         val  VS_AGR_LOTECollection: List<Lote>,
        @SerializedName("odata.metadata")
         val  metadata: String, 
)