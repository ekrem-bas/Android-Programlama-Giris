package com.ekrembas.yemekkitabi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.ekrembas.yemekkitabi.adapter.TarifAdapter
import com.ekrembas.yemekkitabi.databinding.FragmentListeBinding
import com.ekrembas.yemekkitabi.model.Tarif
import com.ekrembas.yemekkitabi.roomdb.TarifDAO
import com.ekrembas.yemekkitabi.roomdb.TarifDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ListeFragment : Fragment() {

    private var _binding: FragmentListeBinding? = null
    private val binding get() = _binding!!

    // database icin degiskenler
    private lateinit var db: TarifDatabase
    private lateinit var tarifDAO: TarifDAO

    // threading icin gerekli olan degisken
    private val mDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        _binding = FragmentListeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // yeni tarif ekleme fonksiyonunu butona atama
        binding.floatingActionButton.setOnClickListener {
            yeniEkle(it)
        }
        // layout manager
        binding.traifRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        // verileri al
        verileriAl()
    }

    private fun verileriAl() {
        mDisposable.add(
            tarifDAO.getAll()
                .subscribeOn(Schedulers.io()) // arka planda islemi yap
                .observeOn(AndroidSchedulers.mainThread()) // islem tamamlaninca mainThread'e gonder
                .subscribe(this::handleResponse) // tum her sey tamamlaninca ne yapilacak
        )
    }

    private fun handleResponse(tarifler: List<Tarif>) {
        // adapter'i initial et
        val adapter = TarifAdapter(tarifler)
        binding.traifRecyclerView.adapter = adapter
    }

    // yeni tarif ekleme fonksiyonu
    fun yeniEkle(view: View) {
        val action = ListeFragmentDirections.actionListeFragmentToTarifFragment("yeni", 0)
        Navigation.findNavController(view).navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }
}