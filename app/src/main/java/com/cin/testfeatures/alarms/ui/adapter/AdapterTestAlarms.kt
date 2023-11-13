package com.cin.testfeatures.alarms.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cin.testfeatures.alarms.model.AlarmModel
import com.cin.testfeatures.databinding.ItemAlarmBinding

class AdapterTestAlarms(
    private val context : Context
) : RecyclerView.Adapter<AdapterTestAlarms.ViewHolder>() {
    private val alarms = mutableListOf<AlarmModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(alarms[position])
    }

    override fun getItemCount(): Int {
        return alarms.size
    }

    class ViewHolder(private val binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: AlarmModel) {
            binding.time.text = "${item.hour} : ${item.minute}"
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun populateAlarms(list: List<AlarmModel>) {
        alarms.clear()
        alarms.addAll(list)
        notifyDataSetChanged()
    }
}