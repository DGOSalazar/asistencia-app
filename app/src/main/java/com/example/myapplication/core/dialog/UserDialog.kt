package com.example.myapplication.core.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.core.extensionFun.glide
import com.example.myapplication.data.models.User
import com.example.myapplication.databinding.DialogUserCheckBinding
import com.example.myapplication.ui.home.HomeViewModel

class UserDialog(var user: User): DialogFragment(R.layout.dialog_user_check) {
    private lateinit var mBinding: DialogUserCheckBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.dialogForm)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinding = DialogUserCheckBinding.inflate(layoutInflater, container, false)
        setView()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.backGroundRefused)))//Color.TRANSPARENT))
        return mBinding.root
    }

    private fun setView() {
        with(mBinding){
            imageProfile.glide(user.profilePhoto)
            tvName1.text = String.format(getString(R.string.fullName),user.name, user.lastName1)
            tvColaboratorNum.text = String.format(getString(R.string.employee_num),user.employee)
            tvTeam.text = String.format(getString(R.string.project),user.team)
            tvPhoneNumber.text = String.format(getString(R.string.phone_num),user.phone)
            tvPosition1.text = user.position
        }
    }
}