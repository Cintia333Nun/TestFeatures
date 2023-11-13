package com.cin.testfeatures.tabs_recycler.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cin.testfeatures.databinding.FragmentClotheBinding
import com.cin.testfeatures.tabs_recycler.adapters.AdapterClothes
import com.cin.testfeatures.tabs_recycler.datasource.DataSourceClothes
import com.cin.testfeatures.tabs_recycler.datasource.TypeClothes

class ClotheFragment(private val type: TypeClothes) : Fragment() {
    private lateinit var binding: FragmentClotheBinding
    private val adapter = AdapterClothes()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initBinding(inflater, container)
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentClotheBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        binding.recyclerClothes.adapter = adapter
        adapter.populateList(DataSourceClothes(type).getDataClothes())
    }
}