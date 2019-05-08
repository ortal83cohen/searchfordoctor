package com.cohen.search_doctors


import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cohen.searchfordoctor.R
import com.cohen.searchfordoctor.data.Doctor
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.lang.Exception

class DoctorViewHolder(v: View, private val mContext: Context) :
    RecyclerView.ViewHolder(v) {

    var image: ImageView = v.findViewById(R.id.image)
    var name: TextView = v.findViewById(R.id.name)
    var address: TextView = v.findViewById(R.id.address)
    private var mItem: Doctor? = null
    private var mPosition: Int = 0

    fun assignItem(item: Doctor, position: Int, session: String?) {
        mPosition = position
        itemView.setOnClickListener {
            //todo
        }
        mItem = item
        mItem?.photoId?.let {
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $session")
                        .build()
                   return@addInterceptor  chain.proceed(newRequest)
                }
                .build()

            val picasso = Picasso.Builder(mContext)
                .downloader(OkHttp3Downloader(client))

                .build()
            picasso.isLoggingEnabled = true
            picasso
                .load("https://api.staging.vivy.com/api/doctors/${mItem?.id}/keys/profilepictures")
                .placeholder(R.mipmap.ic_launcher)
                .fit().centerCrop()
                .into(image)
        }
        name.text = mItem!!.name
        address.text = mItem!!.address
    }


}