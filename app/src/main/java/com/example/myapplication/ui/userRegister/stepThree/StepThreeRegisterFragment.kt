package com.example.myapplication.ui.userRegister.stepThree

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.core.extensionFun.toast
import com.example.myapplication.core.utils.*
import com.example.myapplication.data.datasource.UserRegister
import com.example.myapplication.databinding.FragmentStepThreeRegisterBinding
import com.example.myapplication.ui.MainActivity
import com.example.myapplication.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StepThreeRegisterFragment : Fragment() {

    private lateinit var mBinding: FragmentStepThreeRegisterBinding
    private val viewModel: StepThreeRegisterViewModel by viewModels()
    private val args: StepThreeRegisterFragmentArgs by navArgs()
    private var model = UserRegister()
    private var imageUriLocal: Uri? = null

    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data != null) {
                    imageUriLocal = it.data?.data
                    viewModel.loadPhoto(imageUriLocal.toString())
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setModel(args.userModel)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentStepThreeRegisterBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setNameTexView("${args.userModel.name} ${args.userModel.lastName}")
        setObservers()
        setListeners()
    }

    @SuppressLint("NewApi")
    private fun setObservers() {

        viewModel.getAllPositions().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    if (isAdded)
                        (activity as MainActivity).showLoader()
                }
                Status.SUCCESS -> {
                    val list = arrayListOf<String>()
                    list.add("Puesto")
                    it.data?.forEach {
                        list.add(it)
                    }
                    mBinding.spinnerPosition.adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        list
                    )
                }
                Status.ERROR -> {
                    if (isAdded)
                        (activity as MainActivity).dismissLoader()
                    context?.toast(it.message!!)
                }
            }
        }

        viewModel.getAllTeams().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    if (isAdded)
                        (activity as MainActivity).showLoader()
                }
                Status.SUCCESS -> {
                    val list = arrayListOf<String>()
                    list.add("Iniciativa")
                    it.data?.forEach {
                        list.add(it)
                    }
                    mBinding.spinnerTeam.adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        list
                    )
                }
                Status.ERROR -> {
                    if (isAdded)
                        (activity as MainActivity).dismissLoader()
                    context?.toast(it.message!!)
                }
            }
        }

        viewModel.statusForDoUser.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    if (isAdded)
                        (activity as MainActivity).showLoader()
                }
                Status.SUCCESS -> {
                    if (it.data!!) {
                        viewModel.upLoadImage(imageUriLocal!!)
                    } else {
                        context?.toast(getString(R.string.user_register_error))
                        if (isAdded)
                            (activity as MainActivity).dismissLoader()
                    }
                }
                Status.ERROR -> {
                    if (isAdded)
                        (activity as MainActivity).dismissLoader()
                    it.message?.let { error ->
                        context?.toast(error)
                    }
                }
            }
        }

        viewModel.statusForDataUser.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    if (isAdded)
                        (activity as MainActivity).showLoader()
                }
                Status.SUCCESS -> {
                    if (isAdded)
                        (activity as MainActivity).dismissLoader()
                    if (it.data!!) {
                        val intent = Intent(requireContext(), HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        context?.toast(getString(R.string.user_register_error))
                        if (isAdded)
                            (activity as MainActivity).dismissLoader()
                    }
                }
                Status.ERROR -> {
                    if (isAdded)
                        (activity as MainActivity).dismissLoader()
                    it.message?.let { error ->
                        context?.toast(error)
                    }

                }
            }
        }

        viewModel.statusForUrl.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    if (isAdded)
                        (activity as MainActivity).showLoader()
                }
                Status.SUCCESS -> {
                    if (it.data != null) {
                        viewModel.saveNewUser(createModel(it.data.toString()))
                    } else {
                        context?.toast(getString(R.string.user_register_error))
                    }
                }
                Status.ERROR -> {
                    if (isAdded)
                        (activity as MainActivity).dismissLoader()
                    it.message?.let {
                        context?.toast(it)
                    }
                }
            }
        }


        viewModel.setModel.observe(viewLifecycleOwner) {
            model = it
        }

        viewModel.setNameUser.observe(viewLifecycleOwner) {
            mBinding.tvNameUser.text = it
        }

        viewModel.photoUri.observe(viewLifecycleOwner) {
            mBinding.photoUri = viewModel
            validInputs(uri = imageUriLocal)
        }

        viewModel.activeButton.observe(viewLifecycleOwner) {
            activateButton(it)
        }

    }

    private fun setListeners() {

        mBinding.btNext.setOnClickListener {
            viewModel.createAccount(createModel())
        }

        mBinding.spinnerPosition.getPosition {
            validInputs(position = it)
        }

        mBinding.spinnerTeam.getPosition {
            validInputs(team = it)
        }

        mBinding.inputNumEmployee.doAfterTextChanged {
            mBinding.ilNumEmployee.showAndHideError(
                it.toString().isValidCollaboratorNumber(), getString(
                    R.string.label_error_employee
                )
            )
            validInputs(employee = it.toString())
        }
        mBinding.ivChargePhoto.setOnClickListener {
            fromGallery()
        }
    }

    private fun validInputs(
        position: Int = mBinding.spinnerPosition.selectedItemPosition,
        team: Int = mBinding.spinnerTeam.selectedItemPosition,
        employee: String = mBinding.inputNumEmployee.text.toString(),
        uri: Uri? = imageUriLocal
    ) {
        viewModel.validateInputs(
            position,
            team,
            employee,
            uri
        )
    }

    private fun activateButton(status: Boolean) {
        mBinding.btNext.isEnabled = status
    }

    private fun fromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        responseLauncher.launch(intent)
    }

    private fun createModel(uri: String = ""): UserRegister {
        return model.apply {
            position = mBinding.spinnerPosition.selectedItem.toString()
            team = mBinding.spinnerTeam.selectedItem.toString()
            profilePhoto = uri
            employeeNumber = mBinding.inputNumEmployee.text.toString().toInt()
        }
    }
}