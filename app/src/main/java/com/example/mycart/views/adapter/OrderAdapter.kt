package com.example.mycart.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycart.R
import com.example.mycart.model.Order
import com.example.mycart.model.User
import com.example.mycart.utilis.OrderStatus
import com.example.mycart.utilis.OrderListener

class OrderAdapter(
    val context: Context,
    val listener : OrderListener
): RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    
    var orderList = listOf<Order>()
    val userList = listOf<User>()

    class ViewHolder(val view : View):RecyclerView.ViewHolder(view){
        val orderName = view.findViewById<TextView>(R.id.rvOrderName)
        val orderPrice = view.findViewById<TextView>(R.id.rvOrderPrice)
        val orderQuantity = view.findViewById<TextView>(R.id.rvOrderQuantity)
        val btnCancel = view.findViewById<Button>(R.id.rvabtnCancel)
        val btnComplete = view.findViewById<Button>(R.id.rvabtnComplete)
        val cv = view.findViewById<CardView>(R.id.cvOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_order_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderAdapter.ViewHolder, position: Int) {
       // holder.cv.setCardBackgroundColor(Color.GREEN)
        holder.orderName.text = orderList[position].order_name
        holder.orderPrice.text = orderList[position].order_price.toString()
        holder.orderQuantity.text = orderList[position].order_quantity.toString()

            // Update CardView color and button visibility based on order status
            val order = orderList[position]
            when (order.order_status) {
                OrderStatus.CANCEL -> {
                    holder.cv.setCardBackgroundColor(Color.RED)
                    holder.btnCancel.visibility = View.GONE
                    holder.btnComplete.visibility = View.GONE
                }
                OrderStatus.COMPLETE -> {
                    holder.cv.setCardBackgroundColor(Color.GREEN)

                    holder.btnCancel.visibility = View.GONE
                    holder.btnComplete.visibility = View.GONE
                }
                else -> {
                    holder.cv.setCardBackgroundColor(Color.WHITE)
                    holder.btnCancel.visibility = View.VISIBLE
                    holder.btnComplete.visibility = View.VISIBLE
                }
            }

        if(userList[position].userStatus == "Customer" ){
            holder.btnComplete.visibility = View.GONE
            holder.btnCancel.setOnClickListener{
                listener.onCancelClick(order, position)
            }
        }else{
            holder.btnComplete.setOnClickListener{
                listener.onCompleteClick(order, position)
            }
            holder.btnCancel.setOnClickListener{
                listener.onCancelClick(order, position)
            }
        }




    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSetItemList(newItemList: List<Order>) {
        orderList = newItemList
        notifyDataSetChanged()
    }

    fun updateOrderAtPosition(position: Int) {
        notifyItemChanged(position)
    }


}