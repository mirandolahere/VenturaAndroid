package com.application.venturaapp.tables

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PersonalDatoDAO {


        @Query("SELECT * FROM PersonalDatoRoom")
        fun getAll(): List<PersonalDatoRoom>?

        @Query("SELECT * FROM LaborCulturalRoom")
        fun getLaborAll(): List<LaborCulturalRoom>?

        @Query("SELECT * FROM LaborCulturalRoom WHERE U_VS_AGR_CDPP =:CodigoPEP ")
        fun getLaborPorPEP(CodigoPEP:String): List<LaborCulturalRoom>?

        @Query("SELECT * FROM LaborCulturalRoom ORDER BY id DESC LIMIT 1")
        fun getLaborAllID(): List<LaborCulturalRoom>?

        @Query("SELECT * FROM LaborCulturalDetalleRoom WHERE DocEntry = :DocEntry")
        fun getDetalleAll(DocEntry : Int): List<LaborCulturalDetalleRoom>?

        @Query("SELECT * FROM LaborCulturalDetalleRoom")
        fun getDetallWithouteAll(): List<LaborCulturalDetalleRoom>?
        /*@Query("SELECT * FROM user WHERE uid IN (:userIds)")
        fun loadAllByIds(userIds: IntArray): List<PersonalDatoRoom>*/

        @Query("SELECT * FROM PersonalDatoRoom WHERE Code LIKE :Code")
        fun findByName(Code: String): PersonalDatoRoom

        @Insert
        fun insertPersonal( personal: PersonalDatoRoom?)

        @Query("DELETE FROM PersonalDatoRoom")
        fun deletePersonal()

        @Query("DELETE FROM LaborCulturalRoom")
        fun deleteLabor()

        @Query("DELETE FROM LaborCulturalDetalleRoom")
        fun deleteDetalle()

        @Insert
        fun insertLaborCultural( labor: LaborCulturalRoom?)

        @Insert
        fun insertLaborDetalleCultural( labor: LaborCulturalDetalleRoom?)

        @Update
        fun updateDetalle( labor: LaborCulturalDetalleRoom?)

}