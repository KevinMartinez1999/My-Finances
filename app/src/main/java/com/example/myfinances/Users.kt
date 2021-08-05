package com.example.myfinances

import java.io.Serializable

class Users(
    var nickname: String? = null,
    var email: String? = null,
    var password: String? = null
) : Serializable
