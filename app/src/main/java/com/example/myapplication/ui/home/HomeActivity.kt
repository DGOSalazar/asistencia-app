package com.example.myapplication.ui.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityHomeBinding
import com.example.myapplication.databinding.ActivitySplashBinding
import com.example.myapplication.ui.team.TeamMainFragment
import com.example.myapplication.ui.userRegister.stepOne.StepOneRegisterFragmentDirections
import com.example.myapplication.ui.userScreen.UserScreenFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityHomeBinding

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
            setOnItemSelected(it.id, AssistenceMainFragment())
        }
        mBinding.containerTeamNav.setOnClickListener{
            setOnItemSelected(it.id, TeamMainFragment())
        }
        mBinding.containerMyProfileNav.setOnClickListener{
            setOnItemSelected(it.id, UserScreenFragment())
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setOnItemSelected(id:Int, fragment:Fragment){
        if (id == R.id.container_home_nav)
            mBinding.containerHomeNav.setBackgroundResource(R.drawable.bg_buttom_nav)
        else
            mBinding.containerHomeNav.setBackgroundColor(getColor(R.color.grey1))
        if (id == R.id.container_team_nav)
            mBinding.containerTeamNav.setBackgroundResource(R.drawable.bg_buttom_nav)
        else
            mBinding.containerTeamNav.setBackgroundColor(getColor(R.color.grey1))
        if (id == R.id.container_my_profile_nav)
            mBinding.containerMyProfileNav.setBackgroundResource(R.drawable.bg_buttom_nav)
        else
            mBinding.containerMyProfileNav.setBackgroundColor(getColor(R.color.grey1))

        val transition = supportFragmentManager.beginTransaction()
        transition.replace(R.id.fragment_container, fragment)
        transition.commit()
    }


}