package com.ekrembas.yemekkitabi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ekrembas.yemekkitabi.databinding.FragmentListeBinding

class ListeFragment : Fragment() {

    private var _binding: FragmentListeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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