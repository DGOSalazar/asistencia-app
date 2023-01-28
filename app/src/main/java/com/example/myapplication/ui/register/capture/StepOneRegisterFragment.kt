package com.example.myapplication.ui.register.capture

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStepOneRegisterBinding

class StepOneRegisterFragment : Fragment(R.layout.fragment_step_one_register) {

    private lateinit var mBinding: FragmentStepOneRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding= FragmentStepOneRegisterBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }
}