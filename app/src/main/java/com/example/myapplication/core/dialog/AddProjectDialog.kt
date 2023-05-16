package com.example.myapplication.core.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.example.myapplication.data.models.ProjectInfo
import com.example.myapplication.databinding.DialogAddProjectBinding


class AddProjectDialog(
    var res: (ProjectInfo) -> Unit
    ): DialogFragment(R.layout.dialog_add_project) {

    private lateinit var mBinding: DialogAddProjectBinding
    private var data = ProjectInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.dialogForm)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinding = DialogAddProjectBinding.inflate(layoutInflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.backGroundRefused)))//Color.TRANSPARENT))
        view()
        return mBinding.root
    }

    private fun view() {
        listeners()
    }

    private fun listeners() {
        with(mBinding){
            btBack.setOnClickListener{
                dismiss()
            }
            btConfirm.setOnClickListener{
                data = ProjectInfo(
                    projectName = inputNameProject.text.toString(),
                    releaseDate = inputNameQ.text.toString()
                )
                res(data)
                dismiss()
            }
        }
    }
}