package com.example.myapplication.ui.userRegister.stepTwo

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.core.utils.*
import com.example.myapplication.data.models.UserRegister
import com.example.myapplication.databinding.FragmentStepTwoRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class StepTwoRegisterFragment : Fragment() {
    private lateinit var mBinding: FragmentStepTwoRegisterBinding
    private val args: StepTwoRegisterFragmentArgs by navArgs()
    private var model = UserRegister()
    private val viewModel: StepTwoRegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setModel(args.userModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentStepTwoRegisterBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        setListeners()
        registerLiveData()
    }

    private fun initComponent() {
        mBinding.inputBirth.inputType = InputType.TYPE_NULL
        mBinding.inputBirth.keyListener = null;
    }

    private fun registerLiveData() {
        viewModel.setModel.observe(viewLifecycleOwner) {
            model = it
        }
        viewModel.activeButton.observe(viewLifecycleOwner) {
            activateButton(it)
        }
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setListeners() {

        mBinding.inputName.doAfterTextChanged {
            mBinding.ilName.showAndHideError(
                it.toString().isValidText(),
                getString(R.string.label_error_text)
            )
            validateInput()
        }
        mBinding.inputLastName.doAfterTextChanged {
            mBinding.ilLastName.showAndHideError(
                it.toString().isValidText(),
                getString(R.string.label_error_text)
            )
            validateInput()
        }
        mBinding.inputPhone.doAfterTextChanged {
            mBinding.ilPhone.showAndHideError(
                it.toString().isValidPhone(),
                getString(R.string.label_error_phone_number)
            )
            validateInput()
        }

        mBinding.inputBirth.setOnFocusChangeListener { v, hasFocus ->
            if (mBinding.inputBirth.isFocused || hasFocus) {
                v.hideBoard()
                this.createPiker(
                    listener = {
                        mBinding.inputBirth.setText(it)
                        validateInput()
                        mBinding.ilBirth.clearInputFocus()
                    },
                    negative = {
                        mBinding.ilBirth.clearInputFocus()
                    }
                )
            }
        }
        mBinding.btNext.setOnClickListener {
            val action =
                StepTwoRegisterFragmentDirections.actionTwoRegisterFragmentToStepThreeRegisterFragment(
                    createModel()
                )

            findNavController().navigate(action)
        }
    }

    private fun createModel(): UserRegister {
        return model.apply {
            name = mBinding.inputName.text.toString()
            lastName = mBinding.inputLastName.text.toString()
            birthDate = mBinding.inputBirth.text.toString()
            phone = mBinding.inputPhone.text.toString()
        }
    }

    private fun validateInput() {
        viewModel.validateInputs(
            mBinding.inputName.text.toString(),
            mBinding.inputLastName.text.toString(),
            mBinding.inputBirth.text.toString(),
            mBinding.inputPhone.text.toString()
        )
    }

    private fun activateButton(status: Boolean) {
        mBinding.btNext.isEnabled = status
    }
}