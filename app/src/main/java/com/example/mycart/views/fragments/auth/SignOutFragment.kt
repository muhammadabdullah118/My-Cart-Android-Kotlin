package com.example.mycart.views.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mycart.R
import com.example.mycart.databinding.FragmentSignOutBinding

class SignOutFragment : Fragment(),  View.OnClickListener {

    private var _binding : FragmentSignOutBinding ?= null
    val binding get() =  _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignOutBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerClicks()
    }

    fun registerClicks(){
        binding?.btnLogOut?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
     when(v?.id){
         R.id.btnLogOut->{
             findNavController().navigate(SignOutFragmentDirections.actionSignOutFragmentToSignInFragment())
         }
     }
    }
}