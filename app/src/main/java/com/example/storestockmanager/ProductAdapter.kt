package com.example.storestockmanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.storestockmanager.R
import com.example.storestockmanager.data.Product

class ProductAdapter(
    private var products: List<Product> = emptyList()
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.textProductName)
        val category: TextView = itemView.findViewById(R.id.textCategory)
        val quantity: TextView = itemView.findViewById(R.id.textQuantity)
        val shelfLocation: TextView = itemView.findViewById(R.id.textShelfLocation)
        val price: TextView = itemView.findViewById(R.id.textPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.name.text = product.name
        holder.category.text = product.category
        holder.quantity.text = "${product.quantity} шт"
        holder.shelfLocation.text = "Полка: ${product.shelfLocation}"
        holder.price.text = "${product.price} руб"
    }

    override fun getItemCount(): Int = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}