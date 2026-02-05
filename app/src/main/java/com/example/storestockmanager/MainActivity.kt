package com.example.storestockmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storestockmanager.data.Product
import com.example.storestockmanager.data.ProductDatabase
import com.example.storestockmanager.data.ProductRepository
import com.example.storestockmanager.ui.ProductAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var repository: ProductRepository

    companion object {
        private const val REQUEST_ADD_PRODUCT = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация базы данных
        val productDao = ProductDatabase.getDatabase(this).productDao()
        repository = ProductRepository(productDao)

        setupRecyclerView()
        setupFloatingButton()
        loadProductsFromDatabase()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProductAdapter(onItemLongClick = { product ->
            android.app.AlertDialog.Builder(this)
                .setTitle("Удалить товар")
                .setMessage("Удалить \"${product.name}\"?")
                .setPositiveButton("Удалить") { _, _ ->
                    lifecycleScope.launch {
                        repository.delete(product)
                        Toast.makeText(this@MainActivity, "Товар удален", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Отмена", null)
                .show()
        })

        recyclerView.adapter = adapter
    }

    private fun setupFloatingButton() {
        fabAdd = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            val intent = Intent(this, com.example.storestockmanager.ui.AddProductActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD_PRODUCT)
        }
    }

    private fun loadProductsFromDatabase() {
        lifecycleScope.launch {
            repository.allProducts.collect { products ->
                adapter.updateProducts(products)
            }
        }
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

            lifecycleScope.launch {
                repository.insert(newProduct)
            }
        }
    }
}