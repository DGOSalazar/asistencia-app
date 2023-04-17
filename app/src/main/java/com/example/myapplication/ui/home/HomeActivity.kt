package com.example.myapplication.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityHomeBinding
import com.example.myapplication.sys.generics.loader.LoaderFragment
import com.example.myapplication.ui.team.TeamMainFragmentDirections
import com.example.myapplication.ui.userScreen.UserScreenFragmentDirections
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity(){

    private lateinit var mBinding: ActivityHomeBinding
    private val loader by lazy { LoaderFragment() }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setListeners()
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setListeners(){

        mBinding.containerHomeNav.setOnClickListener{
            setOnItemSelected(it.id)
        }
        mBinding.containerTeamNav.setOnClickListener{
            setOnItemSelected(it.id)
        }
        mBinding.containerMyProfileNav.setOnClickListener{
            setOnItemSelected(it.id)
        }
    }

    @SuppressLint("NewApi", "DetachAndAttachSameFragment")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setOnItemSelected(id:Int){
        with(mBinding){
            if (id == R.id.container_home_nav)
                containerHomeNav.setBackgroundResource(R.drawable.bg_buttom_nav)
            else
                containerHomeNav.setBackgroundColor(getColor(R.color.grey1))
            if (id == R.id.container_team_nav)
                containerTeamNav.setBackgroundResource(R.drawable.bg_buttom_nav)
            else
                containerTeamNav.setBackgroundColor(getColor(R.color.grey1))
            if (id == R.id.container_my_profile_nav)
                containerMyProfileNav.setBackgroundResource(R.drawable.bg_buttom_nav)
            else
                containerMyProfileNav.setBackgroundColor(getColor(R.color.grey1))

            val action: NavDirections?
            val navController = findNavController(fragmentContainer.id)
            val currentDestination = navController.currentDestination

            when(currentDestination!!.id){
                R.id.assistenceMainFragment->{
                    action = when(id){
                        containerHomeNav.id -> null
                        containerTeamNav.id -> AssistenceMainFragmentDirections.actionAssistenceMainFragmentToTeamMainFragment()
                        containerMyProfileNav.id -> AssistenceMainFragmentDirections.actionAssistenceMainFragmentToUserScreenFragment()
                        else -> null
                    }
                }
                R.id.teamMainFragment->{
                    action = when(id){
                        containerHomeNav.id -> TeamMainFragmentDirections.actionTeamMainFragmentToAssistenceMainFragment()
                        containerTeamNav.id -> null
                        containerMyProfileNav.id -> TeamMainFragmentDirections.actionTeamMainFragmentToUserScreenFragment()
                        else -> null
                    }
                }
                R.id.userScreenFragment->{
                    action = when(id){
                        containerHomeNav.id -> UserScreenFragmentDirections.actionUserScreenFragmentToAssistenceMainFragment()
                        containerTeamNav.id -> UserScreenFragmentDirections.actionUserScreenFragmentToTeamMainFragment()
                        containerMyProfileNav.id -> null
                        else -> null
                    }
                }
                else -> action = null
            }

            if (action != null)
                fragmentContainer.findNavController().navigate(action)
        }

    }

    fun hideBottomBar(isVisible: Boolean = false) {
        if (!isVisible)
            mBinding.navBottomContainer.visibility= View.GONE
        else
            mBinding.navBottomContainer.visibility= View.VISIBLE
    }


    fun showLoader() {
        try {
            val loaderDialog = supportFragmentManager.findFragmentByTag("Loader")
            val isShowing = loader.dialog?.isShowing ?: false
            if (loaderDialog != null && loaderDialog.isAdded) {
                return
            }

            if (!loader.isAdded && !loader.isVisible && !isShowing) {
                loader.show(supportFragmentManager, "Loader")
                supportFragmentManager.executePendingTransactions()
            }
        } catch (e: Exception) {
            //ERROR
        }
    }

    fun dismissLoader() {
        if (loader.isAdded) {
            if (loader.isResumed) {
                loader.dismiss()
            } else {
                loader.dismissAllowingStateLoss()
            }
        }
    }

}