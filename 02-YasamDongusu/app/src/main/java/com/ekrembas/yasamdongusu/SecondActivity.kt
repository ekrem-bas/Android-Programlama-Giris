package com.ekrembas.yasamdongusu

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ekrembas.yasamdongusu.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySecondBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // onceki sayfadaki kullanici girdisini bu sayfada al
        val kullaniciIsmi = intent.getStringExtra("isim")
        binding.textView2.text = kullaniciIsmi
    }

    fun geriDon(view: View) {
//
//        // boyle yapilirsa birden cok main activty olur ve bir yerden sonra Android kendi kendine
//        // hafizasi icin bazilarini kapatabilir.
//
//        // bu kisima bu uygulama icin gerek yok
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
    }

}