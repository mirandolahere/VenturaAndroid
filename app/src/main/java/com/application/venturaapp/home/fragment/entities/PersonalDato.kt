package com.application.venturaapp.home.fragment.entities

import com.google.gson.annotations.SerializedName

data class      PersonalDato(

    @SerializedName("odata.metadata")
    val metadata: String,
    @SerializedName("Code")
    var Code: String,
    @SerializedName("Name")
    val Name: String,
    @SerializedName("U_VS_AGR_BPNO")
    var PrimerNombre: String,
    @SerializedName("U_VS_AGR_BPN2")
    var SegundoNombre: String,
    @SerializedName("U_VS_AGR_BPAP")
    var ApellidoPaterno: String,
    @SerializedName("U_VS_AGR_BPAM")
    var ApellidoMaterno: String,
    @SerializedName("U_VS_AGR_TIDO")
    var TipoDocumento: String,
    @SerializedName("U_VS_AGR_DOIN")
    var NumeroDocumento: String,
    @SerializedName("U_VS_AGR_DIRE")
    val Dirrecion: String?,
    @SerializedName("U_VS_AGR_TEL1")
    val Telefono1: String?,
    @SerializedName("U_VS_AGR_TEL2")
    val Telefono2: String?,
    @SerializedName("U_VS_AGR_LBCA")
    var CodigoLabor: String,
    @SerializedName("U_VS_AGR_DELA")
    var DescripcionLabor: String,
    @SerializedName("U_VS_AGR_FENA")
    var FechaNacimiento: String,
    @SerializedName("U_VS_AGR_NUCO")
    val NumeroContrato: String?,
    @SerializedName("U_VS_AGR_TEMP")
    var TipoEmpleado: String,
    @SerializedName("U_VS_AGR_CPRV")
    val CodigoProveedor: String,
    @SerializedName("U_VS_AGR_NPROV")
    val NombreProveedor: String,
    @SerializedName("U_VS_AGR_ACTV")
    val Activo: String,
    @SerializedName("U_VS_AGR_USCA")
    val UsuarioCreador: String?,
    @SerializedName("U_VS_AGR_USAA")
    val UsuarioActualizador: String?,
    @SerializedName("U_VS_AGR_RORI")
    val OrigenRegistro: String?,
    @SerializedName("VS_AGR_CONTCollection")
    val Collection: List<PersonalDatoCollection>?
)