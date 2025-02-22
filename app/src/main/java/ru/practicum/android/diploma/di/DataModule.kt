package ru.practicum.android.diploma.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.database.AppDatabase
import ru.practicum.android.diploma.data.network.HHApi
import ru.practicum.android.diploma.data.network.HHBASEURL
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient

private const val MYCOOLOFFER_PREFERENCES = "mycooloffer_preferences"

val dataModule = module {
    single<HHApi> {
        Retrofit.Builder()
            .baseUrl(HHBASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HHApi::class.java)
    }

    single {
        androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as (ConnectivityManager)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }

    single { Gson() }

    single {
        androidContext().getSharedPreferences(MYCOOLOFFER_PREFERENCES, Context.MODE_PRIVATE)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}
