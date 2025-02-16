package com.ekrembas.yasamdongusu

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ekrembas.yasamdongusu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // kullanici daha uygulamayi gormeden calisiyor
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

        println("onCreate calistirildi...")
    }

    // kullanici uygulamayi goruyor ama etkilesime giremedigi an (cok ufak bir sn 'splash screen' hemen sonrasi)
    // onResume'ye hazirlik
    override fun onStart() {
        super.onStart()

        println("onStart calistirildi...")
    }

    // kullanici artik uygulamayla etkilesime girebilir
    override fun onResume() {
        super.onResume()
        println("onResume calistirildi...")
    }

    // onStop'a hazirlik
    override fun onPause() {
        super.onPause()
        println("onPause calistirildi...")
    }

    // kullanici uygulamayi arka plana attiginda calisir
    override fun onStop() {
        super.onStop()
        println("onStop calistirildi...")
    }

    // kullanici uygulamayi kapattiginda calisir (arka plandan da kapatinca)
    override fun onDestroy() {
        super.onDestroy()
        println("onDestroy calistirildi...")
    }

    fun sonrakiSayfa(view: View) {
        val textController = binding.editText.text.toString()
        binding.textView.text = "İsim: ${textController}"
    }
}