package com.cohen.searchfordoctor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cohen.search_doctors.DoctorsViewModel
import com.cohen.search_doctors.UserViewModel

class MainActivity : AppCompatActivity() {
    lateinit var doctorsViewModel: DoctorsViewModel
    lateinit var userViewModel: UserViewModel
    private val itemsListFragment: ItemsListFragment  by lazy {
        if (supportFragmentManager.findFragmentByTag("ItemsListFragment") != null) {
            supportFragmentManager.findFragmentByTag("ItemsListFragment") as ItemsListFragment
        } else {
            ItemsListFragment.newInstance()
        }
    }
    private val loginFragment by lazy {
        if (supportFragmentManager.findFragmentByTag("LoginFragment") != null) {
            supportFragmentManager.findFragmentByTag("LoginFragment") as LoginFragment
        } else {
            LoginFragment.newInstance()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.init(this as AppCompatActivity)
        userViewModel.getUser().observe(this, Observer {
            if (it == null) {
                setLoginMode()
            } else {
                setListMode()
            }

        })
        doctorsViewModel = ViewModelProviders.of(this).get(DoctorsViewModel::class.java)
        doctorsViewModel.init(this, userViewModel)
    }

    private fun setListMode() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, itemsListFragment, "itemsListFragment")
        transaction.commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setLoginMode() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, loginFragment, "loginFragment")
        transaction.commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

}
