package com.example.storestockmanager.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.storestockmanager.R
import com.example.storestockmanager.data.Categories
import com.example.storestockmanager.data.Product

class AddProductActivity : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var editQuantity: EditText
    private lateinit var editPrice: EditText
    private lateinit var editShelfLocation: EditText
    private lateinit var editMinStockLevel: EditText

    private var productId: Int = 0
    private var selectedCategory: String = Categories.allCategories[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        initViews()
        setupCategorySpinner()
        setupButtons()

        val isEditMode = intent.getBooleanExtra("edit_mode", false)
        if (isEditMode) {
            supportActionBar?.title = "Редактировать товар"
            loadProductData()
        }
    }

    private fun initViews() {
        editName = findViewById(R.id.editName)
        spinnerCategory = findViewById(R.id.spinnerCategory)  // ← ID должен совпадать с XML
        editQuantity = findViewById(R.id.editQuantity)
        editPrice = findViewById(R.id.editPrice)
        editShelfLocation = findViewById(R.id.editShelfLocation)
        editMinStockLevel = findViewById(R.id.editMinStockLevel)

        editMinStockLevel.setText("5")
    }

    private fun setupCategorySpinner() {
        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            Categories.allCategories
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        spinnerCategory.setPopupBackgroundResource(R.color.background_white)

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = Categories.allCategories[position]

                (view as? TextView)?.setTextColor(getColor(R.color.text_dark))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedCategory = Categories.allCategories[0]
            }
        }

        if (selectedCategory.isNotEmpty()) {
            val position = Categories.allCategories.indexOf(selectedCategory)
            spinnerCategory.setSelection(if (position >= 0) position else 0)
        }
    }
    private fun loadProductData() {
        productId = intent.getIntExtra("product_id", 0)
        editName.setText(intent.getStringExtra("product_name") ?: "")

        val savedCategory = intent.getStringExtra("product_category") ?: Categories.allCategories[0]
        val categoryPosition = Categories.allCategories.indexOf(savedCategory)
        spinnerCategory.setSelection(if (categoryPosition >= 0) categoryPosition else 0)
        selectedCategory = savedCategory

        editQuantity.setText((intent.getIntExtra("product_quantity", 0)).toString())
        editPrice.setText((intent.getDoubleExtra("product_price", 0.0)).toString())
        editShelfLocation.setText(intent.getStringExtra("product_shelf") ?: "")
        editMinStockLevel.setText((intent.getIntExtra("product_min_stock", 5)).toString())
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
            selectedCategory.isEmpty() -> {
                showError("Выберите категорию")
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
        val quantity = editQuantity.text.toString().toIntOrNull() ?: 0
        val price = editPrice.text.toString().toDoubleOrNull() ?: 0.0
        val shelfLocation = editShelfLocation.text.toString().trim()
        val minStockLevel = editMinStockLevel.text.toString().toIntOrNull() ?: 5

        if (name.isEmpty() || selectedCategory.isEmpty() || quantity == 0 || price == 0.0) {
            Toast.makeText(this, "Заполните обязательные поля", Toast.LENGTH_SHORT).show()
            return
        }

        val resultIntent = Intent().apply {
            putExtra("product_id", productId)
            putExtra("product_name", name)
            putExtra("product_category", selectedCategory)
            putExtra("product_quantity", quantity)
            putExtra("product_price", price)
            putExtra("product_shelf", shelfLocation)
            putExtra("product_min_stock", minStockLevel)
        }

        setResult(RESULT_OK, resultIntent)
        finish()
    }
}