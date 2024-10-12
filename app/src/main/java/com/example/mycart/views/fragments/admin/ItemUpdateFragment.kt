package com.example.mycart.views.fragments.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mycart.R
import com.example.mycart.databinding.FragmentItemUpdateBinding
import com.example.mycart.model.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

class ItemUpdateFragment : Fragment() , View.OnClickListener{

    private var _binding : FragmentItemUpdateBinding ?= null
    val binding get() =  _binding
    private val arg : ItemUpdateFragmentArgs by navArgs()
    val myRef = FirebaseDatabase.getInstance().getReference("Item")
    private var auth : FirebaseAuth ?= null
    private var pickImageLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentItemUpdateBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        setImage()
        initialize()
        registerClicks()
    }

    fun initialize(){
        binding?.etItemName?.setText(arg.item.item_name)
        binding?.etItemPrice?.setText(arg.item.item_price.toString())
        binding?.etItemDescription?.setText(arg.item.item_description)
    }

    fun registerClicks(){
        binding?.buttonUpdateOrder?.setOnClickListener(this)
        binding?.uAddImageBtn?.setOnClickListener(this)
        binding?.buttonBackthree?.setOnClickListener(this)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher?.launch(intent)
    }

    fun setImage(){
        pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val selectedImageUri: Uri? = result.data?.data
                binding?.ivuItemImage?.setImageURI(selectedImageUri)
                // Set the URI to the ImageView's tag
                binding?.ivuItemImage?.tag = selectedImageUri
            }
        }
    }

    private fun errorNotVisible(): Boolean{
        return binding?.etItemName?.text.toString().isNotEmpty()
                && binding?.etItemDescription?.text.toString().isNotEmpty()
                && binding?.etItemPrice?.text.toString().isNotEmpty()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.uAddImageBtn->{
                openGallery()
            }
            R.id.buttonBackthree->{
                findNavController().navigateUp()
            }
            R.id.buttonUpdateOrder->{
                val selectedImageUri = binding?.ivuItemImage?.tag as? Uri
                if (selectedImageUri != null && errorNotVisible()){
                    myRef.child("${arg.item.id}").setValue(
                        Item(
                            id = arg.item.id,
                            item_name = binding?.etItemName?.text.toString(),
                            item_price = binding?.etItemPrice?.text.toString().toInt(),
                            item_description = binding?.etItemDescription?.text.toString(),
                            item_image = selectedImageUri.toString()
                        )
                    ).addOnSuccessListener {
                        findNavController().navigate(ItemUpdateFragmentDirections.actionItemUpdateFragmentToAdminDashBoardFragment())
                        Toast.makeText(context, "Item Updated Successfully!", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed to Update Item!", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(context, " Fields Empty " , Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}