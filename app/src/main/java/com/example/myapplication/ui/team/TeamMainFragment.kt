package com.example.myapplication.ui.team

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.core.extensionFun.toast
import com.example.myapplication.core.utils.Status
import com.example.myapplication.data.models.TeamGroup
import com.example.myapplication.databinding.FragmentTeamMainBinding
import com.example.myapplication.ui.team.adapters.TeamAdapter

class TeamMainFragment : Fragment(R.layout.fragment_team_main) {

    private val viewModel: TeamViewModel by activityViewModels()
    private lateinit var mBinding: FragmentTeamMainBinding
    private lateinit var mAdapterTeams: TeamAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentTeamMainBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLiveData()
        setListeners()
    }

    private fun setLiveData() {
        viewModel.getTeams().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    var cNum = 0
                    it.data?.let { t ->
                        setTeamAdapter(t)
                    }
                    for (i in 0..5) {
                        cNum += it.data!!.size
                    }
                    with(mBinding) {
                        tvColaboratorCount.text = String.format(getString(R.string.cNum), cNum)
                        progress.visibility = View.GONE
                        toolbar.visibility = View.VISIBLE
                        svTeam.visibility = View.VISIBLE
                    }
                }
                Status.LOADING -> {}
                Status.ERROR -> {}
            }
        }
    }

    private fun setTeamAdapter(list: ArrayList<TeamGroup>) {
        mAdapterTeams = TeamAdapter(list) { context?.toast(it) }

        mBinding.rvTeams.apply {
            adapter = mAdapterTeams
            layoutManager = LinearLayoutManager(mBinding.rvTeams.context)
        }
    }

    private fun setListeners() {
        with(mBinding) {
            //containerHomeNav.setOnClickListener {
            /*val navBuilder = NavOptions.Builder()
            navBuilder.setEnterAnim(R.anim.enter_from_right).setExitAnim(R.anim.exit_from_right)
                .setPopEnterAnim(R.anim.enter_from_left).setPopExitAnim(R.anim.exit_from_left)
            findNavController().
            navigate(
                TeamMainFragmentDirections.actionTeamMainFragmentToAssistenceMainFragment(),
                navBuilder.build())*/
            //}
        }
    }
}