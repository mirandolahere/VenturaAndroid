package com.application.venturaapp.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.application.venturaapp.home.fragment.entities.PersonalDato

@Entity(tableName = "LaborCulturalRoom")
 data class LaborCulturalRoom (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "U_VS_AGR_FERG")
    val U_VS_AGR_FERG: String,
    @ColumnInfo(name = "U_VS_AGR_CDCA")
    val U_VS_AGR_CDCA: String,
    @ColumnInfo(name = "U_VS_AGR_CDPP")
    val U_VS_AGR_CDPP: String,
    @ColumnInfo(name = "U_VS_AGR_CDEP")
    val U_VS_AGR_CDEP: String,
    @ColumnInfo(name = "U_VS_AGR_ACTV")
    val U_VS_AGR_ACTV: String,
    @ColumnInfo(name = "U_VS_AGR_RORI")
    val U_VS_AGR_RORI: String,

)