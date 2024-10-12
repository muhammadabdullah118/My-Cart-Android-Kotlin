package com.example.mycart.views.fragments.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycart.R
import com.example.mycart.databinding.FragmentAdminDashBoardBinding
import com.example.mycart.model.Item
import com.example.mycart.utilis.AdminItemListener
import com.example.mycart.views.adapter.AdminItemAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class AdminDashBoardFragment : Fragment() , View.OnClickListener , AdminItemListener {

    private var _binding : FragmentAdminDashBoardBinding ?= null
    val binding get() = _binding
    private var adapter : AdminItemAdapter ?= null
    val myRef = FirebaseDatabase.getInstance().getReference("Item")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdminDashBoardBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvItem?.layoutManager = LinearLayoutManager(requireContext())
        adapter = AdminItemAdapter(requireContext(), this)
        binding?.rvItem?.adapter = adapter

        fetchDataFromFirebase()
        registerClicks()
    }

    private fun fetchDataFromFirebase() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemList = mutableListOf<Item>()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue<Item>()
                    // Only add items that are active
                    if (item != null && item.item_Active) {
                        itemList.add(item)
                    }
                }
                // Update the adapter with the new list
                adapter?.updateSetItemList(itemList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load data.", Toast.LENGTH_SHORT).show()
            }
        })
    }




    fun registerClicks(){
        binding?.buttonOrders?.setOnClickListener(this)
        binding?.buttonInsert?.setOnClickListener(this)
        binding?.btnSetting?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buttonInsert->{
                findNavController().navigate(AdminDashBoardFragmentDirections.actionAdminDashBoardFragmentToAddItemFragment())
            }
            R.id.buttonOrders->{
                findNavController().navigate(AdminDashBoardFragmentDirections.actionAdminDashBoardFragmentToAdminOrdersFragment())
            }
            R.id.btnSetting->{
                findNavController().navigate(AdminDashBoardFragmentDirections.actionAdminDashBoardFragmentToSignOutFragment())
            }
        }
    }

    override fun updateItem(item: Item) {
        findNavController().navigate(AdminDashBoardFragmentDirections.actionAdminDashBoardFragmentToItemUpdateFragment(item))
    }

    override fun deleteItem(item: Item) {
        val itemRef = myRef.child(item.id.toString())
        itemRef.child("item_Active").setValue(false).addOnSuccessListener {
            Toast.makeText(context, "Item Deleted Successfully!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to Delete Item!", Toast.LENGTH_SHORT).show()
        }
    }

}