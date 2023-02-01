package com.example.myapplication.ui.register.capture

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.NavOptions
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
    private var isChangeTextByUser = true

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
            if (!isValidName){
                mBinding.ilName.isErrorEnabled = true
                mBinding.ilName.error = if (isValidName) null else "Ingrese un nombre"
            }else
                mBinding.ilName.isErrorEnabled = false
            updateEnableNextBtn()
        }
        mBinding.inputLastName.doAfterTextChanged {
            isValidLastName = validations.isValidText(it.toString())
            if (!isValidLastName){
                mBinding.ilLastName.isErrorEnabled = true
                mBinding.ilLastName.error = if (isValidLastName) null else "Ingrese un nombre"
            }else
                mBinding.ilLastName.isErrorEnabled = false
            updateEnableNextBtn()
        }
        mBinding.inputPhone.doAfterTextChanged {
            isValidPhone = validations.isValidPhone(it.toString())
            if (!isValidPhone){
                mBinding.ilPhone.isErrorEnabled = true
                mBinding.ilPhone.error = if (isValidPhone) null else "Ingrese un nombre"
            }else
                mBinding.ilPhone.isErrorEnabled = false
            updateEnableNextBtn()
        }
        mBinding.inputBirth.addTextChangedListener(dateWasher)
        mBinding.btNext.setOnClickListener{

            val navBuilder = NavOptions.Builder()
            navBuilder.setEnterAnim(R.anim.enter_from_left).setExitAnim(R.anim.exit_from_left)
                .setPopEnterAnim(R.anim.enter_from_right).setPopExitAnim(R.anim.exit_from_right)

            val action = StepTwoRegisterFragmentDirections.actionStepTwoRegisterFragmentToStepThreeRegisterFragment(
                email,
                password,
                mBinding.inputName.text.toString(),
                mBinding.inputLastName.text.toString(),
                mBinding.inputBirth.text.toString(),
                mBinding.inputPhone.text.toString()
            )
            findNavController().navigate(action, navBuilder.build())
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

    private var lenghtOfTextBeforeTextChanged = 0
    private val dateWasher = object :TextWatcher{
        @SuppressLint("SetTextI18n")
        override fun beforeTextChanged(changedText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            lenghtOfTextBeforeTextChanged = changedText?.length ?: 0
        }

        @SuppressLint("SetTextI18n")
        override fun onTextChanged(changedText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (changedText.isNullOrEmpty()) return

            if (isChangeTextByUser){
                val isAddSlash = lenghtOfTextBeforeTextChanged < changedText.length

                if(changedText[0].toString().toInt() > 1 || changedText[0].toString().toInt() < 0 && isAddSlash){
                    mBinding.inputBirth.setText("${changedText.dropLast(1)}")
                    return
                }
                if (changedText.length in arrayListOf(2,5) && isAddSlash){
                    isChangeTextByUser = false
                    mBinding.inputBirth.setText("$changedText/")
                }
                if (changedText.length in arrayListOf(3,6) && isAddSlash){
                    val isSlashTheLast = changedText.last() == '/'
                    val formatText = if (isSlashTheLast)"$changedText/" else "${changedText.dropLast(1)}/${changedText.last()}"
                    isChangeTextByUser = false
                    mBinding.inputBirth.setText(formatText)
                }
                if (changedText.length in arrayListOf(3,6) && !isAddSlash){
                    isChangeTextByUser = false
                    mBinding.inputBirth.setText("${changedText.dropLast(1)}")
                }
                mBinding.inputBirth.setSelection(mBinding.inputBirth.length())
            }else
                isChangeTextByUser = true
        }

        override fun afterTextChanged(p0: Editable?) { }
    }

}