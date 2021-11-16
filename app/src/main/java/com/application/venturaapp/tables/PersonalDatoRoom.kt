package com.application.venturaapp.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.application.venturaapp.home.fragment.entities.PersonalDato

@Entity(tableName = "PersonalDatoRoom")
 data class PersonalDatoRoom (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "metadata")
    val metadata: String?,
   @ColumnInfo(name = "Code")
    val Code: String,
    @ColumnInfo(name = "Name")
    val Name: String,
    @ColumnInfo(name = "PrimerNombre")
    val PrimerNombre: String,
    @ColumnInfo(name = "SegundoNombre")
    val SegundoNombre: String,
    @ColumnInfo(name = "ApellidoPaterno")
    val ApellidoPaterno: String,
    @ColumnInfo(name = "ApellidoMaterno")
    val ApellidoMaterno: String,
    @ColumnInfo(name = "TipoDocumento")
    val TipoDocumento: String,
    @ColumnInfo(name = "NumeroDocumento")
    val NumeroDocumento: String,
    @ColumnInfo(name = "Dirrecion")
    val Dirrecion: String?,
    @ColumnInfo(name = "Telefono1")
    val Telefono1: String?,
    @ColumnInfo(name = "Telefono2")
    val Telefono2: String?,
    @ColumnInfo(name = "CodigoLabor")
    val CodigoLabor: String,
    @ColumnInfo(name = "DescripcionLabor")
    val DescripcionLabor: String,
    @ColumnInfo(name = "FechaNacimiento")
    val FechaNacimiento: String,
    @ColumnInfo(name = "NumeroContrato")
    val NumeroContrato: String?,
    @ColumnInfo(name = "TipoEmpleado")
    val TipoEmpleado: String,
    @ColumnInfo(name = "CodigoProveedor")
    val CodigoProveedor: String,
    @ColumnInfo(name = "NombreProveedor")
    val NombreProveedor: String,
    @ColumnInfo(name = "Activo")
    var Activo: String,
    @ColumnInfo(name = "UsuarioCreador")
    val UsuarioCreador: String?,
    @ColumnInfo(name = "UsuarioActualizador")
    val UsuarioActualizador: String?,
    @ColumnInfo(name = "OrigenRegistro")
    val OrigenRegistro: String?
)