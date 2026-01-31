package com.example.storestockmanager.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.storestockmanager.R
import com.example.storestockmanager.data.Product

class AddProductActivity : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editCategory: EditText
    private lateinit var editQuantity: EditText
    private lateinit var editPrice: EditText
    private lateinit var editShelfLocation: EditText
    private lateinit var editMinStockLevel: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        initViews()
        setupButtons()
    }

    private fun initViews() {
        editName = findViewById(R.id.editName)
        editCategory = findViewById(R.id.editCategory)
        editQuantity = findViewById(R.id.editQuantity)
        editPrice = findViewById(R.id.editPrice)
        editShelfLocation = findViewById(R.id.editShelfLocation)
        editMinStockLevel = findViewById(R.id.editMinStockLevel)

        // Устанавливаем значение по умолчанию для минимального остатка
        editMinStockLevel.setText("5")
    }

    private fun setupButtons() {
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        btnSave.setOnClickListener {
            if (validateInput()) {
                saveProduct()
            }
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun validateInput(): Boolean {
        return when {
            editName.text.toString().trim().isEmpty() -> {
                showError("Введите название товара")
                false
            }
            editCategory.text.toString().trim().isEmpty() -> {
                showError("Введите категорию")
                false
            }
            editQuantity.text.toString().trim().isEmpty() -> {
                showError("Введите количество")
                false
            }
            editPrice.text.toString().trim().isEmpty() -> {
                showError("Введите цену")
                false
            }
            else -> true
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveProduct() {
        val product = Product(
            name = editName.text.toString().trim(),
            category = editCategory.text.toString().trim(),
            quantity = editQuantity.text.toString().toIntOrNull() ?: 0,
            price = editPrice.text.toString().toDoubleOrNull() ?: 0.0,
            shelfLocation = editShelfLocation.text.toString().trim(),
            minStockLevel = editMinStockLevel.text.toString().toIntOrNull() ?: 5
        )

        // Создаем новый Intent для передачи данных
        val resultIntent = Intent().apply {
            putExtra("product_name", product.name)
            putExtra("product_category", product.category)
            putExtra("product_quantity", product.quantity)
            putExtra("product_price", product.price)
            putExtra("product_shelf", product.shelfLocation)
            putExtra("product_min_stock", product.minStockLevel)
        }

        setResult(RESULT_OK, resultIntent)
        finish()
    }
}