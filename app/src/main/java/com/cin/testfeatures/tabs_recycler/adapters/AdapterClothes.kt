package com.cin.testfeatures.tabs_recycler.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cin.testfeatures.databinding.ItemClotheBinding
import com.cin.testfeatures.tabs_recycler.datasource.ModelClothes
import com.squareup.picasso.Picasso

class AdapterClothes: RecyclerView.Adapter<AdapterClothes.ViewHolderClothes>() {
    private var listClothes = mutableListOf<ModelClothes>()

    inner class ViewHolderClothes (private val binding: ItemClotheBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(item: ModelClothes) {
                Picasso.get().load(item.urlImage).into(binding.imageClothe)
                binding.titleItem.text = item.title
                binding.description.text = item.description
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClothes {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemClotheBinding.inflate(layoutInflater,parent,false)
        return ViewHolderClothes(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderClothes, position: Int) {
        val item = listClothes[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = listClothes.size

    @SuppressLint("NotifyDataSetChanged")
    fun populateList(list: List<ModelClothes>) {
        listClothes.clear()
        listClothes.addAll(list)
        notifyDataSetChanged()
    }
}