package com.example.myapplication.ui.register

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private lateinit var mBinding : FragmentRegisterBinding
    private val viewModel : RegisterViewMode by activityViewModels()

    private var isValidEmail= true
    private var isValidPassword= true
    private val SELECT_FILE = 1
    var imageUri: Uri? = null
    var foto_gallery: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentRegisterBinding.inflate(layoutInflater, container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObserver()
    }

    private fun setObserver() {
        viewModel.registerFlag.observe(viewLifecycleOwner){
            findNavController().navigate(R.id.action_registerFragment_to_assistenceMainFragment)
        }
    }

    private fun setListeners() {
        with(mBinding){
            inputEmail.addTextChangedListener(object:TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!text.isNullOrBlank())
                        validateEmail(text as String)
                }

                override fun afterTextChanged(text: Editable?) {}
            })

            inputPassword.addTextChangedListener(object:TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!text.isNullOrBlank())
                        validatePassword(text as String)
                }

                override fun afterTextChanged(text: Editable?) {}
            })

            bnRegister.setOnClickListener{
                if (isValidEmail && isValidPassword)
                    viewModel.register(inputEmail.text.toString(), inputPassword.text.toString())
                if (isValidFields()) {
                    viewModel.saveUserData(
                        name = inputName.text.toString(),
                        birthdate = inputBirthDate.text.toString(),
                        position = ilPosition.checkedChipIds.toString(),
                        email = inputEmail.text.toString(),
                        team = "",
                        phone = inputPhone.text.toString()
                    )
                }
            }
            bnBack.setOnClickListener{
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
            btUpload.setOnClickListener {
                openGallery()

            }
        }
    }
    private fun openGallery(){
        //val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        //startActivityForResult(gallery, SELECT_FILE)
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            imageUri = data?.data
            foto_gallery?.setImageURI(imageUri)
            mBinding.btUpload.setImageURI(imageUri)
            imageUri?.let { viewModel.uploadImage(it) }
        }
    }

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == SELECT_FILE){
            imageUri = data?.data
            foto_gallery?.setImageURI(imageUri)
            mBinding.btUpload.setImageURI(imageUri)
        }
    }
     */

    private fun isValidFields(): Boolean {
        return true
    }
    private fun validatePassword(password:String) {
    }
    private fun validateEmail(email:String) {
    }
}