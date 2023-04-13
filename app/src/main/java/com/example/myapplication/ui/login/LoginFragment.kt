package com.example.myapplication.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.myapplication.R
import com.example.myapplication.R.color.*
import com.example.myapplication.core.extensionFun.toast
import com.example.myapplication.core.utils.Status
import com.example.myapplication.data.datasource.Login
import com.example.myapplication.data.datasource.UserRegister
import com.example.myapplication.data.statusNetwork.ResponseStatus
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.core.utils.checkIfIsValidEmail
import com.example.myapplication.core.utils.checkIfIsValidPassword
import com.example.myapplication.core.utils.showAndHideError
import com.example.myapplication.ui.MainActivity
import com.example.myapplication.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var mBinding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initComponent()
        setListeners()
        subscribeLiveData()
    }

    private fun initComponent() {
        with(mBinding) {
            inputEmail.setText(viewModel.getEmail())
            inputPass.setText(viewModel.getPassWord())

            if (inputEmail.text.toString().isNotEmpty() ||
                inputPass.text.toString().isNotEmpty()
            ) activateButton(true)
            else activateButton(false)
        }
    }

    private fun setListeners() {
        with(mBinding) {
            inputEmail.doAfterTextChanged {
                if (it.toString().isNotEmpty()) {
                    mBinding.ilMail.showAndHideError(
                        it.toString().checkIfIsValidEmail(),
                        getString(R.string.error_mail)
                    )
                }
                validateInputs()
            }
            inputPass.doAfterTextChanged {
                if (it.toString().isNotEmpty()) {
                    mBinding.ilPass.showAndHideError(
                        it.toString().checkIfIsValidPassword(),
                        getString(R.string.error_pass)
                    )
                }
                validateInputs()
            }
            bnLogin.setOnClickListener {
                viewModel.getDoLogin(
                    mBinding.inputEmail.text.toString(),
                    mBinding.inputPass.text.toString()
                )
            }

            bnRegister.setOnClickListener {
                it.findNavController().navigate(R.id.action_loginFragment2_to_register_navigation)
            }
        }
    }

    private fun validateInputs() {
        viewModel.validateEmailAndPassword(
            mBinding.inputEmail.text.toString(),
            mBinding.inputPass.text.toString()
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun subscribeLiveData() {
        viewModel.status.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    if (isAdded)
                        (activity as MainActivity).showLoader()
                }
                Status.SUCCESS -> {
                    if (isAdded)
                        (activity as MainActivity).dismissLoader()

                    viewModel.saveLogin(
                        it.data!!.email,
                        mBinding.inputPass.text.toString()
                    )
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                }
               Status.ERROR -> {
                    if (isAdded)
                        (activity as MainActivity).dismissLoader()
                    context?.toast(getString(it.message!!))
                }
            }
        }

        viewModel.activeButton.observe(viewLifecycleOwner) {
            if (it) activateButton(true) else activateButton(false)
        }
    }

    private fun activateButton(status: Boolean) {
        mBinding.bnLogin.isEnabled = status
    }
}