package com.ekrembas.yemekkitabi.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.navigation.Navigation
import androidx.room.Room
import com.ekrembas.yemekkitabi.databinding.FragmentTarifBinding
import com.ekrembas.yemekkitabi.model.Tarif
import com.ekrembas.yemekkitabi.roomdb.TarifDAO
import com.ekrembas.yemekkitabi.roomdb.TarifDatabase
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.ByteArrayOutputStream

class TarifFragment : Fragment() {

    private var _binding: FragmentTarifBinding? = null
    private val binding get() = _binding!!

    // izin icin degisken
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var secilenGorsel: Uri? = null // resmin path'i
    private var secilenBitmap: Bitmap? = null // uri'den alinan path'i gorsele cevirme

    // database icin degiskenler
    private lateinit var db: TarifDatabase
    private lateinit var tarifDAO: TarifDAO

    // RxJava degiskeni Threading icin gerekli
    // cok fazla istek oldugunda uygulamada sıkinti olmasin diye
    // bu istekleri kullan at sekline donusturmeye yariyor
    private val mDisposable = CompositeDisposable()

    // silme islemi icin onceki sayfadan gelen tarifin degiskeni
    private var secilenTarif: Tarif? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()
        db = Room.databaseBuilder(
            requireContext(),
            TarifDatabase::class.java, "Tarifler" // Database'e vermek istedigimiz isim
        ).build()

        tarifDAO = db.tarifDAO()
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
                // secilen tarifin id'sini safeArgs yardimiyla al
                val id = TarifFragmentArgs.fromBundle(it).id
                // Threading (RxJava) kullanarak id ile db'den tarifi cek
                mDisposable.add(
                    tarifDAO.findById(id)
                        .subscribeOn(Schedulers.io()) // arka planda islemi yap
                        .observeOn(AndroidSchedulers.mainThread()) // gelen bilgiyi main threade aktar
                        .subscribe(this::handleResponse) // islem bittikten sonra ne olsun
                )
            }
        }
    }

    private fun handleResponse(tarif: Tarif) {
        // byte array olan resmi bitmap'e cevir
        val bitmap = BitmapFactory.decodeByteArray(tarif.gorsel, 0, tarif.gorsel.size)
        binding.imageView.setImageBitmap(bitmap)
        // alinan tarifin ismini tarif fragment'inda goster
        binding.isimText.setText(tarif.isim)
        binding.malzemeText.setText(tarif.malzeme)
        secilenTarif = tarif
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

            val tarif = Tarif(isim, malzeme, byteDizisi)
            // RxJava (threading icin gerekli)
            mDisposable.add(
                tarifDAO.insert(tarif)
                    .subscribeOn(Schedulers.io()) // thread secme islemi (islemi arka planda yap)
                    .observeOn(AndroidSchedulers.mainThread()) // sonucu mainThread'e getir
                    .subscribe(this::handleResponseForInsert) // bu islem sonucunda hangi fonksiyon calisacak
            )
        }
    }

    private fun handleResponseForInsert() {
        // bir onceki ekrana don
        val action = TarifFragmentDirections.actionTarifFragmentToListeFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    fun sil(view: View) {
        if (secilenTarif != null) {
            mDisposable.add(
                tarifDAO.delete(secilenTarif!!)
                    .subscribeOn(Schedulers.io()) // arka planda yap
                    .observeOn(AndroidSchedulers.mainThread()) // on plana getir
                    .subscribe(this::handleResponseForInsert) // islem bittikten sonra yapilacak islem (bir onceki sayfaya don)
            )
        }
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
        mDisposable.clear() // view kapandiginda disposable'i da clear et
    }
}