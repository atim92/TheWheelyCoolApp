package com.tatam.thewheelycoolapp.ui.additems

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tatam.thewheelycoolapp.R
import com.tatam.thewheelycoolapp.data.model.WheelItem
import com.tatam.thewheelycoolapp.databinding.ItemForWheelBinding

class WheelItemAdapter(
    private val deleteItem: (WheelItem) -> Unit,
    private val updateItem: (WheelItem) -> Unit,
) : RecyclerView.Adapter<WheelItemAdapter.WheelItemViewHolder>() {

    private var wheelItemList = emptyList<WheelItem>()

    inner class WheelItemViewHolder(val binding: ItemForWheelBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WheelItemViewHolder {
        val binding = ItemForWheelBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WheelItemViewHolder(binding)
    }

    //Using viewbinding here
    override fun onBindViewHolder(holder: WheelItemViewHolder, position: Int) {
        with(holder) {
            with(wheelItemList[position]) {
                binding.tvItemName.text = this.name
                binding.llItem.setOnClickListener { updateItem(this) }
                binding.btnDeleteItem.setOnClickListener { deleteItem(this) }
            }
        }
    }

    override fun getItemCount(): Int {
        return wheelItemList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(itemList: List<WheelItem>) {
        wheelItemList = itemList
        notifyDataSetChanged()
    }

}