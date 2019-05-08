package com.cohen.searchfordoctor


import android.content.Context
import android.location.Location
import com.cohen.search_doctors.UserViewModel
import com.cohen.searchfordoctor.data.DoctorsResult
import com.cohen.searchfordoctor.data.OauthResult
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException
import java.util.concurrent.TimeUnit


class WebService(
    private val mContext: Context,
    private val userViewModel: UserViewModel
) : IWebService {
    override fun loginUser(username: String, password: String): Observable<OauthResult> {
        return getAuth()!!.login("password", username, password)
    }

    override fun getDoctors(search: String, location: Location): Observable<DoctorsResult> {
        return getApi(userViewModel.getUser().value)!!.getDoctors(
//            search=search,lat="52.534709",lng="13.3976972")
            search, location.altitude.toString(), location.longitude.toString())

    }


    private fun getAuth(): IServiceAuth? {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
        client.connectTimeout(10, TimeUnit.SECONDS)
        client.writeTimeout(25, TimeUnit.SECONDS)
        client.readTimeout(25, TimeUnit.SECONDS)

        client.interceptors().add(ExtraHeadersInterceptor())
        client.interceptors().add(BasicHeadersInterceptor())
        client.interceptors().add(loggingInterceptor)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://auth.staging.vivy.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client.build())
            .build()

        return retrofit.create(IServiceAuth::class.java)
    }

    private fun getApi(
        userServiceToken: String? = null
    ): IServiceApi? {


        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
        client.connectTimeout(10, TimeUnit.SECONDS)
        client.writeTimeout(25, TimeUnit.SECONDS)
        client.readTimeout(25, TimeUnit.SECONDS)

        client.interceptors().add(loggingInterceptor)
        client.interceptors().add(ExtraHeadersInterceptor())
        if (userServiceToken != null) {
            client.interceptors().add(UserHeadersInterceptor(userServiceToken))
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.staging.vivy.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client.build())
            .build()

        return retrofit.create(IServiceApi::class.java)
    }

    inner class UserHeadersInterceptor(private val userServiceToken: String) : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request()
                .newBuilder()

            builder.addHeader("Authorization","Bearer $userServiceToken")

            return chain.proceed(builder.build())
        }
    }


    inner class ExtraHeadersInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request()
                .newBuilder()
            builder.addHeader("content-type", "application/x-www-form-urlencoded")
            builder.addHeader("Accept", "application/json")
            return chain.proceed(builder.build())
        }
    }

    inner class BasicHeadersInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request()
                .newBuilder()

            builder.addHeader("authorization", "Basic aXBob25lOmlwaG9uZXdpbGxub3RiZXRoZXJlYW55bW9yZQ==")

            return chain.proceed(builder.build())
        }
    }

    interface IServiceAuth {
        @POST("/oauth/token")
        @FormUrlEncoded
        fun login(@Query("grant_type") grant_type: String, @Field("username") username: String, @Field("password") password: String): Observable<OauthResult>
    }

    interface IServiceApi {

        @GET("/api/users/me/doctors")
        fun getDoctors(@Query("search") search: String, @Query("lat") lat: String, @Query("lng") lng: String): Observable<DoctorsResult>
    }
}
