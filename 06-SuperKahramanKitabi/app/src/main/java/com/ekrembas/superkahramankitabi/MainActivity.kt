package com.ekrembas.superkahramankitabi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ekrembas.superkahramankitabi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // super kahraman listesi
    private lateinit var superKahramanListesi: ArrayList<SuperKahraman>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Super kahramanlar
        val superman = SuperKahraman("Superman", "Gazeteci", R.drawable.superman)
        val batman = SuperKahraman("Batman", "CEO", R.drawable.batman)
        val aquaman = SuperKahraman("Aquaman", "Kral", R.drawable.aquaman)
        val ironman = SuperKahraman("Iron Man", "Holding Sahibi", R.drawable.ironman)

        // listenin doldurulmasi
        superKahramanListesi = arrayListOf(superman, batman, aquaman, ironman)
    }
}