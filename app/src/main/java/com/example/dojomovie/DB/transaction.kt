package com.example.dojomovie.DB


data class transaction(
    val transaction_id :Int,
    val user_id: String,
    val film_id: String,
    val quantity: Int
)

