package com.example.mycart.views.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mycart.R
import com.example.mycart.databinding.FragmentSignInBinding
import com.example.mycart.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class SignInFragment : Fragment() , View.OnClickListener {

    private var _binding : FragmentSignInBinding ?= null
    val binding get() = _binding
    private var auth : FirebaseAuth?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        initialize()
        registerClicks()
    }

    fun initialize(){

    }

    fun registerClicks(){
        binding?.siCbAdmin?.setOnClickListener(this)
        binding?.siCbCustomer?.setOnClickListener(this)
        binding?.tvAccount?.setOnClickListener(this)
        binding?.buttonSignIn?.setOnClickListener(this)
    }

    fun checkEmpty(): Boolean {
        return binding?.etEmail?.text.toString().isNotEmpty()
                && binding?.etPassword?.text.toString().isNotEmpty()
                && binding?.siTvUserStatus?.text.toString().isNotEmpty()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.siCbCustomer -> {
                binding?.siCbAdmin?.isChecked = false
                binding?.siTvUserStatus?.text = "customer"
            }

            R.id.siCbAdmin -> {
                binding?.siCbCustomer?.isChecked = false
                binding?.siTvUserStatus?.text = "admin"
            }

            R.id.tvAccount -> {
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
            }

            R.id.buttonSignIn -> {
                if (checkEmpty()) {
                    auth?.signInWithEmailAndPassword(
                        binding?.etEmail?.text.toString(), binding?.etPassword?.text.toString()
                    )?.addOnSuccessListener {

                        val userId = auth?.currentUser?.uid
                        val database = FirebaseDatabase.getInstance().getReference("User")
                        val selectedStatus = binding?.siTvUserStatus?.text.toString()


                        userId?.let { id ->
                            database.child(id).get().addOnSuccessListener { snapshot ->
                                val user = snapshot.getValue(User::class.java)

                                val u = user?.userStatus

                                if (u == selectedStatus) {
                                    when (u) {
                                        "admin" -> {
                                            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToAdminDashBoardFragment())
                                            Toast.makeText(context, "Login Successfully as Admin", Toast.LENGTH_SHORT).show()
                                        }
                                        "customer" -> {
                                            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToDashBoardCustomerFragment())
                                            Toast.makeText(context, "Login Successful as Customer", Toast.LENGTH_SHORT).show()
                                        }
                                        else -> {
                                            Toast.makeText(context, "Status Mismatch ", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    // Status mismatch
                                    Toast.makeText(context, "User status mismatch", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }?.addOnFailureListener {
                        Toast.makeText(context, " Login Failed ", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, " Empty Fields ", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


