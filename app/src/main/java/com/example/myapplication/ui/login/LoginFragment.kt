package com.example.myapplication.ui.login

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val EMAIL_KEY = "email_key"
const val PASSWORD_KEY = "password_key"

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login)  {

    private lateinit var mBinding :  FragmentLoginBinding
    private val viewModel : LoginViewModel by activityViewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentLoginBinding.inflate(layoutInflater, container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListeners()
        subscribeLiveData()
    }

    private fun setListeners() {
        with(mBinding) {
            bnLogin.setOnClickListener {
                viewModel.login(mBinding.inputEmail.text.toString(),mBinding.inputPass.text.toString())
                Toast.makeText(mBinding.root.context, "Hola", Toast.LENGTH_SHORT).show()
            }
            bnRegister.setOnClickListener{
                val navBuilder = NavOptions.Builder()
                navBuilder.setEnterAnim(R.anim.enter_from_left).setExitAnim(R.anim.exit_from_left)
                    .setPopEnterAnim(R.anim.enter_from_right).setPopExitAnim(R.anim.exit_from_right)
                findNavController().navigate(R.id.action_loginFragment_to_stepOneRegisterFragment2, null, navBuilder.build())
            }
        }
    }

    private fun subscribeLiveData() {
        viewModel.userExist.observe(viewLifecycleOwner){
            if (it) {
                sharedPreferences.edit().putString(EMAIL_KEY, mBinding.inputEmail.text.toString()).apply()
                findNavController().navigate(R.id.action_loginFragment_to_assistenceMainFragment)
            }
            else Toast.makeText(mBinding.root.context,("El usuario no aparece en la base de datos"), Toast.LENGTH_SHORT).show()
        }
    }

}