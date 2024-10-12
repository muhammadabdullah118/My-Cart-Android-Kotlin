package com.example.mycart.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycart.R
import com.example.mycart.model.Item
import com.example.mycart.utilis.AdminItemListener
import com.squareup.picasso.Picasso


class AdminItemAdapter(
    val context: Context,
    val listener : AdminItemListener
): RecyclerView.Adapter<AdminItemAdapter.ViewHolder>() {

    var itemList =  listOf<Item>()

    class ViewHolder(val view : View): RecyclerView.ViewHolder(view){
        val aimgView = view.findViewById<ImageView>(R.id.rvaImageItem)
        val atvName = view.findViewById<TextView>(R.id.rvaTvName)
        val atvPrice = view.findViewById<TextView>(R.id.rvaTvPrice)
        val btnUpdate = view.findViewById<Button>(R.id.rvabtnUpdate)
        val btnDelete = view.findViewById<Button>(R.id.rvabtnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_item_admin , parent ,  false)
        return  ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        

        Glide.with(context)
            .load(itemList[position].item_image)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.aimgView)
        holder.atvName.text = itemList[position].item_name
        holder.atvPrice.text = itemList[position].item_price.toString()
        holder.btnUpdate.setOnClickListener {
            listener.updateItem(itemList[position])
                }
        holder.btnDelete.setOnClickListener {
            listener.deleteItem(itemList[position])
                }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSetItemList(newItemList: List<Item>) {
        itemList = newItemList.filter { it.item_Active }
        notifyDataSetChanged()
    }

}