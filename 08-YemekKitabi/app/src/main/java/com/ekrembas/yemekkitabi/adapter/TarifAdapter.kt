package com.ekrembas.yemekkitabi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ekrembas.yemekkitabi.databinding.RecyclerRowBinding
import com.ekrembas.yemekkitabi.model.Tarif
import com.ekrembas.yemekkitabi.view.ListeFragmentDirections

// RecyclerView.Adapter<TarifAdapter.TarifHolder>() sınıfından miras alır.
// Bir yemek tarifleri listesi (tarifListesi) alır ve RecyclerView içinde göstermek için kullanır.
// val tarifListesi: List<Tarif> → Bu, tariflerin bir listesidir. Liste, Tarif modelini içerir.
class TarifAdapter(val tarifListesi: List<Tarif>) :
    RecyclerView.Adapter<TarifAdapter.TarifHolder>() {

    class TarifHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        // 	TarifHolder sınıfı, her bir öğe (row) için bir ViewHolder tanımlar.
        //	RecyclerRowBinding sınıfını kullanarak recycler_row.xml’i bağlar.
        //	ViewHolder, binding.root üzerinden ilgili öğeye erişim sağlar.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarifHolder {
        // LayoutInflater.from() -> XML dosyasını Java/Kotlin nesnesine dönüştürmek için kullanılır.
        // RecyclerRowBinding.inflate(..., parent, false) → recycler_row.xml dosyasını bağlar.
        // return TarifHolder(recyclerRowBinding) → Yeni bir TarifHolder nesnesi döndürür.
        val recyclerRowBinding =
            RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TarifHolder(recyclerRowBinding)
    }

    override fun getItemCount(): Int {
        return tarifListesi.size
    }

    // holder.binding.recyclerViewTextView.text = tarifListesi[position].isim -> recycler_row.xml içinde bulunan TextView öğesine tarif adını (isim) atar.
    // holder.itemView.setOnClickListener { ... } -> Kullanıcı bir öğeye tıkladığında çalışacak işlemi tanımlar.
    // Sayfa Geçişi (Navigation Component) val action = ListeFragmentDirections...
    // Safe Args kullanarak, TarifFragment adlı sayfaya geçiş yapar.
    // bilgi = "eski" → TarifFragment’a bir parametre olarak "eski" bilgisini gönderir.
    // tarifListesi[position].id → Seçilen tarifin ID’sini yeni sayfaya gönderir.
    override fun onBindViewHolder(holder: TarifHolder, position: Int) {
        holder.binding.recyclerViewTextView.text = tarifListesi[position].isim
        holder.itemView.setOnClickListener {
            val action = ListeFragmentDirections.actionListeFragmentToTarifFragment(
                bilgi = "eski",
                tarifListesi[position].id
            )
            Navigation.findNavController(it).navigate(action)
        }
    }
}