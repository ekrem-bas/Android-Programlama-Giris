package com.ekrembas.contextanddialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ekrembas.contextanddialogs.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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

        // context
        // Aktivite context (sadece aktivite icinde erisilebilir), App context (her yerden ulasilabilir)

        // toast message
        Toast.makeText(this, "Uygulama basariyla acildi", Toast.LENGTH_LONG).show()

        // bu sekide de bir butona onClick fonksiyonu tanimlanabilir
        // burada fark su: this dedigimizde eger bu aktiviteye referans edeceksek
        // this@MainActivity kullanmamiz lazim cunku bu lambda gosteriminde this
        // <no name provided> refernas olarak gozukuyor
//        binding.button.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                println("butona tiklandi")
//            }
//        })
    }

    fun buttonDialog(view: View) {
        // alert dialog
        // application context verildiginde hata veriyor
        //val alert = AlertDialog.Builder(applicationContext)
        var alert = AlertDialog.Builder(this@MainActivity)
        alert.setTitle("Alert Başlığı")
        alert.setMessage("Alert Mesajı")
        alert.setPositiveButton("Evet", DialogInterface.OnClickListener { dialog, which ->
            Toast.makeText(this@MainActivity, "Evet tuşuna basıldı", Toast.LENGTH_LONG).show()
        })

        alert.setNegativeButton("Hayır", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                Toast.makeText(this@MainActivity, "Hayır tuşuna basıldı", Toast.LENGTH_LONG).show()
            }

        })
        alert.show()
    }
}