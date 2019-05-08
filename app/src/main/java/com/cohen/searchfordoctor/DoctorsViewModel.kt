package com.cohen.search_doctors

import android.app.Activity
import android.location.Location
import androidx.lifecycle.*
import com.cohen.searchfordoctor.IWebService
import com.cohen.searchfordoctor.WebService
import com.cohen.searchfordoctor.data.Doctor
import com.cohen.searchfordoctor.data.DoctorsResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException


class DoctorsViewModel : ViewModel() {

    lateinit var userViewModel: UserViewModel

    private var items = MutableLiveData<ArrayList<Doctor>>()
    private var searchString = MutableLiveData<Pair<String, Location>>()
    lateinit var webService: IWebService

    fun init(activity: Activity, userViewModel: UserViewModel) {
        this.userViewModel = userViewModel
        webService = WebService(activity, this.userViewModel)
        searchString.observe(activity as LifecycleOwner, Observer {
            webService.getDoctors(it.first, it.second)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : io.reactivex.Observer<DoctorsResult> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(doctorsResult: DoctorsResult) {
                        items.postValue(doctorsResult.doctors)
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.code() == 401) {
                                userViewModel.logOutUser()
                            }
                        }
                    }

                    override fun onComplete() {

                    }
                })
        })
    }

    fun getItems(): LiveData<ArrayList<Doctor>> {
        return items
    }

    fun setSearch(string: String, location: Location) {
        searchString.postValue(Pair(string, location))
    }

}
