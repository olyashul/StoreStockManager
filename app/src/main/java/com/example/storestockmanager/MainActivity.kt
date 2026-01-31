package com.example.storestockmanager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storestockmanager.data.Product
import com.example.storestockmanager.ui.AddProductActivity
import com.example.storestockmanager.ui.ProductAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var fabAdd: FloatingActionButton

    companion object {
        private const val REQUEST_ADD_PRODUCT = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
        setupFloatingButton()
        loadTestData()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter()
        recyclerView.adapter = adapter
    }

    private fun setupFloatingButton() {
        fabAdd = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD_PRODUCT)
        }
    }

    private fun loadTestData() {
        val testProducts = listOf(
            Product(
                name = "Молоко Простоквашино",
                category = "Молочные продукты",
                quantity = 15,
                price = 89.0,
                shelfLocation = "Холодильник А-3"
            ),
            Product(
                name = "Хлеб Бородинский",
                category = "Хлебобулочные изделия",
                quantity = 8,
                price = 45.0,
                shelfLocation = "Стол Б-2"
            ),
            Product(
                name = "Яйца куриные",
                category = "Яйца",
                quantity = 5,
                price = 120.0,
                shelfLocation = "Холодильник А-1",
                minStockLevel = 10
            ),
            Product(
                name = "Сахар",
                category = "Бакалея",
                quantity = 12,
                price = 65.0,
                shelfLocation = "Стол В-4"
            ),
            Product(
                name = "Кофе Jacobs",
                category = "Напитки",
                quantity = 3,
                price = 350.0,
                shelfLocation = "Стол А-5",
                minStockLevel = 5
            )
        )
        adapter.updateProducts(testProducts)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ADD_PRODUCT && resultCode == RESULT_OK && data != null) {
            val name = data.getStringExtra("product_name") ?: ""
            val category = data.getStringExtra("product_category") ?: ""
            val quantity = data.getIntExtra("product_quantity", 0)
            val price = data.getDoubleExtra("product_price", 0.0)
            val shelfLocation = data.getStringExtra("product_shelf") ?: ""
            val minStockLevel = data.getIntExtra("product_min_stock", 5)

            val newProduct = Product(
                name = name,
                category = category,
                quantity = quantity,
                price = price,
                shelfLocation = shelfLocation,
                minStockLevel = minStockLevel
            )

            val currentProducts = adapter.getCurrentList() + newProduct
            adapter.updateProducts(currentProducts)
            recyclerView.smoothScrollToPosition(currentProducts.size - 1)
        }
    }

    private fun addTestProduct() {
        val newProduct = Product(
            name = "Новый товар",
            category = "Разное",
            quantity = 10,
            price = 100.0,
            shelfLocation = "Полка Н-1"
        )
        val currentProducts = adapter.getCurrentList() + newProduct
        adapter.updateProducts(currentProducts)
        recyclerView.smoothScrollToPosition(currentProducts.size - 1)
    }
}