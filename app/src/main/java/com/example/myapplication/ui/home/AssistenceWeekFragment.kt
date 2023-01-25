package com.example.myapplication.ui.home

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.core.dialog.EnrollToDayDialog
import com.example.myapplication.core.extensionFun.toast
import com.example.myapplication.data.models.Day
import com.example.myapplication.data.models.User
import com.example.myapplication.databinding.FragmentAssistencceWeekBinding
import com.example.myapplication.ui.home.adapters.UserAdapter
import com.example.myapplication.ui.home.adapters.WeekAdapter

@RequiresApi(Build.VERSION_CODES.O)
class AssistenceWeekFragment : Fragment(R.layout.fragment_assistencce_week) {

    private lateinit var mBinding: FragmentAssistencceWeekBinding
    private lateinit var mAdapter: WeekAdapter
    private lateinit var mUserAdapter: UserAdapter
    private val viewModel: HomeViewModel by activityViewModels()
    private var days: List<Day> = listOf()
    private var selectDay : Day = Day()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding= FragmentAssistencceWeekBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        days = viewModel.weekSelected.value!!
        setDaysAdapter(days)
        setUserAdapter(days[0].userList)
        subscribeLiveData()
        setListeners()
    }

    private fun subscribeLiveData() {
      with(viewModel){
          user.observe(viewLifecycleOwner){
              selectDay.userList.add(it)
              setUserAdapter(selectDay.userList)
              context?.toast(it.toString())
          }
      }
    }

    private fun setDaysAdapter(_p:List<Day>) {
        mAdapter = WeekAdapter(_p){
            click(it)
        }
        mBinding.recyclerWeek.apply {
            layoutManager = GridLayoutManager(mBinding.root.context,5)
            adapter = mAdapter
        }
    }
    private fun setUserAdapter(userList: List<User>) {
        mUserAdapter = UserAdapter(userList)
        mBinding.recyclerUsers.apply {
            layoutManager= LinearLayoutManager(activity?.applicationContext)
            adapter=mUserAdapter
        }
    }

    private fun setListeners() {
        with(mBinding){
            ivBack.setOnClickListener{
                activity?.onBackPressed()
            }
            btAdd.setOnClickListener {
                val dialog = EnrollToDayDialog().show(parentFragmentManager,"Yep")

                viewModel.getUserData()
            }
        }
    }

    private fun click(i: Day){
        cleanSelected(i.dayOfWeek-1)
        setUserAdapter(selectDay.userList)
        setDaysAdapter(days)
    }

    private fun cleanSelected(t:Int){
        for(i in 0..4) {
            days[i].selected = false
        }
        days[t].selected = true
        selectDay = days[t]
    }
}