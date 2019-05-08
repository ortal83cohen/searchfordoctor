package com.cohen.search_doctors


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.cohen.searchfordoctor.R


class DoctorsRecyclerViewAdapter(
    private val doctorsViewModel: DoctorsViewModel,
    private val activity: FragmentActivity

) : RecyclerView.Adapter<DoctorViewHolder>() {

    init {
        doctorsViewModel.getItems().observe(activity, Observer {
            notifyDataSetChanged()
        })

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return DoctorViewHolder(view, activity)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        doctorsViewModel.getItems().value?.get(position)?.let {
            holder.assignItem(it, position,doctorsViewModel.userViewModel.getUser().value)
        }

    }

    override fun getItemCount(): Int = doctorsViewModel.getItems().value?.size ?: 0


}
