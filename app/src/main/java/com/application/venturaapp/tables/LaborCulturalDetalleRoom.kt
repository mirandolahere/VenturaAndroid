package com.application.venturaapp.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.application.venturaapp.home.fragment.entities.PersonalDato

@Entity(tableName = "LaborCulturalDetalleRoom")
 data class LaborCulturalDetalleRoom (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "DocEntry")
    val DocEntry: Int?,
    @ColumnInfo(name = "LineId")
    val LineId: Int,
    @ColumnInfo(name = "VisOrder")
    val VisOrder: Int,
    @ColumnInfo(name = "Object")
    val Object: String,
    @ColumnInfo(name = "LogInst")
    val LogInst: String,
    @ColumnInfo(name = "U_VS_AGR_CDLC")
    val U_VS_AGR_CDLC: String,
    @ColumnInfo(name = "U_VS_AGR_CDPS")
    val U_VS_AGR_CDPS: String,
    @ColumnInfo(name = "U_VS_AGR_HRIN")
    val U_VS_AGR_HRIN: String,
    @ColumnInfo(name = "U_VS_AGR_HRFN")
    val U_VS_AGR_HRFN: String,
    @ColumnInfo(name = "U_VS_AGR_ESTA")
    val U_VS_AGR_ESTA: String,
    @ColumnInfo(name = "U_VS_AGR_LNEP")
    val U_VS_AGR_LNEP: Int,
    @ColumnInfo(name = "U_VS_AGR_ACTV")
    val U_VS_AGR_ACTV: String,
    @ColumnInfo(name = "U_VS_AGR_USCA")
    val U_VS_AGR_USCA: String,
    @ColumnInfo(name = "U_VS_AGR_USAA")
    val U_VS_AGR_USAA: String,
    @ColumnInfo(name = "U_VS_AGR_RORI")
    val U_VS_AGR_RORI: String,
    @ColumnInfo(name = "U_VS_AGR_TOJR")
    val U_VS_AGR_TOJR: Int,
    @ColumnInfo(name = "U_VS_AGR_TOHX")
    val U_VS_AGR_TOHX: Int,
)