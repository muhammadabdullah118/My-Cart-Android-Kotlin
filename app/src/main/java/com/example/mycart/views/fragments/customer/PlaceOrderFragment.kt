package com.example.mycart.views.fragments.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mycart.R
import com.example.mycart.databinding.FragmentPlaceOrderBinding
import com.example.mycart.model.Order
import com.example.mycart.utilis.OrderStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

class PlaceOrderFragment : Fragment() , View.OnClickListener {

    private var _binding : FragmentPlaceOrderBinding ?= null
    val binding get() =  _binding
    var quantity : Int = 1
    private val args : PlaceOrderFragmentArgs by navArgs()
    val myRef = FirebaseDatabase.getInstance().getReference("Order")
    val randomNum = Random.nextInt(1000,9999)
    private var auth : FirebaseAuth?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPlaceOrderBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth=FirebaseAuth.getInstance()

        registerClicks()
        initialize()

    }

    private fun initialize() {
        binding?.getName?.text = args.item.item_name
        binding?.getPrice?.text = args.item.item_price.toString()
        binding?.getQuantity?.text=quantity.toString()
    }

    private fun registerClicks() {
        binding?.buttonBackTwo?.setOnClickListener(this)
        binding?.buttonMinus?.setOnClickListener(this)
        binding?.buttonPlus?.setOnClickListener(this)
        binding?.buttonConfirmOrder?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        var price = args.item.item_price

        when(v?.id){
        R.id.buttonBackTwo->{
            findNavController().navigateUp()
        }
        R.id.buttonMinus->{
            if(quantity > 1 ){
                quantity = quantity-1
                price =price*quantity
                binding?.getQuantity?.text=quantity.toString()
                binding?.getPrice?.text=price.toString()
            }
        }
        R.id.buttonPlus->{
            if(quantity < 3) {
                quantity = quantity + 1
                price = price * quantity
                binding?.getQuantity?.text = quantity.toString()
                binding?.getPrice?.text = price.toString()
            }
        }
        R.id.buttonConfirmOrder->{
            myRef.child("$randomNum").setValue(
                Order(
                    id = randomNum,
                    order_name = binding?.getName?.text.toString(),
                    order_quantity = binding?.getQuantity?.text.toString().toInt(),
                    order_price = binding?.getPrice?.text.toString().toInt(),
                    order_status = OrderStatus.PENDING
                )
            ).addOnSuccessListener {
                Toast.makeText(context, " Successfully added", Toast.LENGTH_SHORT).show()
                findNavController().navigate(PlaceOrderFragmentDirections.actionPlaceOrderFragmentToDashBoardCustomerFragment())
            }.addOnFailureListener {
                Toast.makeText(context, " Failed ", Toast.LENGTH_SHORT).show()
            }
        }
    }
    }
}

