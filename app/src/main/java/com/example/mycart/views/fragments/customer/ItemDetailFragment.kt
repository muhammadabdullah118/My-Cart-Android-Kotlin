package com.example.mycart.views.fragments.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mycart.R
import com.example.mycart.databinding.FragmentItemDetailBinding

class ItemDetailFragment : Fragment(), View.OnClickListener {

    private var _binding : FragmentItemDetailBinding ?= null
    val binding get() = _binding
    private val args : ItemDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       _binding = FragmentItemDetailBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()
        registerClicks()
    }

    fun initialize(){
        Glide.with(this)
            .load(args.item.item_image)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(binding!!.imgIcon)
        binding?.getName?.text = args.item.item_name
        binding?.getPrice?.text = args.item.item_price.toString()
        binding?.getDescription?.text = args.item.item_description
    }

    fun registerClicks(){
        binding?.buttonPlaceOrder?.setOnClickListener(this)
        binding?.buttonBack?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buttonBack->{
                findNavController().navigateUp()
            }
            R.id.buttonPlaceOrder->{
                findNavController().navigate(ItemDetailFragmentDirections.actionItemDetailFragmentToPlaceOrderFragment(args.item))
            }
        }
    }
}