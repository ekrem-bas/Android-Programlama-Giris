package com.ekrembas.yemekkitabi.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ekrembas.yemekkitabi.databinding.FragmentTarifBinding
import com.ekrembas.yemekkitabi.model.Tarif
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream

class TarifFragment : Fragment() {

    private var _binding: FragmentTarifBinding? = null
    private val binding get() = _binding!!

    // izin icin degisken
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var secilenGorsel: Uri? = null // resmin path'i
    private var secilenBitmap: Bitmap? = null // uri'den alinan path'i gorsele cevirme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()
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
        // tarif icin gerekli olan degiskenler
        val isim = binding.isimText.text.toString()
        val malzeme = binding.malzemeText.text.toString()
        // SQLite kullandigimizdan buyuk gorselleri kucultmemiz gerekir
        if (secilenBitmap != null) {
            // secilen gorselin bitmap'ini kucuk bitmap haline getir
            val kucukBitmap = kucukBitmapOlustur(secilenBitmap!!, 300)
            // bu bitmap'i kucultmek ve kullanabilmek icin sıkıstır
            val outputStream = ByteArrayOutputStream()
            kucukBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
            // sıkıstırılan bitmapi byte dizisi haline getir
            // modelde byte dizisi olarak tanimli cunku
            val byteDizisi = outputStream.toByteArray()

            // tarifimizi olusturalim
            val tarif = Tarif(isim, malzeme, byteDizisi)
        }
    }

    fun sil(view: View) {

    }

    fun gorselSec(view: View) {
        // izin verilmemisse izni istememiz lazim
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                ) {
                    // snackbar gosterilmesi lazim, kullanicidan neden izin istedigimizi bir kez soylememiz lazim
                    Snackbar.make(
                        view,
                        "Galeriye erişip görsel seçmeniz gerekmektedir!",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("İzin Ver", View.OnClickListener {
                        // izin isteyecegiz
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }).show()
                } else {
                    // izin isteyecegiz
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            } else {
                // izin onceden verilmis, galeriye gidilebilir
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    // snackbar gosterilmesi lazim, kullanicidan neden izin istedigimizi bir kez soylememiz lazim
                    Snackbar.make(
                        view,
                        "Galeriye erişip görsel seçmeniz gerekmektedir!",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("İzin Ver", View.OnClickListener {
                        // izin isteyecegiz
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }).show()
                } else {
                    // izin isteyecegiz
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            } else {
                // izin onceden verilmis, galeriye gidilebilir
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        }
    }

    private fun registerLauncher() {

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    // kullanici gorsel secti
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        secilenGorsel = intentFromResult.data
                        try {
                            // yeni yontem
                            if (Build.VERSION.SDK_INT >= 28) {
                                val source = ImageDecoder.createSource(
                                    requireActivity().contentResolver,
                                    secilenGorsel!!
                                )
                                secilenBitmap = ImageDecoder.decodeBitmap(source)
                                binding.imageView.setImageBitmap(secilenBitmap)
                            } else {
                                // eski yontem
                                secilenBitmap =
                                    MediaStore.Images.Media.getBitmap(
                                        requireActivity().contentResolver,
                                        secilenGorsel
                                    )
                                binding.imageView.setImageBitmap(secilenBitmap)
                            }
                        } catch (e: Exception) {
                            println(e.localizedMessage)
                        }
                    }
                }

            }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    // izin verildi, galeriye gidilebilir
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                } else {
                    // izin verilmedi
                    Toast.makeText(requireContext(), "İzin Verilmedi!", Toast.LENGTH_LONG).show()
                }
            }
    }

    // gorseli kucultmek icin fonksiyon
    private fun kucukBitmapOlustur(kullanicininBitmapi: Bitmap, maxBoyut: Int): Bitmap {
        // kullanicinin sectigi gorsellerin width ve height degerleri
        var width = kullanicininBitmapi.width
        var height = kullanicininBitmapi.height

        val bitmapOrani: Double = width.toDouble() / height.toDouble()

        if (bitmapOrani > 1) {
            // gorsel yatay
            width = maxBoyut
            val kisaltilmisYukseklik = width / bitmapOrani
            height = kisaltilmisYukseklik.toInt()
        } else {
            // gorsel dikey
            height = maxBoyut
            val kisaltilmisGenislik = height * bitmapOrani
            width = kisaltilmisGenislik.toInt()
        }

        return Bitmap.createScaledBitmap(kullanicininBitmapi, width, height, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}