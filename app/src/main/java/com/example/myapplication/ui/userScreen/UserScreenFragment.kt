package com.example.myapplication.ui.userScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.core.utils.Status
import com.example.myapplication.core.extensionFun.glide
import com.example.myapplication.core.extensionFun.toast
import com.example.myapplication.data.models.Notify
import com.example.myapplication.data.models.User
import com.example.myapplication.databinding.FragmentUserScreenBinding
import com.example.myapplication.ui.home.HomeActivity
import com.example.myapplication.ui.userScreen.adapters.NotifyAdapter

class UserScreenFragment : Fragment(R.layout.fragment_user_screen) {

    private var user: User = User()
    private lateinit var mBinding: FragmentUserScreenBinding
    private val viewModel: UserScreenViewModel by activityViewModels()
    private lateinit var mAdapter: NotifyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentUserScreenBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserData()
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel.notifyData.observe(viewLifecycleOwner) {
            //launchAdapter()
        }
        viewModel.userData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    (activity as HomeActivity).dismissLoader()
                    user = it.data!!
                    setUi()
                }
                Status.ERROR -> {
                    context?.toast("error")
                }
                Status.LOADING -> {
                    (activity as HomeActivity).showLoader()
                }
            }
        }
    }


    private fun launchAdapter() {
        val list =
            arrayListOf(
                Notify(
                    "001", "Recuerda registrarte la siguiente semana",
                    R.drawable.icon_timer, "justo ahora"
                ),
                Notify(
                    "001", "Recuerda registrarte la siguiente semana",
                    R.drawable.icon_timer, "justo ahora"
                )
            )
        mAdapter = NotifyAdapter(list)
        mBinding.notifyRecycler.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity?.applicationContext)
        }
        mBinding.notifyRecycler.visibility = View.VISIBLE
    }

    private fun setUi() {
        with(mBinding) {
            ivUserPhoto.glide(user.profilePhoto)
            tvNameUser.text = user.name
            tvPositionUser.text =
                String.format(getString(R.string.positionAndTeam), user.position, user.team)
            colaboratorNum.text = String.format(getString(R.string.employee_num), user.employee)
            tvCostCenter.text = String.format(getString(R.string.cost), "12982")
            tvManager.text = String.format(getString(R.string.manager), user.name)
            tvPosition.text = String.format(getString(R.string.positionRol), user.position)
            tvManagerSup.text = String.format(getString(R.string.maanager_sup), "Diana Estrada")
            tvProject.text = String.format(getString(R.string.project), "Abono coppel")
            tvScrum.text = String.format(getString(R.string.scrum), "Esteban Ochoa")
            tvDateEnroll.text = String.format(getString(R.string.inDate), "01-08-1998")
            tvWorkerDays.text = String.format(getString(R.string.workerDays), "22")
            tvOfficeDays.text = String.format(getString(R.string.daysInOffice), "7")
        }
    }

    private fun setListeners() {
        var isCheck = false
        with(mBinding) {
            tvNotificationH.setOnClickListener {
                changeViewToNotify(true)
                launchAdapter()
            }
            tvResumenH.setOnClickListener {
                changeViewToNotify(false)
            }

            btEdit.setOnClickListener {
                isCheck = !isCheck
                if (isCheck) with(mBinding) {
                    ivUserPhoto.setImageDrawable(resources.getDrawable(R.drawable.img_4, null))
                    btEdit.text = getString(R.string.donePerfil)
                    etCostCenter.visibility = View.VISIBLE
                    etManager.visibility = View.VISIBLE
                    etInDate.visibility = View.VISIBLE
                    etPosition.visibility = View.VISIBLE
                    etManagerSup.visibility = View.VISIBLE
                    etProject.visibility = View.VISIBLE
                    etSm.visibility = View.VISIBLE
                }
                else with(mBinding) {
                    ivUserPhoto.glide(user.profilePhoto)
                    btEdit.text = getString(R.string.edit_profile)
                    etCostCenter.visibility = View.GONE
                    etManager.visibility = View.GONE
                    etInDate.visibility = View.GONE
                    etPosition.visibility = View.GONE
                    etManagerSup.visibility = View.GONE
                    etProject.visibility = View.GONE
                    etSm.visibility = View.GONE
                }
            }
        }
    }

    private fun changeViewToNotify(noty: Boolean) {
        with(mBinding) {
            btEdit.visibility =
                if (noty) View.INVISIBLE else View.VISIBLE
            ilNotification.visibility =
                if (noty) View.VISIBLE else View.GONE
            ilSummary.visibility =
                if (noty) View.GONE else View.VISIBLE
        }
    }
}