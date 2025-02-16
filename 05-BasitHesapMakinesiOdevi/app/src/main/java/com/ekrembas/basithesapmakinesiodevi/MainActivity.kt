package com.ekrembas.basithesapmakinesiodevi

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ekrembas.basithesapmakinesiodevi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // degiskenler
    var num1: Double? = null
    var num2: Double? = null
    var sonuc: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // birinci sayiyi double olarak ya da null olarak dondur
    fun getBirinciSayi(): Double? {
        return binding.editText.text.toString().toDoubleOrNull()
    }

    // ikinci sayiyi double olarak ya da null olarak dondur
    fun getİkinciSayi(): Double? {
        return binding.editText2.text.toString().toDoubleOrNull()
    }

    // inputlarda bos olup olmadigini kontrol et
    fun checkEmpty(): Boolean {
        // birinci sayiyi al
        val birinciSayi = getBirinciSayi()
        // ikinci sayiyi al
        val ikinciSayi = getİkinciSayi()
        // eger true donerse bos yer var demektir
        if (birinciSayi == null || ikinciSayi == null) {
            binding.textView.text = "Numaraları giriniz!"
            return true
        }
        // bos yoksa false don
        return false
    }

    // hesaplama islemini yap
    fun islem(view: View) {
        // eger bos deger yoksa
        if (!checkEmpty()) {
            // sayilari al
            num1 = getBirinciSayi()
            num2 = getİkinciSayi()

            // butonlarin tag'ina gore islemi gerceklestir
            when (view.tag.toString()) {
                "toplama" -> sonuc = num1!! + num2!!
                "cikarma" -> sonuc = num1!! - num2!!
                "carpma" -> sonuc = num1!! * num2!!
                "bolme" -> sonuc = num1!! / num2!!
            }
            // sonucu yazdir
            binding.textView.text = "Sonuç: ${sonuc.toString()}"
        }
    }
}