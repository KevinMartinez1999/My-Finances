package com.example.myfinances.data.dao

import androidx.room.*
import com.example.myfinances.data.entities.User

@Dao
interface UserDAO {
    @Insert
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT*FROM tabla_usuario where email LIKE:email")
    fun searchUser(email: String): User

    @Update
    fun updateUser(user: User)
}