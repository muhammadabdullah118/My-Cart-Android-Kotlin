package com.example.mycart.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycart.R
import com.example.mycart.model.Item
import com.example.mycart.utilis.CustomerItemListener

class ItemAdapter(
    val context : Context,
    val listener : CustomerItemListener
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>(){

    var itemList = listOf<Item>()
    class ViewHolder(val view : View) : RecyclerView.ViewHolder(view) {
        val imgView = view.findViewById<ImageView>(R.id.rvcImageItem)
        val tvName = view.findViewById<TextView>(R.id.rvcTvName)
        val tvPrice = view.findViewById<TextView>(R.id.rvcTvPrice)
        val cvItem = view.findViewById<CardView>(R.id.cvItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view  = LayoutInflater.from(context).inflate(R.layout.rv_item_customer, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
      return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context)
            .load(itemList[position].item_image)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.imgView)
            holder.tvName.text = itemList[position].item_name
            holder.tvPrice.text = itemList[position].item_price.toString()
            holder.cvItem.setOnClickListener{
                listener.onItemClick(itemList[position])
            }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSetItemList(newItemList: List<Item>) {
        itemList = newItemList.filter { it.item_Active }
        notifyDataSetChanged()
    }
}