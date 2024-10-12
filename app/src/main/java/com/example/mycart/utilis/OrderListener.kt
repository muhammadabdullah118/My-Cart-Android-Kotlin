package com.example.mycart.utilis

import com.example.mycart.model.Order
import java.text.FieldPosition

interface OrderListener {

    fun onCancelClick(order: Order , position: Int)

    fun onCompleteClick(order: Order , position: Int)
}