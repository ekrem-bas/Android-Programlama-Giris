package com.ekrembas.yemekkitabi.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ekrembas.yemekkitabi.model.Tarif

@Dao
interface TarifDAO {
    // Database'deki Tarif tablosundan tum satirlari ve sutunlari getir
    @Query("SELECT * FROM Tarif")
    fun getAll(): List<Tarif>

    // ID bilgisine gore tarif alma
    @Query("SELECT * FROM Tarif WHERE id = :id")
    fun findById(id: Int): Tarif

    @Insert
    fun insert(tarif: Tarif)

    @Delete
    fun delete(tarif: Tarif)

}