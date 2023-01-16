package com.example.myapplication.ui.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var mBinding : FragmentRegisterBinding
    private val viewModel : RegisterViewMode by activityViewModels()

    private var isValidEmail= true
    private var isValidPassword= true

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
                    if (text.isNullOrBlank())
                        validateEmail(text as String)
                }

                override fun afterTextChanged(text: Editable?) {}
            })

            inputPassword.addTextChangedListener(object:TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (text.isNullOrBlank())
                        validatePassword(text as String)
                }

                override fun afterTextChanged(text: Editable?) {}
            })

            bnRegister.setOnClickListener{
                if (isValidEmail && isValidPassword)
                    viewModel.register(inputEmail.text.toString(), inputPassword.text.toString())
            }

            bnBack.setOnClickListener{
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

    private fun validatePassword(password:String) {

    }

    private fun validateEmail(email:String) {

    }


}