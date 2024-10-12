package com.example.mycart.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
    val id : Int = 0,
    val item_name : String = "",
    val item_price : Int = 0,
    val item_description : String="",
    val item_image : String="",
    val item_Active : Boolean = true
):Parcelable
