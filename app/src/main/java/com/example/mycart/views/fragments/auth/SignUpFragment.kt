package com.example.mycart.views.fragments.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mycart.R
import com.example.mycart.databinding.FragmentSignUpBinding
import com.example.mycart.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern
import kotlin.random.Random

class SignUpFragment : Fragment() , View.OnClickListener {

    private var _binding : FragmentSignUpBinding ?= null
    val binding get() = _binding
    private var auth : FirebaseAuth?=null
    val myRef = FirebaseDatabase.getInstance().getReference("User")
    val randomNum = Random.nextInt(1000,9999)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater,container,false)
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
        binding?.cbAdmin?.setOnClickListener(this)
        binding?.cbCustomer?.setOnClickListener(this)
        binding?.buttonSignUp?.setOnClickListener(this)
    }

    private fun isValidEmail(email : String): Boolean {
        val pattern : Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun  isPasswordValid(pass : String , confirmPass : String ):  Boolean{
        return (binding?.edPassword?.text.toString().length >= 8) &&
                (binding?.edPassword?.text.toString() ==  binding?.edConfirmPassword?.text.toString())
    }

    private fun checkInput(): Boolean{
        return  binding?.edFirstName?.text.toString().isNotEmpty()
                && binding?.edLastName?.text.toString().isNotEmpty()
                && isValidEmail( binding?.edEmail?.text.toString())
                && binding?.tvUserStatus?.text.toString().isNotEmpty()
                && isPasswordValid( binding?.edPassword?.text.toString(),binding?.edConfirmPassword?.text.toString())
    }

    override fun onClick(v: View?) {
       when(v?.id){
        R.id.cbCustomer-> {
            binding?.cbAdmin?.isChecked = false
            binding?.tvUserStatus?.text = "customer"
        }
        R.id.cbAdmin-> {
            binding?.cbCustomer?.isChecked = false
            binding?.tvUserStatus?.text = "admin"
        }
        R.id.buttonSignUp->{
            if (checkInput()){
                auth?.createUserWithEmailAndPassword(
                    binding?.edEmail?.text.toString(),binding?.edPassword?.text.toString()
                )?.addOnFailureListener {
                    Toast.makeText(context, "SignUP failed !", Toast.LENGTH_SHORT).show()
                }?.addOnSuccessListener {

                    val userId = auth?.currentUser?.uid // Get Firebase UID
                    val user = User(
                        id = randomNum, // You can still use a random number for another ID if needed
                        fname = binding?.edFirstName?.text.toString(),
                        lname = binding?.edLastName?.text.toString(),
                        email = binding?.edEmail?.text.toString(),
                        password = binding?.edPassword?.text.toString(),
                        userStatus = binding?.tvUserStatus?.text.toString()
                    )
                    userId?.let {
                        myRef.child(it).setValue(user).addOnSuccessListener {
                            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
                            Toast.makeText(context, "SignUp Success!", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(context, "SignUp Failed in DB!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            else{
                Toast.makeText(context,"Empty Fields / Wrong Input", Toast.LENGTH_SHORT).show()
            }
        }
       }
    }
}