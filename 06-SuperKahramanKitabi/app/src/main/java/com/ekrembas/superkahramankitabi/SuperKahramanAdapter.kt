package com.ekrembas.superkahramankitabi

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekrembas.superkahramankitabi.databinding.RecyclerRowBinding

class SuperKahramanAdapter(val superKahramanListesi: ArrayList<SuperKahraman>) :
    RecyclerView.Adapter<SuperKahramanAdapter.SuperKahramanViewHolder>() {
    // Adapter'a verilecek viewHolder sinifi ici bos kaliyor
    class SuperKahramanViewHolder(val binding: RecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    // Adapter'in implement edilmesi gereken fonksiyonlari

    // RecyclerView'i initalize etmek icin fonksiyon
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperKahramanViewHolder {
        // binding'i initialize ettik bu kisim biraz ezbere giriyor kaliplasmis bir yapi burasi
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SuperKahramanViewHolder(binding)
    }

    // Recycler View'da kac tane item olacaginin fonksiyonu
    override fun getItemCount(): Int {
        return superKahramanListesi.size
    }

    // Recycler View icerisindeki item'lara tiklandiginda ne olacak, nereye gidilecek vs.
    override fun onBindViewHolder(holder: SuperKahramanViewHolder, position: Int) {
        holder.binding.textViewRecyclerView.text = superKahramanListesi[position].isim

        // holder'a tiklandiginda ne yapilacak
        holder.itemView.setOnClickListener {
            // intent yapisi (this, ...) diyememizin sebebi bu sinifin aktivite olmamasi
            val intent = Intent(holder.itemView.context, TanitimAktivitesi::class.java)
            // veriyi yollama
            // 1. yontem: yollanacak sinifi Serializable yapmak
            intent.putExtra("secilenKahraman", superKahramanListesi[position])

            // yine this diyememizin sebebi bu sinifin aktivite olmamasi ve bize bir context gerekmesi
            // holder.itemView diyerek parent'in context'ine erisebiliyoruz
            // LayoutInflater.from(parent.context) gibi
            holder.itemView.context.startActivity(intent)
        }
    }
}