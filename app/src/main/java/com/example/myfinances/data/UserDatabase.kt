package com.example.myfinances.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myfinances.data.dao.UserDAO
import com.example.myfinances.data.entities.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
}