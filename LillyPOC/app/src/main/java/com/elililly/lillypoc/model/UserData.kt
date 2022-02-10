package com.elililly.lillypoc.model

data class UserData(
    val firstName: String = EMPTY,
    val lastName: String,
    val email: String,
    val address: String,
    val pincode: String
    )