package com.ekrembas.yemekkitabi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tarif(
    // column isimleri
    @ColumnInfo(name = "isim")
    var isim: String,
    @ColumnInfo(name = "malzeme")
    var malzeme: String,
    @ColumnInfo(name = "gorsel")
    var gorsel: ByteArray
) {
    // primary key'i kendi kendine arttirsin
    @PrimaryKey(autoGenerate = true)
    var id = 0
}