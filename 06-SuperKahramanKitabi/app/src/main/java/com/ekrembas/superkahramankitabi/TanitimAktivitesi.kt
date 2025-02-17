package com.ekrembas.superkahramankitabi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ekrembas.superkahramankitabi.databinding.ActivityTanitimAktivitesiBinding

class TanitimAktivitesi : AppCompatActivity() {

    private lateinit var binding: ActivityTanitimAktivitesiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTanitimAktivitesiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // bu yeni bir android surumunde geldigi icin eski cihazlarda calismayabilir (API Level 33)
        //val secilenKahraman = intent.getSerializableExtra("secilenKahraman", SuperKahraman::class.java)
        // eski hali
        val secilenKahraman = intent.getSerializableExtra("secilenKahraman") as SuperKahraman
        // kahramanin gorselinin, isminin ve mesleginin ayarlanmasi
        binding.imageView2.setImageResource(secilenKahraman.gorsel)
        binding.isimTextView.text = secilenKahraman.isim
        binding.meslekTextView.text = secilenKahraman.meslek
    }
}