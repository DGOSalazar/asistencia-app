package com.example.myapplication.ui.userScreen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.core.dialog.AddProjectDialog
import com.example.myapplication.core.utils.Status
import com.example.myapplication.core.extensionFun.glide
import com.example.myapplication.core.extensionFun.toast
import com.example.myapplication.data.models.*
import com.example.myapplication.databinding.FragmentUserScreenBinding
import com.example.myapplication.ui.home.HomeActivity
import com.example.myapplication.ui.userScreen.adapters.NotifyAdapter
import com.example.myapplication.ui.userScreen.adapters.ProjectsAdapter

class UserScreenFragment : Fragment(R.layout.fragment_user_screen) {

    private var user: User = User()
    private var listProjects = ProjectsDomainModel()
    private lateinit var mBinding: FragmentUserScreenBinding
    private val viewModel: UserScreenViewModel by activityViewModels()
    private lateinit var mAdapterNoty: NotifyAdapter
    private lateinit var mAdapterProjects : ProjectsAdapter
    private var imageUriLocal: Uri? = null

    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data != null) {
                    imageUriLocal = it.data?.data
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentUserScreenBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel.statusForUrl.observe(viewLifecycleOwner){
            when(it.status)
            {
                Status.LOADING->{
                    (activity as HomeActivity).showLoader()
                }
                Status.SUCCESS->{
                    (activity as HomeActivity).dismissLoader()
                }
                Status.ERROR->{
                    (activity as HomeActivity).dismissLoader()
                    Log.i("Error","Fatal")
                }
            }
        }
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
        viewModel.setUserMoreData.observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS -> {
                    (activity as HomeActivity).dismissLoader()
                    viewModel.getMoreUserData()
                }
                Status.ERROR -> {
                    context?.toast("error")
                }
                Status.LOADING -> {
                    (activity as HomeActivity).showLoader()
                }
            }
        }
        viewModel.userMoreData.observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS -> {
                    (activity as HomeActivity).dismissLoader()
                    setNewData(it.data!!)
                }
                Status.ERROR -> {
                    setNewData(UserAdditionalData())
                }
                Status.LOADING -> {
                    (activity as HomeActivity).showLoader()
                }
            }
        }
        viewModel.userProjects.observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS -> {
                    (activity as HomeActivity).dismissLoader()
                    listProjects = it.data!!
                    launchAdapterProjects(listProjects)
                }
                Status.ERROR -> {
                    setNewData(UserAdditionalData())
                }
                Status.LOADING -> {
                    (activity as HomeActivity).showLoader()
                }
            }
        }
        viewModel.setUserProjects.observe(viewLifecycleOwner){
            when(it.status) {
                Status.SUCCESS -> {
                    (activity as HomeActivity).dismissLoader()
                    viewModel.getProjects()
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
    private fun initObservers(){
        viewModel.apply {
            getUserData()
            getMoreUserData()
            getProjects()
        }
    }

    private fun setNewData(user: UserAdditionalData) {
        with(mBinding) {
            tvCostCenter.text = String.format(getString(R.string.cost), user.costCenter)
            etCostCenter.setText(user.costCenter)
            tvManager.text = String.format(getString(R.string.manager), user.managerMain)
            etManager.setText(user.managerMain)
            tvManagerSup.text = String.format(getString(R.string.maanager_sup), user.managerDirect)
            etManagerSup.setText(user.managerDirect)
            tvProject.text = String.format(getString(R.string.project), user.project)
            etProject.setText(user.project)
            tvScrum.text = String.format(getString(R.string.scrum), user.scrumMaster)
            etSm.setText(user.scrumMaster)
            tvDateEnroll.text = String.format(getString(R.string.inDate), user.enrollDate)
            etInDate.setText(user.enrollDate)
            tvWorkerDays.text = String.format(getString(R.string.workerDays), "0")
            tvOfficeDays.text = String.format(getString(R.string.daysInOffice), "0")
        }
    }

    private fun launchAdapterNotify() {
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
        mAdapterNoty = NotifyAdapter(list)
        mBinding.notifyRecycler.apply {
            adapter = mAdapterNoty
            layoutManager = LinearLayoutManager(activity?.applicationContext)
        }
    }

    private fun launchAdapterProjects(projects : ProjectsDomainModel){
        mAdapterProjects =
            ProjectsAdapter(projects,getString(R.string.projectName),getString(R.string.liberationQ))
        mBinding.rvProjects.apply {
            adapter = mAdapterProjects
            layoutManager = LinearLayoutManager(activity?.applicationContext)
        }
    }

    private fun setUi() {
        with(mBinding) {
            ivUserPhoto.glide(user.profilePhoto)
            tvNameUser.text = user.name
            tvPositionUser.text =
                String.format(getString(R.string.positionAndTeam), user.position, user.team)
            colaboratorNum.text = String.format(getString(R.string.employee_num), user.employee)
        }
    }

    private fun setListeners() {
        var isCheck = false
        with(mBinding) {
            tvNotificationH.setOnClickListener {
                changeViewToNotify(true)
                launchAdapterNotify()
            }

            tvResumenH.setOnClickListener {
                changeViewToNotify(false)
            }

            ivImg.setOnClickListener {
                //viewModel.deletePhoto()
            }
            ivAddProject.setOnClickListener{
                AddProjectDialog{
                    listProjects.projectInfo.add(it)
                    viewModel.saveProjects(listProjects)
                }.show(parentFragmentManager,"showDialog")
            }

            btEdit.setOnClickListener {
                isCheck = !isCheck
                if (isCheck) with(mBinding) {
                    btEdit.text = getString(R.string.donePerfil)
                    etCostCenter.visibility = View.VISIBLE
                    etManager.visibility = View.VISIBLE
                    etInDate.visibility = View.VISIBLE
                    etManagerSup.visibility = View.VISIBLE
                    etProject.visibility = View.VISIBLE
                    etSm.visibility = View.VISIBLE
                }
                else with(mBinding) {
                    btEdit.text = getString(R.string.edit_profile)
                    etCostCenter.visibility = View.GONE
                    etManager.visibility = View.GONE
                    etInDate.visibility = View.GONE
                    etManagerSup.visibility = View.GONE
                    etProject.visibility = View.GONE
                    etSm.visibility = View.GONE

                    viewModel.setMoreData(
                        UserAdditionalData(
                            costCenter = etCostCenter.text.toString(),
                            managerMain = etManager.text.toString(),
                            managerDirect = etManagerSup.text.toString(),
                            project = etProject.text.toString(),
                            scrumMaster = etSm.text.toString(),
                            enrollDate = etInDate.text.toString()
                        )
                    )
                }
            }
            ivShowProject.setOnClickListener {
                showProjects(true)
            }
            ivHideProject.setOnClickListener {
                showProjects(false)
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

    private fun showProjects(isShow: Boolean = true){
        with(mBinding){
            rvProjects.visibility = if (isShow) View.VISIBLE else View.GONE
            ivHideProject.visibility = if (isShow) View.VISIBLE else View.GONE
            ivShowProject.visibility = if (isShow) View.GONE else View.VISIBLE
        }
    }

    private fun fromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        responseLauncher.launch(intent)
    }
}