package com.example.shopping.model

data class ProductX(
    val discountPercentage: Double,
    val discountedPrice: Int,
    val id: Int,
    val price: Int,
    val quantity: Int,
    val title: String,
    val total: Int
)