package com.example.myapplication.ui.userScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.core.extensionFun.glide
import com.example.myapplication.core.extensionFun.toast
import com.example.myapplication.data.models.Notify
import com.example.myapplication.data.models.User
import com.example.myapplication.databinding.FragmentUserScreenBinding
import com.example.myapplication.ui.home.AssistenceMainFragmentDirections
import com.example.myapplication.ui.userScreen.adapters.NotifyAdapter

class UserScreenFragment : Fragment(R.layout.fragment_user_screen) {

    private var user: User = User()
    private lateinit var mBinding: FragmentUserScreenBinding
    private val viewModel : UserScreenViewModel by activityViewModels()
    private lateinit var mAdapter: NotifyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            user = UserScreenFragmentArgs.fromBundle(it).user
        }
        mBinding = FragmentUserScreenBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUi()
        setListeners()
        //setObservers()
    }

    private fun setObservers() {
        viewModel.notifyData.observe(viewLifecycleOwner){
            launchAdapter()
        }
    }


    private fun launchAdapter() {
        val list = arrayListOf<Notify>(
            Notify("001","Recuerda registrarte la siguiente semana",
                R.drawable.icon_timer,"justo ahora"),
            Notify("001","Recuerda registrarte la siguiente semana",
                R.drawable.icon_timer,"justo ahora"),
            Notify("001","Recuerda registrarte la siguiente semana",
                R.drawable.icon_timer,"justo ahora"),
            Notify("001","Recuerda registrarte la siguiente semana",
                R.drawable.icon_timer,"justo ahora"))

        mAdapter = NotifyAdapter(list)
        mBinding.notifyRecycler.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity?.applicationContext)
        }
        mBinding.notifyRecycler.visibility = View.VISIBLE

    }

    private fun setUi() {
        //viewModel.getNotification()
        with(mBinding){
            ivUserPhoto.glide(user.profilePhoto)
            tvNameUser.text = user.name
            tvPositionUser.text = String.format(getString(R.string.positionAndTeam),user.position)
            tvColaboratorNo.text = String.format(getString(R.string.employee_num),user.employee)
            tvCostCenter.text = String.format(getString(R.string.cost),"12982")
            tvManager.text= String.format(getString(R.string.manager),user.name)
            tvPosition.text = String.format(getString(R.string.positionRol),user.position)
            tvManagerSup.text = String.format(getString(R.string.maanager_sup),"Diana Estrada")
            tvProject.text = String.format(getString(R.string.project),"Abono coppel")
            tvScrum.text = String.format(getString(R.string.scrum),"Esteban Ochoa")
            tvDateEnroll.text = String.format(getString(R.string.inDate),"01-08-1998")
            tvWorkerDays.text = String.format(getString(R.string.workerDays),"22")
            tvOfficeDays.text = String.format(getString(R.string.daysInOffice),"7")
        }
    }
    private fun setListeners(){
        with(mBinding){
            containerHomeNav.setOnClickListener {
                val navBuilder = NavOptions.Builder()
                navBuilder.setEnterAnim(R.anim.enter_from_right).setExitAnim(R.anim.exit_from_right)
                    .setPopEnterAnim(R.anim.enter_from_left).setPopExitAnim(R.anim.exit_from_left)
                findNavController().
                navigate(UserScreenFragmentDirections.actionUserScreenFragmentToAssistenceMainFragment(),
                    navBuilder.build())
            }
            containerTeamNav.setOnClickListener {
                val navBuilder = NavOptions.Builder()
                navBuilder.setEnterAnim(R.anim.enter_from_right).setExitAnim(R.anim.exit_from_right)
                    .setPopEnterAnim(R.anim.enter_from_left).setPopExitAnim(R.anim.exit_from_left)
                findNavController().
                navigate(UserScreenFragmentDirections.actionUserScreenFragmentToTeamMainFragment(),
                    navBuilder.build())
            }
            tvNotification.setOnClickListener{
                with(mBinding){
                    ilPersonalData.visibility = View.GONE
                    ilPersonalHistory.visibility = View.GONE
                    vSeparation5.visibility = View.INVISIBLE
                    vActivateNotification.visibility = View.INVISIBLE
                    tvResumen.setTextColor(resources.getColor(R.color.grey5))
                    tvNotification.setTextColor(resources.getColor(R.color.blueCoppel))
                    vActivateResumen.visibility = View.VISIBLE
                    notifyRecycler.visibility = View.VISIBLE
                    launchAdapter()
                }
            }
            btEdit.setOnClickListener {

            }
        }
    }
}