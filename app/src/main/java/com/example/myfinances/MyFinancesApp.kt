package com.example.myfinances

import android.app.Application
import androidx.room.Room
import com.example.myfinances.data.UserDatabase

class MyFinancesApp: Application() {
    companion object{
        lateinit var database: UserDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            this,
            UserDatabase::class.java,
            "user_db"
        ).allowMainThreadQueries()
            .build()
    }
}