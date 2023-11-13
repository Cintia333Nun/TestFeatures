package com.cin.testfeatures.retrofit_mvvm.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.cin.testfeatures.databinding.ActivityRestServicesBinding
import com.cin.testfeatures.retrofit_mvvm.repository.BooksRepository
import com.cin.testfeatures.retrofit_mvvm.service.NetworkState
import com.cin.testfeatures.retrofit_mvvm.service.ServiceApi
import com.cin.testfeatures.retrofit_mvvm.viewmodel.BooksViewModel
import com.cin.testfeatures.tabs_recycler.adapters.AdapterClothes
import com.cin.testfeatures.tabs_recycler.datasource.ModelClothes

class RestServicesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestServicesBinding
    private val viewModel by lazy {
        getViewModel {
            BooksViewModel(BooksRepository(ServiceApi().getBooksApi()))
        }
    }
    private val adapter = AdapterClothes() //Re-use

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        binding = ActivityRestServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycler()
        initObservers()
        initListeners()
    }

    private fun initRecycler() {
        binding.recyclerBooks.adapter = adapter
    }

    private fun initObservers() {
        viewModel.networkState.observe(this) { state ->
            state?.let {
                when (it) {
                    NetworkState.ERROR -> {
                        Toast.makeText(this, "Server Error", Toast.LENGTH_LONG).show()
                        binding.progressCircular.visibility = View.GONE
                    }
                    NetworkState.LOADING -> {
                        binding.progressCircular.visibility = View.VISIBLE
                        binding.progressCircular.animate()
                    }
                    NetworkState.LOADED -> binding.progressCircular.visibility = View.GONE
                }
            }
        }

        viewModel.responseVolume.observe(this) { volume ->
            val mapList = volume.listVolumes.map {
                ModelClothes(
                    it.volumeInfo.title?:"Titulo",
                    it.volumeInfo.imageLinks.smallThumbnail?:"https://images-na.ssl-images-amazon.com/images/I/516fO0wjqnL.jpg",
                    it.volumeInfo.description?:"Descripcion ejemplo"
                )
            }
            adapter.populateList(mapList)
        }
    }

    private fun initListeners() {
        binding.buttonSearchBooks.setOnClickListener {
            val key = binding.keyWord.text.toString()
            val author = binding.author.text.toString()

            if (key != "") {
                if (author != "") {
                    viewModel.getBooks(key,author)
                } else Toast.makeText(this, "Agregue autor",Toast.LENGTH_LONG).show()
            } else Toast.makeText(this, "Agregue palabra clave",Toast.LENGTH_LONG).show()
        }
    }
}

inline fun <reified T : ViewModel> ViewModelStoreOwner.getViewModel(crossinline factory: () -> T): T {
    val newFactory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }
    return ViewModelProvider(this, newFactory).get(T::class.java)
}