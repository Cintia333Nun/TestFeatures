package com.cin.testfeatures.tabs_recycler.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cin.testfeatures.databinding.FragmentErrorBinding

class ErrorFragment : Fragment() {
    private lateinit var binding: FragmentErrorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initBinding(inflater, container)
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentErrorBinding.inflate(inflater,container,false)
        return binding.root
    }

}