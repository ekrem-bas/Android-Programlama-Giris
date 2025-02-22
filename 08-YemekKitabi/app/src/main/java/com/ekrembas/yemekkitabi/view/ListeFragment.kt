package com.ekrembas.yemekkitabi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.room.Room
import com.ekrembas.yemekkitabi.databinding.FragmentListeBinding
import com.ekrembas.yemekkitabi.roomdb.TarifDAO
import com.ekrembas.yemekkitabi.roomdb.TarifDatabase

class ListeFragment : Fragment() {

    private var _binding: FragmentListeBinding? = null
    private val binding get() = _binding!!

    // database icin degiskenler
    private lateinit var db: TarifDatabase
    private lateinit var tarifDAO: TarifDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(
            requireContext(),
            TarifDatabase::class.java, "Tarifler" // Database'e vermek istedigimiz isim
        ).build()
        tarifDAO = db.tarifDAO()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // yeni tarif ekleme fonksiyonunu butona atama
        binding.floatingActionButton.setOnClickListener {
            yeniEkle(it)
        }
    }

    // yeni tarif ekleme fonksiyonu
    fun yeniEkle(view: View) {
        val action = ListeFragmentDirections.actionListeFragmentToTarifFragment("yeni", 0)
        Navigation.findNavController(view).navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}