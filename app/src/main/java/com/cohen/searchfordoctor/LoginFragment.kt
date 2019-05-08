package com.cohen.searchfordoctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.cohen.search_doctors.UserViewModel
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : androidx.fragment.app.Fragment() {

    lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        userViewModel = ViewModelProviders.of(activity!!).get(UserViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginButton?.apply {
            setOnClickListener {
                userViewModel.loginUser()
            }
        }

    }


    companion object {


        @JvmStatic
        fun newInstance() =
            LoginFragment()
    }
}
