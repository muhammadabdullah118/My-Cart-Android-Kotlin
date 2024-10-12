package com.example.mycart.utilis

import com.example.mycart.model.Item

interface AdminItemListener {

    fun updateItem(item: Item)

    fun deleteItem(item: Item)
}