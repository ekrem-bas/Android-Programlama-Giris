package com.ekrembas.yemekkitabi.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ekrembas.yemekkitabi.model.Tarif

// version degiskeni eger tablona yeni sutunlar vs eklersen diye onun gidip 2, 3 gibi degistirebilirsin
@Database(entities = [Tarif::class], version = 1)
abstract class TarifDatabase : RoomDatabase() {
    abstract fun tarifDAO(): TarifDAO
}