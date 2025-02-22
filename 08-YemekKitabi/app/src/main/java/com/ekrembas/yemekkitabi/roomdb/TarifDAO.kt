package com.ekrembas.yemekkitabi.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ekrembas.yemekkitabi.model.Tarif
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface TarifDAO {
    // Database'deki Tarif tablosundan tum satirlari ve sutunlari getir
    @Query("SELECT * FROM Tarif")
    fun getAll(): Flowable<List<Tarif>>
    // flowable: 0..N flows, supporting Reactive-Streams and backpressure

    // ID bilgisine gore tarif alma
    @Query("SELECT * FROM Tarif WHERE id = :id")
    fun findById(id: Int): Flowable<Tarif>

    @Insert
    fun insert(tarif: Tarif): Completable

    @Delete
    fun delete(tarif: Tarif): Completable // geriye bir sey dondurmediginden completable
    // completable: a flow without items but only a completion or error signal

}