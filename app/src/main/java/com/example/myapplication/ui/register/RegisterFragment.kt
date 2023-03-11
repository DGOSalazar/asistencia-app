package com.example.myapplication.ui.register

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.core.extensionFun.toast
import com.example.myapplication.data.models.User
import com.example.myapplication.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private lateinit var mBinding : FragmentRegisterBinding
    private val viewModel : RegisterViewMode by activityViewModels()

    private var isValidEmail= true
    private var isValidPassword= true
    private var imageUri: Uri? = null
    private var photoGallery: ImageView? = null

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            imageUri = data?.data
            photoGallery?.setImageURI(imageUri)
            mBinding.btUpload.setImageURI(imageUri)
            imageUri?.let { viewModel.uploadImage(it) }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        }
        viewModel.urlPhoto.observe(viewLifecycleOwner){
            imageUri = it
            context?.toast(viewModel.urlPhoto.value.toString())
        }
    }

    private fun setListeners() {
        with(mBinding){
            bnRegister.setOnClickListener{
                if (isValidEmail && isValidPassword) {
                    viewModel.register(inputEmail.text.toString(), inputPassword.text.toString())
                }
                if (isValidFields()) {
                    val user = User(
                        email = mBinding.inputEmail.text.toString(),
                        name = mBinding.inputName.text.toString(),
                        lastName1 = mBinding.inputName.text.toString(),
                        lastName2 = mBinding.inputName.text.toString(),
                        position = setPosition(),
                        birthDate = mBinding.inputBirthDate.text.toString(),
                        team = mBinding.inputTeam.text.toString(),
                        profilePhoto = imageUri.toString(),
                        employee = mBinding.inputEmployee.text.toString().toLong(),
                        phone = inputPhone.text.toString(),
                        assistDay = arrayListOf("","","")
                    )
                    viewModel.saveUserData(user)
                }
            }
            bnBack.setOnClickListener{
            }
            btUpload.setOnClickListener {
                openGallery()
            }
        }
    }
    private fun setPosition(): String{
        return when(mBinding.ilPosition.checkedChipId.toString()){
            getString(R.string.idAnalyst) ->{getString(R.string.analyst)}
            getString(R.string.idScrumMaster) -> {getString(R.string.scrummaster)}
            getString(R.string.idQa) ->{getString(R.string.qa)}
            getString(R.string.idAndroid)-> {getString(R.string.android)}
            getString(R.string.idIos) ->{getString(R.string.ios)}
            else ->  {getString(R.string.back)}
        }
    }
    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private fun isValidFields(): Boolean {
        return true
    }
}