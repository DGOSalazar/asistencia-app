package com.example.myapplication.ui.register.capture


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStepOneRegisterBinding
import com.example.myapplication.ui.register.Validations
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class StepOneRegisterFragment : Fragment(R.layout.fragment_step_one_register) {

    @Inject
    lateinit var validations:Validations

    private lateinit var mBinding: FragmentStepOneRegisterBinding

    private var isValidEmail = false
    private var isValidPassword = false
    private var isValidPassword2 = false
    private var password1 = ""
    private var password2 = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentStepOneRegisterBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setListeners(){
        mBinding.inputEmail.doAfterTextChanged {
            isValidEmail = validations.isValidEmail(it.toString())
            mBinding.ilMail.error = if (isValidEmail) null else "Debe ingresar un correo valido"
            updateEnableNextBtn()
        }
        mBinding.inputPass1.doAfterTextChanged {
            password1 = it.toString()
            isValidPassword = validations.isValidPassword(it.toString())
            mBinding.ilPass1.error = if (isValidPassword) null else "Debe de tener al menos 8 caracteres"
            updateEnableNextBtn()
        }
        mBinding.inputPass2.doAfterTextChanged {
            password2 = it.toString()
            isValidPassword2 = validations.isValidPassword(it.toString())
            mBinding.ilPass2.error = if (isValidPassword2 && password1 == password2) null else "Contraseña diferente"
            updateEnableNextBtn()
        }
        mBinding.btNext.setOnClickListener{
            if (isValidEmail && isValidPassword && isValidPassword2){

                val navBuilder = NavOptions.Builder()
                navBuilder.setEnterAnim(R.anim.enter_from_left).setExitAnim(R.anim.exit_from_left)
                    .setPopEnterAnim(R.anim.enter_from_right).setPopExitAnim(R.anim.exit_from_right)

                val action = StepOneRegisterFragmentDirections.actionStepOneRegisterFragmentToStepTwoRegisterFragment(
                    mBinding.inputEmail.text.toString(),
                    password1
                )
                findNavController().navigate(action, navBuilder.build())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun updateEnableNextBtn(){
        val isValidData = isValidEmail && isValidPassword && isValidPassword2 && password2 == password1
        val textColor = if (isValidData) R.color.blue_app else R.color.grey7
        val backgroundColor = if (isValidData) R.color.white else R.color.grey2

        mBinding.btNext.apply {
            isEnabled = isValidData
            setBackgroundColor(requireContext().getColor(textColor))
            setTextColor(requireContext().getColor(backgroundColor))
        }
    }


}