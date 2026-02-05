package com.example.storestockmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val category: String,
    val quantity: Int,
    val price: Double,
    val shelfLocation: String,
    val barcode: String? = null,
    val minStockLevel: Int = 5
)