package com.cohen.searchfordoctor

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cohen.search_doctors.DoctorsRecyclerViewAdapter
import com.cohen.search_doctors.DoctorsViewModel
import kotlinx.android.synthetic.main.fragment_item_list.*
import androidx.recyclerview.widget.DividerItemDecoration




class ItemsListFragment : androidx.fragment.app.Fragment() {

    lateinit var doctorsViewModel: DoctorsViewModel
    lateinit var locationManager: LocationManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        doctorsViewModel = ViewModelProviders.of(activity!!).get(DoctorsViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        askPermissions()
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        recyclerView?.apply {
            adapter = DoctorsRecyclerViewAdapter(doctorsViewModel, activity!!)
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(
                recyclerView.context,
                (layoutManager as LinearLayoutManager).orientation
            ))
        }

        editText?.apply {
            addTextChangedListener(object : TextWatcher {
                @SuppressLint("MissingPermission")
                override fun afterTextChanged(s: Editable?) {
                    if (!permissionsGranted()) {
                        askPermissions()
                    } else {
                        if (!s.isNullOrEmpty()) {
                            doctorsViewModel.setSearch(
                                s.toString(),
                                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            )
                        }
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })
        }
    }

    fun askPermissions() {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        activity?.let { ActivityCompat.requestPermissions(it, permissions, 0) }
    }

    fun permissionsGranted(): Boolean {
        return context?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } == PackageManager.PERMISSION_GRANTED
    }


    companion object {


        @JvmStatic
        fun newInstance() =
            ItemsListFragment()
    }
}
