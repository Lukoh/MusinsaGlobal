package com.goforer.musinsaglobaltest.di.module

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.goforer.base.extension.isNull
import com.goforer.base.utils.connect.ConnectionUtils
import com.goforer.musinsaGlobalTest.BuildConfig
import com.goforer.musinsaglobaltest.MusinsaGlobalTest
import com.goforer.musinsaglobaltest.data.source.network.NetworkError
import com.goforer.musinsaglobaltest.data.source.network.NetworkErrorHandler
import com.goforer.musinsaglobaltest.data.source.network.adapter.factory.FlowCallAdapterFactory
import com.goforer.musinsaglobaltest.data.source.network.adapter.factory.NullOnEmptyConverterFactory
import com.goforer.musinsaglobaltest.data.source.network.api.RestAPI
import com.goforer.musinsaglobaltest.presentation.Caller.preparingService
import com.goforer.musinsaglobaltest.presentation.Caller.runNotAvailableNetwork
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.orhanobut.logger.Logger
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * A module for Android-specific dependencies which require a [Context] or
 * [android.app.Application] to create.
 */
@Module
class AppModule {
    companion object {
        const val timeout_read = 60L
        const val timeout_connect = 60L
        const val timeout_write = 60L
    }

    @Provides
    @Singleton
    fun appContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideGSon(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideNetworkErrorHandler(context: Context) = NetworkErrorHandler(context)


    @Singleton
    @Provides
    fun providePersistentCookieJar(context: Context) =
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptor: Interceptor,
        cookieJar: PersistentCookieJar
    ): OkHttpClient {
        val ok = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .connectTimeout(timeout_connect, TimeUnit.SECONDS)
            .readTimeout(timeout_read, TimeUnit.SECONDS)
            .writeTimeout(timeout_write, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            if ("robolectric" != Build.FINGERPRINT)
                ok.addNetworkInterceptor(StethoInterceptor())

            val httpLoggingInterceptor =
                HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        if (isJSONValid(message))
                            Logger.json(message)
                        else
                            Timber.d("%s", message)
                    }

                    fun isJSONValid(jsonInString: String): Boolean {
                        try {
                            JSONObject(jsonInString)
                        } catch (ex: JSONException) {
                            try {
                                JSONArray(jsonInString)
                            } catch (ex1: JSONException) {
                                return false
                            }

                        }

                        return true
                    }
                })

            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            ok.addInterceptor(httpLoggingInterceptor)
        }

        ok.addInterceptor(interceptor)

        return ok.build()
    }

    @Provides
    @Singleton
    fun getRequestInterceptor(
        application: Application,
        context: Context
    ): Interceptor {
        return Interceptor {
            Timber.tag("PRETTY_LOGGER")

            if (!ConnectionUtils.isNetworkAvailable(context)) {
                if (application is MusinsaGlobalTest) {
                    application.activity?.let { activity ->
                        activity as AppCompatActivity
                        activity.lifecycleScope.launch {
                            runNotAvailableNetwork(context)
                        }
                    }
                }
            }

            val original = it.request()

            //Log.e("pretty", "Interceptor.url.host: ${original.url.host}")
            //Log.e("pretty", "Interceptor.url.path: ${original.url}")
            val requested = with(original) {
                val builder = newBuilder()

                builder.header("Accept", "application/json")
                //builder.header("Accept-Version", "v1")
                //builder.header("mobileplatform", "android")
                //Timber.d("mobileplatform: android")

                //builder.header("versioncode", "${BuildConfig.VERSION_CODE}")
                //Timber.d("versioncode: ${BuildConfig.VERSION_CODE}")

                //Timber.d("request cookies: ${cookieJar.loadForRequest(requested.url)}")
                builder.build()
            }

            val response = it.proceed(requested)
            val body = response.body
            var bodyStr = body.string()
            Timber.d("**http-num: ${response.code}")
            Timber.d("**http-body: $body")

            if (!response.isSuccessful) {
                try {
                    when (response.code) {
                        NetworkError.ERROR_SERVICE_BAD_GATEWAY, NetworkError.ERROR_SERVICE_UNAVAILABLE -> {
                            preparingService(context)
                        }

                        NetworkError.ERROR_SERVICE_UNPROCESSABLE_ENTITY -> {
                            val networkError =
                                Gson().fromJson(bodyStr, NetworkError::class.java)

                            networkError.isNull({

                            }, {
                                networkError.detail[0].msg =
                                    original.url.encodedPath + "\n" + networkError.detail[0].msg
                                bodyStr = Gson().toJson(networkError)
                            })
                        }

                        else -> {
                            val networkError =
                                Gson().fromJson(bodyStr, NetworkError::class.java)

                            networkError.isNull({

                            }, {
                                networkError.detail[0].msg =
                                    original.url.encodedPath + "\n" + networkError.detail[0].msg
                                bodyStr = Gson().toJson(networkError)
                            })
                        }
                    }
                } catch (e: Exception) {
                    e.stackTrace
                    Timber.e(Throwable(e.toString()))
                }
            }

            val builder = response.newBuilder()

            builder.body(bodyStr.toByteArray().toResponseBody(body.contentType())).build()
        }
    }

    @Singleton
    @Provides
    fun provideRestAPI(gson: Gson, okHttpClient: OkHttpClient): RestAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.apiServer)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(FlowCallAdapterFactory())
            .addConverterFactory(NullOnEmptyConverterFactory())
            .client(okHttpClient)
            .build()

        return retrofit.create(RestAPI::class.java)
    }

    @Singleton
    @Provides
    fun providesInAppUpdateManager(application: Application): AppUpdateManager =
        AppUpdateManagerFactory.create(application)
}