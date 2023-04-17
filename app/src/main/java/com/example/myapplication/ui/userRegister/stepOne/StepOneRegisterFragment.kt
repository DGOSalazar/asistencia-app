package com.example.myapplication.ui.userRegister.stepOne


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.data.datasource.UserRegister
import com.example.myapplication.databinding.FragmentStepOneRegisterBinding
import com.example.myapplication.core.utils.checkIfIsValidEmail
import com.example.myapplication.core.utils.checkIfIsValidPassword
import com.example.myapplication.core.utils.showAndHideError
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StepOneRegisterFragment : Fragment(R.layout.fragment_step_one_register) {
    private lateinit var mBinding: FragmentStepOneRegisterBinding
    private val viewModel: StepOneViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentStepOneRegisterBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        registerLiveData()
    }

    private fun registerLiveData() {
        viewModel.activeButton.observe(viewLifecycleOwner) {
            activateButton(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setListeners() {
        mBinding.inputEmail.doAfterTextChanged {
            mBinding.ilMail.showAndHideError(
                it.toString().checkIfIsValidEmail(),
                getString(R.string.error_mail)
            )
            viewModel.validateEmailAndPassword(
                mBinding.inputEmail.text.toString(),
                mBinding.inputPass1.text.toString(),
                mBinding.inputPass2.text.toString()
            )
        }


        mBinding.inputPass1.doAfterTextChanged {
            mBinding.ilPass1.showAndHideError(
                it.toString().checkIfIsValidPassword(),
                getString(R.string.error_pass)
            )
            validatePasswords()
            viewModel.validateEmailAndPassword(
                mBinding.inputEmail.text.toString(),
                mBinding.inputPass1.text.toString(),
                mBinding.inputPass2.text.toString()
            )
        }
        mBinding.inputPass2.doAfterTextChanged {
            validatePasswords()
            viewModel.validateEmailAndPassword(
                mBinding.inputEmail.text.toString(),
                mBinding.inputPass1.text.toString(),
                mBinding.inputPass2.text.toString()
            )
        }
        mBinding.btNext.setOnClickListener {

            val action =
                StepOneRegisterFragmentDirections.actionStepOneRegisterFragment2ToStepTwoRegisterFragment2(
                    createModel()
                )

            findNavController().navigate(action)
        }
    }

    private fun createModel(): UserRegister =
        UserRegister(
            email = mBinding.inputEmail.text.toString(),
            password = mBinding.inputPass1.text.toString()
        )

    private fun activateButton(status: Boolean) {
        mBinding.btNext.isEnabled = status
    }

    private fun validatePasswords() {
        if (mBinding.inputPass1.text.toString() == mBinding.inputPass2.text.toString()) {
            mBinding.ilPass1.showAndHideError(true)
            mBinding.ilPass2.showAndHideError(true)
        } else {
            mBinding.ilPass2.showAndHideError(
                false,
                getString(R.string.error_confirmation_pass)
            )
        }
    }
}