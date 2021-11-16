package com.application.venturaapp.tables

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PersonalDatoRoom::class, LaborCulturalRoom::class, LaborCulturalDetalleRoom::class], version = 1 , exportSchema = false)
abstract class PersonalDB:RoomDatabase() {

    abstract fun personalDao () :PersonalDatoDAO?

    companion object{
        @Volatile
        private var INSTANCE: PersonalDB? = null

        fun getDatabase(context: Context):PersonalDB{

           if(INSTANCE==null)
           {
               INSTANCE = Room.databaseBuilder<PersonalDB>(
                   context.applicationContext, PersonalDB::class.java, "AppDB"
               )
                   .allowMainThreadQueries().build()
           }

            return INSTANCE!!
        }
    }

}