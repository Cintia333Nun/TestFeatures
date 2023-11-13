package com.cin.testfeatures.tabs_recycler.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cin.testfeatures.R
import com.cin.testfeatures.databinding.ActivityTabRecyclerBinding

class TabRecyclerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTabRecyclerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        binding = ActivityTabRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}