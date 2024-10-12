package com.example.mycart.views.fragments.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycart.databinding.FragmentAdminOrdersBinding
import com.example.mycart.model.Order
import com.example.mycart.utilis.OrderStatus
import com.example.mycart.utilis.OrderListener
import com.example.mycart.views.adapter.OrderAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class AdminOrdersFragment : Fragment() , View.OnClickListener, OrderListener{

    private var _binding : FragmentAdminOrdersBinding ?= null
    val binding get() = _binding
    private var adapter : OrderAdapter?= null
    val myRef = FirebaseDatabase.getInstance().getReference("Order")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdminOrdersBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvaOrder?.layoutManager = LinearLayoutManager(requireContext())
        adapter = OrderAdapter(requireContext(),this)
        binding?.rvaOrder?.adapter = adapter

        fetchDataFromFirebase()

    }
    private fun fetchDataFromFirebase() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderList = mutableListOf<Order>()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue<Order>()
                    item?.let { orderList.add(it) }
                }
                // Update the adapter with the new list
                adapter?.updateSetItemList(orderList)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load data.", Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onClick(v: View?) {

    }

    override fun onCancelClick(order: Order, position: Int) {
        order.order_status = OrderStatus.CANCEL
        val status = order.order_status
        val orderRef = myRef.child(order.id.toString())
        orderRef.child("order_status").setValue("$status").addOnSuccessListener {
            adapter?.updateOrderAtPosition(position)
            Toast.makeText(context, "Order Cancel Successfully!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to Cancel Order!", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCompleteClick(order: Order, position: Int) {
        order.order_status = OrderStatus.COMPLETE
        val status = order.order_status
        val orderRef = myRef.child(order.id.toString())
        orderRef.child("order_status").setValue("$status").addOnSuccessListener {
            adapter?.updateOrderAtPosition(position)
            Toast.makeText(context, "Order Cancel Successfully!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to Cancel Order!", Toast.LENGTH_SHORT).show()
        }
    }

}