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
import com.example.mycart.R
import com.example.mycart.databinding.FragmentAddItemBinding
import com.example.mycart.model.Item
import com.example.mycart.model.User
import com.example.mycart.views.fragments.auth.SignUpFragmentDirections
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlin.random.Random

class AddItemFragment : Fragment() , View.OnClickListener {

    private var _binding: FragmentAddItemBinding? = null
    val binding get() = _binding
    val myRef = FirebaseDatabase.getInstance().getReference("Item")
    val randomNum = Random.nextInt(1000, 9999)
    private var auth: FirebaseAuth? = null
    private var pickImage: ActivityResultLauncher<String> ?= null
    var uri: Uri? = null
    var storageRef = FirebaseStorage.getInstance().getReference("Images")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){
           binding?.ivItemImage?.setImageURI(it)
           if (it != null){
               uri = it
           }
       }
//        binding?.addImageBtn?.setOnClickListener{
//            pickImage?.launch("image/*")
//        }

//        setImage()
        registerClicks()
    }

    fun registerClicks() {
        binding?.addImageBtn?.setOnClickListener(this)
        binding?.btnAddItem?.setOnClickListener(this)
    }


//    private fun openGallery() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        pickImageLauncher?.launch(intent)
//    }
//
//    fun setImage() {
//        pickImageLauncher = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
//                val selectedImageUri: Uri? = result.data?.data
//                binding?.ivItemImage?.setImageURI(selectedImageUri)
//                // Set the URI to the ImageView's tag
//                binding?.ivItemImage?.tag = selectedImageUri
//            }
//        }
//    }

    private fun errorNotVisible(): Boolean {
        return binding?.edItemName?.text.toString().isNotEmpty()
                && binding?.edItemDescription?.text.toString().isNotEmpty()
                && binding?.edItemPrice?.text.toString().isNotEmpty()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addImageBtn->{
             //  openGallery()
                pickImage!!.launch("image/*")
            }
            R.id.btnAddItem -> {


                if (uri != null && errorNotVisible()) {
                    var item: Item
                    uri?.let {
                        storageRef.child("$randomNum").putFile(it).addOnSuccessListener { task ->
                            task.metadata?.reference?.downloadUrl
                                ?.addOnSuccessListener { url ->
                                    val imgUrl = url.toString()

                                    item = Item(
                                        id = randomNum,
                                        item_name = binding?.edItemName?.text.toString(),
                                        item_price = binding?.edItemPrice?.text.toString().toInt(),
                                        item_description = binding?.edItemDescription?.text.toString(),
                                        item_image = imgUrl,
                                        item_Active = true
                                    )
                                    myRef.child("$randomNum").setValue(item)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                context,
                                                "Item added successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            findNavController().navigate(
                                                AddItemFragmentDirections.actionAddItemFragmentToAdminDashBoardFragment()
                                            )
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(
                                                context,
                                                "Failed to add Item",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }

                                }
                        }
                    }
                } else {
                    Toast.makeText(context, " Fields Empty ", Toast.LENGTH_SHORT).show()
                }

            }

        }
    }
}
//                val selectedImageUri = binding?.ivItemImage?.tag as? Uri
//                if (selectedImageUri != null && errorNotVisible()){
//                        myRef.child("$randomNum").setValue(
//                            Item(
//                                id = randomNum,
//                                item_name = binding?.edItemName?.text.toString(),
//                                item_price = binding?.edItemPrice?.text.toString().toInt(),
//                                item_description = binding?.edItemDescription?.text.toString(),
//                                item_image = selectedImageUri.toString() ,
//                                item_Active = true
//                            )
//                        ).addOnSuccessListener {
//                            findNavController().navigate(AddItemFragmentDirections.actionAddItemFragmentToAdminDashBoardFragment())
//                            Toast.makeText(context, "Item Added Successfully!", Toast.LENGTH_SHORT).show()
//                        }.addOnFailureListener {
//                            Toast.makeText(context, "Failed to Add Item!", Toast.LENGTH_SHORT).show()
//                        }
//                }
//                else{
//                    Toast.makeText(context, " Fields Empty " , Toast.LENGTH_SHORT).show()
//                }