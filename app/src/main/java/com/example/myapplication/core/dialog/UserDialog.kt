package com.example.myapplication.core.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.core.extensionFun.glide
import com.example.myapplication.data.models.User
import com.example.myapplication.databinding.DialogUserCheckBinding
import com.example.myapplication.ui.home.HomeViewModel

class UserDialog(var user: User): DialogFragment() {
    private lateinit var mBinding: DialogUserCheckBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinding = DialogUserCheckBinding.inflate(layoutInflater, container, false)
        setView()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return mBinding.root
    }

    private fun setView() {
        with(mBinding){
            imageProfile.glide(user.profilePhoto)
            tvName1.text = user.name
            tvColaboratorNum.text = user.employee.toString()
            tvTeam.text = user.team
            tvPhoneNumber.text = user.phone
            tvBirth.text=user.birthDate
            tvPosition1.text = user.position
        }
    }
}