package com.example.myapplication.ui.team

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.core.extensionFun.toast
import com.example.myapplication.data.models.TeamGroup
import com.example.myapplication.databinding.FragmentTeamMainBinding
import com.example.myapplication.ui.home.AssistenceMainFragmentDirections
import com.example.myapplication.ui.team.adapters.TeamAdapter
import com.example.myapplication.ui.userScreen.UserScreenFragmentDirections

class TeamMainFragment : Fragment(R.layout.fragment_team_main) {

    private val viewModel : TeamViewModel by activityViewModels()
    private lateinit var mBinding: FragmentTeamMainBinding
    private lateinit var mAdapterTeams : TeamAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentTeamMainBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setLiveData()
        setListeners()
    }

    private fun initView() {
        viewModel.getTeams()
    }

    private fun setLiveData() {
        viewModel.teams.observe(viewLifecycleOwner){
            var cNum = 0
            setTeamAdapter(it)
            for(i in 0..5){
                cNum += it[i].users.size
            }
            mBinding.tvColaboratorCount.text = String.format(getString(R.string.cNum),cNum)
        }
    }

    private fun setTeamAdapter(list: ArrayList<TeamGroup>) {
        mAdapterTeams = TeamAdapter(list){ context?.toast(it) }

        mBinding.rvTeams.apply {
            adapter = mAdapterTeams
            layoutManager = LinearLayoutManager(mBinding.rvTeams.context)
        }
    }

    private fun setListeners(){
        with(mBinding){
            containerHomeNav.setOnClickListener {
                val navBuilder = NavOptions.Builder()
                navBuilder.setEnterAnim(R.anim.enter_from_right).setExitAnim(R.anim.exit_from_right)
                    .setPopEnterAnim(R.anim.enter_from_left).setPopExitAnim(R.anim.exit_from_left)
                findNavController().
                navigate(
                    TeamMainFragmentDirections.actionTeamMainFragmentToAssistenceMainFragment(),
                    navBuilder.build())
            }
        }
    }
}