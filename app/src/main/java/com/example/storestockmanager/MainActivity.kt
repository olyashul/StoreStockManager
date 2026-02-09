package com.example.storestockmanager

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
    private lateinit var searchView: SearchView

    companion object {
        private const val REQUEST_ADD_PRODUCT = 1001
        private const val REQUEST_EDIT_PRODUCT = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val productDao = ProductDatabase.getDatabase(this).productDao()
        repository = ProductRepository(productDao)

        setupSearchView()
        setupRecyclerView()
        setupFloatingButton()
        loadProductsFromDatabase()
    }

    private fun openEditProductActivity(product: Product) {
        val intent = Intent(this, com.example.storestockmanager.ui.AddProductActivity::class.java).apply {
            putExtra("edit_mode", true)
            putExtra("product_id", product.id)
            putExtra("product_name", product.name)
            putExtra("product_category", product.category)
            putExtra("product_quantity", product.quantity)
            putExtra("product_price", product.price)
            putExtra("product_shelf", product.shelfLocation)
            putExtra("product_min_stock", product.minStockLevel)
        }
        startActivityForResult(intent, REQUEST_EDIT_PRODUCT)
    }

    private fun setupSearchView() {
        searchView = findViewById(R.id.searchView)

        // Настраиваем внешний вид SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    loadProductsFromDatabase()
                } else {
                    searchProducts(newText)
                }
                return true
            }
        })

        val searchEditText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText?.background = null
    }

    private fun searchProducts(query: String) {
        lifecycleScope.launch {
            repository.searchProducts(query).collect { products ->
                adapter.updateProducts(products)
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProductAdapter(
            onItemClick = { product ->
                openEditProductActivity(product)
            },
            onItemLongClick = { product ->
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
            },
            onQuantityChanged = { product, newQuantity ->
                lifecycleScope.launch {
                    val updatedProduct = product.copy(quantity = newQuantity)
                    repository.insert(updatedProduct)
                }
            }
        )

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

        if (resultCode == RESULT_OK && data != null) {
            val name = data.getStringExtra("product_name") ?: ""
            val category = data.getStringExtra("product_category") ?: ""
            val quantity = data.getIntExtra("product_quantity", 0)
            val price = data.getDoubleExtra("product_price", 0.0)
            val shelfLocation = data.getStringExtra("product_shelf") ?: ""
            val minStockLevel = data.getIntExtra("product_min_stock", 5)

            val productId = data.getIntExtra("product_id", 0)

            val product = Product(
                id = productId,
                name = name,
                category = category,
                quantity = quantity,
                price = price,
                shelfLocation = shelfLocation,
                minStockLevel = minStockLevel
            )

            lifecycleScope.launch {
                repository.insert(product)
                val message = if (productId > 0) "Товар обновлен" else "Товар добавлен"
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}