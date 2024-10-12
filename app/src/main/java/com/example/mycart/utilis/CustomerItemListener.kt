package com.example.mycart.utilis

import com.example.mycart.model.Item

interface CustomerItemListener {
    fun onItemClick(item: Item)
}