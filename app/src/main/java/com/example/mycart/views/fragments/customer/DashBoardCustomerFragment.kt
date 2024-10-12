package com.example.mycart.views.fragments.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycart.R
import com.example.mycart.databinding.FragmentDashBoardCustomerBinding
import com.example.mycart.model.Item
import com.example.mycart.utilis.CustomerItemListener
import com.example.mycart.views.adapter.AdminItemAdapter
import com.example.mycart.views.adapter.ItemAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class DashBoardCustomerFragment : Fragment() , View.OnClickListener ,CustomerItemListener {

    private var _binding : FragmentDashBoardCustomerBinding ?= null
    val binding get() = _binding
    private var adapter : ItemAdapter?= null
    val myRef = FirebaseDatabase.getInstance().getReference("Item")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashBoardCustomerBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.cRvItem?.layoutManager = LinearLayoutManager(requireContext())
        adapter = ItemAdapter(requireContext(),this)
        binding?.cRvItem?.adapter = adapter

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
        binding?.btnSettings?.setOnClickListener(this)
        binding?.buttonOrderHistory?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnSettings->{
                findNavController().navigate(DashBoardCustomerFragmentDirections.actionDashBoardCustomerFragmentToSignOutFragment())
            }
            R.id.buttonOrderHistory->{
                findNavController().navigate(DashBoardCustomerFragmentDirections.actionDashBoardCustomerFragmentToCustomerOrderHistoryFragment())
            }
        }
    }

    override fun onItemClick(item: Item) {
        findNavController().navigate(DashBoardCustomerFragmentDirections.actionDashBoardCustomerFragmentToItemDetailFragment(item))
    }

}