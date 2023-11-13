package com.cin.testfeatures.clean_mvvm_dagger.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.cin.testfeatures.clean_mvvm_dagger.ui.viewmodel.QuoteViewModel
import com.cin.testfeatures.databinding.ActivityDaggerTestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DaggerTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaggerTestBinding
    private val quoteViewModel: QuoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initViewModel()
        initObservers()
    }

    private fun initViewModel() {
        quoteViewModel.onCreate()
        binding.viewContainer.setOnClickListener { quoteViewModel.randomQuote() }
    }

    private fun initObservers() {
        quoteViewModel.quoteModel.observe(this, Observer {
            binding.tvQuote.text = it.quote
            binding.tvAuthor.text = it.author
        })
        quoteViewModel.isLoading.observe(this, Observer {
            binding.loading.isVisible = it
        })
    }

    private fun initBinding() {
        binding = ActivityDaggerTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}