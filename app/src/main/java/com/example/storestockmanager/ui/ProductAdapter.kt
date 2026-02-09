package com.example.storestockmanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.storestockmanager.R
import com.example.storestockmanager.data.Product

class ProductAdapter(
    private var products: List<Product> = emptyList(),
    private val onItemClick: (Product) -> Unit = {},
    private val onItemLongClick: (Product) -> Unit = {},
    private val onQuantityChanged: (Product, newQuantity: Int) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.textProductName)
        val category: TextView = itemView.findViewById(R.id.textCategory)
        val quantity: TextView = itemView.findViewById(R.id.textQuantity)
        val shelfLocation: TextView = itemView.findViewById(R.id.textShelfLocation)
        val price: TextView = itemView.findViewById(R.id.textPrice)
        val statusCircle: ImageView = itemView.findViewById(R.id.imageStatus)
        val btnIncrease: ImageView = itemView.findViewById(R.id.btnIncrease)
        val btnDecrease: ImageView = itemView.findViewById(R.id.btnDecrease)
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
        holder.quantity.text = product.quantity.toString()
        holder.shelfLocation.text = "Полка: ${product.shelfLocation}"
        holder.price.text = "${product.price} руб"

        if (product.quantity < product.minStockLevel) {
            holder.quantity.setTextColor(holder.itemView.context.getColor(R.color.low_stock))
            holder.statusCircle.setImageResource(R.drawable.ic_circle_low)
        } else {
            holder.quantity.setTextColor(holder.itemView.context.getColor(R.color.normal_stock))
            holder.statusCircle.setImageResource(R.drawable.ic_circle_normal)
        }

        holder.btnIncrease.setOnClickListener {
            val newQuantity = product.quantity + 1
            onQuantityChanged(product, newQuantity)
        }

        holder.btnDecrease.setOnClickListener {
            if (product.quantity > 0) {
                val newQuantity = product.quantity - 1
                onQuantityChanged(product, newQuantity)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick(product)
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick(product)
            true
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    fun getCurrentList(): List<Product> = products
}