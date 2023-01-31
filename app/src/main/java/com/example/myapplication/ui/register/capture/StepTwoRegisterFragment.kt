package com.example.myapplication.ui.register.capture

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStepOneRegisterBinding
import com.example.myapplication.databinding.FragmentStepTwoRegisterBinding
import com.example.myapplication.ui.register.Validations
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StepTwoRegisterFragment : Fragment() {

    @Inject
    lateinit var validations:Validations

    private lateinit var mBinding: FragmentStepTwoRegisterBinding
    private lateinit var email:String
    private lateinit var password:String

    private var isValidName = false
    private var isValidLastName = false
    private var isValidBirthDate = true
    private var isValidPhone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = arguments?.getString("email")?:""
        password = arguments?.getString("password")?:""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding= FragmentStepTwoRegisterBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setListeners(){
        mBinding.inputName.doAfterTextChanged {
            isValidName = validations.isValidText(it.toString())
            mBinding.ilName.error = if (isValidName) null else "Ingrese un nombre"
            updateEnableNextBtn()
        }
        mBinding.inputLastName.doAfterTextChanged {
            isValidLastName = validations.isValidText(it.toString())
            mBinding.ilLastName.error = if (isValidLastName) null else "Ingrese un apellido"
            updateEnableNextBtn()
        }
        mBinding.inputPhone.doAfterTextChanged {
            isValidPhone = validations.isValidPhone(it.toString())
            mBinding.ilPhone.error = if (isValidPhone) null else "Ingrese un 8-10 digitos"
            updateEnableNextBtn()
        }
        mBinding.btNext.setOnClickListener{
            if (isValidName && isValidLastName && isValidBirthDate && isValidPhone){
                val action = StepTwoRegisterFragmentDirections.actionStepTwoRegisterFragmentToStepThreeRegisterFragment(
                    email,
                    password,
                    mBinding.inputName.text.toString(),
                    mBinding.inputLastName.text.toString(),
                    mBinding.inputBirth.text.toString(),
                    mBinding.inputPhone.text.toString()
                )
                findNavController().navigate(action)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun updateEnableNextBtn(){
        val enable = isValidName && isValidLastName && isValidBirthDate && isValidPhone
        val textColor = if (enable) R.color.blue_app else R.color.grey7
        val backgroundColor = if (enable) R.color.white else R.color.grey2

        mBinding.btNext.apply {
            isEnabled = enable
            setBackgroundColor(requireContext().getColor(textColor))
            setTextColor(requireContext().getColor(backgroundColor))
        }
    }

}