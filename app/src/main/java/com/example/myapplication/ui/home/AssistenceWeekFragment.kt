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
    private var day: String = ""
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
        subscribeLiveData()
        setListeners()
    }

    private fun subscribeLiveData() {
      with(viewModel){
          daySelected.observe(viewLifecycleOwner){
              day = it
              context?.toast(day)
          }
          users.observe(viewLifecycleOwner){
              selectDay.userList = it
              setUserAdapter(selectDay.userList)
          }
          weekSelected.observe(viewLifecycleOwner){
              days = it
              setDaysAdapter(days)
          }
          userEmails.observe(viewLifecycleOwner){
              if (it.isEmpty()){
                  setUserAdapter(listOf())

              }
              viewModel.enrollUser(day,viewModel.userEmails.value!!)
              viewModel.getUserDatastore(it)
          }
      }
    }
    private fun setListeners() {
        with(mBinding){
            ivBack.setOnClickListener{
                cleanUserAdapter()
                activity?.onBackPressed()
            }
            btAdd.setOnClickListener {
                //context?.toast(viewModel!!.saveEmail!!.value!!)
                EnrollToDayDialog().show(parentFragmentManager,"Yep")
                viewModel.addUserToListUsers("diego.maradona@coppl.com")
                viewModel.addUserToDay()
                changeButton()
            }
            btUndo.setOnClickListener {
                changeButton()
            }
        }
    }

    private fun setDaysAdapter(_p:List<Day>) {
        mAdapter = WeekAdapter(_p){
            click(it)
            viewModel.getListEmails(day)
        }
        mBinding.recyclerWeek.apply {
            layoutManager = GridLayoutManager(mBinding.root.context,5)
            adapter = mAdapter
        }
    }
    private fun setUserAdapter(userList: List<User>) {
        if(userList.isEmpty()) {
            setEmptyUser()
        }
        else {
            setEmptyUser()
            mBinding.tvActualMonth.text = "${userList.size} ${getString(R.string.msgAssit)}"
            mUserAdapter = UserAdapter(userList)
            mBinding.recyclerUsers.apply {
                layoutManager = LinearLayoutManager(activity?.applicationContext)
                adapter = mUserAdapter
            }
        }
    }

    private fun setEmptyUser() {
        with(mBinding){
            if (tvActualMonth.visibility == View.GONE) {
                tvActualMonth.visibility = View.VISIBLE
                tvFreeDay.visibility = View.GONE
                recyclerUsers.visibility = View.VISIBLE
            }else{
                tvActualMonth.visibility=View.GONE
                tvFreeDay.visibility = View.VISIBLE
                recyclerUsers.visibility=View.GONE
            }
        }
    }

    private fun changeButton() {
        with(mBinding) {
            if (mBinding.btAdd.visibility == View.VISIBLE) {
                    btAdd.visibility = View.GONE
                    btUndo.visibility = View.VISIBLE
                }else{
                    btAdd.visibility = View.VISIBLE
                    btUndo.visibility = View.GONE
                }
        }
    }

    private fun cleanUserAdapter() {
        setUserAdapter(listOf())
    }

    private fun click(i: Day){
        day = i.date
        viewModel.setDay(day)
        cleanUserAdapter()
        cleanSelected(i.dayOfWeek-1)
        setDaysAdapter(days)
        context?.toast(day)
    }

    private fun cleanSelected(t:Int){
        for(i in 0..4) {
            days[i].selected = false
        }
        days[t].selected = true
        selectDay = days[t]
    }
}