package com.ekrembas.kullaniciarayuzu

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ekrembas.kullaniciarayuzu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // 2. yontem icin gerekli olan degisken
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 2. yontem icin gerekli olanlar
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //////////////////////////////////////////////////////

        // 2. yontemdeki setContentView'dan dolayi bu satira gerek yok
        // setContentView(R.layout.activity_main) // bu gornumu xml dosyasina baglama

        // bu satirlar telefon ekranindaki status bar vs gibi ozellikler icin
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // xml dosyasinda yapmis oldugumuz komponentlere nasil ulasabiliriz?

        // 1. yontem ama pek tercih edilmiyor
        // R.id -> tum id'leri tek tek gezip match etmeye calisiyor bu yuzden
        // O(n) zamanada oldugundan cok fazla id olursa kotu olur
        val image = findViewById<ImageView>(R.id.imageView)
        val customText = findViewById<TextView>(R.id.textView)
        customText.text = "Merhaba Kotlin"

        // 2. yontem View Binding (build.gradle.kts dosyasina buildFeatures eklemesi)
        binding.textView.text = "Binding yöntemiyle erişilen text view"

        // butona tiklandiginda bir islem gerceklestirelim

        // 1. yontem
//        binding.button.setOnClickListener {
//            binding.textView.text = "Kaydet butonuna tıklandı"
//
//        }

        // 2. yontem


    }

    // xml dosyasinda onClick attribute kismina ne yazdiysak ayni isimle fonksiyon yaziyoruz
    fun kaydet(view: View) {
        binding.textView.text = "Kaydet butonuna basildi"
    }

    fun iptal(view: View) {
        binding.textView.text = "İptal butonuna basildi"
    }
}