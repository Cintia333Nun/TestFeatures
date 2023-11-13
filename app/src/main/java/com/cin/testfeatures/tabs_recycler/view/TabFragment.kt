package com.cin.testfeatures.tabs_recycler.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cin.testfeatures.R
import com.cin.testfeatures.databinding.FragmentTabBinding
import com.cin.testfeatures.tabs_recycler.adapters.AdapterTabClothing

class TabFragment : Fragment() {
    private lateinit var binding: FragmentTabBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return initBinding(inflater, container)
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentTabBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTab()
    }

    private fun initTab() {
        val adapter = AdapterTabClothing(childFragmentManager)
        binding.ordersContent.adapter = adapter
        binding.orderTabs.setupWithViewPager(binding.ordersContent)
    }
}