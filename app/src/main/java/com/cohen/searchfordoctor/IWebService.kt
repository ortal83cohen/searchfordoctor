package com.cohen.searchfordoctor

import android.location.Location
import com.cohen.searchfordoctor.data.DoctorsResult
import com.cohen.searchfordoctor.data.OauthResult
import io.reactivex.Observable


interface IWebService {

    fun getDoctors(string: String, location: Location): Observable<DoctorsResult>
    fun loginUser(username: String, password: String): Observable<OauthResult>

}
