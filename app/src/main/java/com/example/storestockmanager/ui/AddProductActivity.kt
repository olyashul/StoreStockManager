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
        val name = editName.text.toString().trim()
        val category = editCategory.text.toString().trim()
        val quantity = editQuantity.text.toString().trim().toIntOrNull() ?: 0
        val price = editPrice.text.toString().trim().toDoubleOrNull() ?: 0.0
        val shelfLocation = editShelfLocation.text.toString().trim()
        val minStockLevel = editMinStockLevel.text.toString().trim().toIntOrNull() ?: 5

        if (name.isEmpty() || category.isEmpty() || quantity == 0 || price == 0.0) {
            Toast.makeText(this, "Заполните обязательные поля", Toast.LENGTH_SHORT).show()
            return
        }

        val resultIntent = Intent().apply {
            putExtra("product_name", name)
            putExtra("product_category", category)
            putExtra("product_quantity", quantity)
            putExtra("product_price", price)
            putExtra("product_shelf", shelfLocation)
            putExtra("product_min_stock", minStockLevel)
        }

        setResult(RESULT_OK, resultIntent)
        finish()
    }
}