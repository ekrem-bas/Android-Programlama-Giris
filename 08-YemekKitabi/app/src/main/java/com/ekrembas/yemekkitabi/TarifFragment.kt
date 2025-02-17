package com.ekrembas.yemekkitabi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ekrembas.yemekkitabi.databinding.FragmentTarifBinding

class TarifFragment : Fragment() {

    private var _binding: FragmentTarifBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTarifBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // butonlarin onClickListener ayarlari
        binding.imageView.setOnClickListener { gorselSec(it) }
        binding.kaydetButton.setOnClickListener { kaydet(it) }
        binding.silButton.setOnClickListener { sil(it) }

        arguments?.let {
            val bilgi = TarifFragmentArgs.fromBundle(it).bilgi
            if (bilgi == "yeni") {
                // yeni tarif eklenecek
                binding.silButton.isEnabled = false
                binding.isimText.setText("")
                binding.malzemeText.setText("")
            } else {
                // var olan tarif gosterilecek
                binding.kaydetButton.isEnabled = false
            }
            val id = TarifFragmentArgs.fromBundle(it).id
        }
    }

    fun kaydet(view: View) {

    }

    fun sil(view: View) {

    }

    fun gorselSec(view: View) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}