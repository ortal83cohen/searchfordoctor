package com.cohen.search_doctors

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cohen.searchfordoctor.IWebService
import com.cohen.searchfordoctor.WebService
import com.cohen.searchfordoctor.data.OauthResult
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class UserViewModel : ViewModel() {
    lateinit var webService: IWebService

    private var user = MutableLiveData<String?>()


    fun init(appCompatActivity: AppCompatActivity) {
        webService = WebService(appCompatActivity, this)
        user.postValue(null) // load from cache, db or session manager..
    }

    fun getUser(): LiveData<String?> {
        return user
    }

    fun logOutUser() {
        user.postValue(null)
    }

    fun loginUser() {
        webService.loginUser("androidChallenge@vivy.com", "88888888")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<OauthResult> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(oauthResult: OauthResult) {
                    user.postValue(oauthResult.accessToken)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }
            })
    }

}
