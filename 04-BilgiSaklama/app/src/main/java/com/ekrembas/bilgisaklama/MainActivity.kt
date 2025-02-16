package com.ekrembas.bilgisaklama

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ekrembas.bilgisaklama.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Shared Preferences
    lateinit var sharedPreferences: SharedPreferences

    var kullanicininIsmi: String? = null

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

        sharedPreferences = this.getSharedPreferences(
            "com.ekrembas.bilgisaklama",
            MODE_PRIVATE // baska uygulamalar erisemez
        ) // this zorunlu degil

        // kaydedilmis ismi gosterelim
        kullanicininIsmi = sharedPreferences.getString("isim", "")
        // eger kaydedilen isim yoksa 'Kaydedilen İsim: ' gozukecek
        binding.textView.text = "Kaydedilen İsim: ${kullanicininIsmi}" ?: "Kaydedilen İsim: "
    }

    fun kaydet(view: View) {
        val kullaniciIsmi = binding.editText.text.toString()
        if (kullaniciIsmi == "") {
            Toast.makeText(this@MainActivity, "İsminizi Boş Bırakmayınız!", Toast.LENGTH_LONG)
                .show()
        } else {
            // isim kismi bos degilse ismi kaydet
            sharedPreferences.edit().putString("isim", kullaniciIsmi).apply()
            binding.textView.text = "Kaydedilen İsim: ${kullaniciIsmi}"
            binding.editText.text.clear()
        }
    }

    fun sil(view: View) {
        kullanicininIsmi = sharedPreferences.getString("isim", "")
        if (kullanicininIsmi != "") {
            sharedPreferences.edit().remove("isim").apply()
            binding.textView.text = "Kaydedilen İsim: "
            binding.editText.text.clear()
        }
    }
}