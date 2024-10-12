package com.example.mycart.model

import android.os.Parcelable
import com.example.mycart.utilis.OrderStatus
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    val id : Int =0,
    val order_name : String="",
    val order_price : Int=0,
    val order_quantity : Int=0,
    var order_status : OrderStatus = OrderStatus.PENDING
):Parcelable
