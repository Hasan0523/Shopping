package com.example.shopping.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shopping.R
import com.example.shopping.model.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.roundToInt

class ProductsAdapter(var products:List<Product>, private val context: Context, private val productPressed: ProductPressed) : RecyclerView.Adapter<ProductsAdapter.MyHolder>() {
    class MyHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image : ImageView = itemView.findViewById(R.id.shop_item_image)
        val title : TextView = itemView.findViewById(R.id.shop_item_title)
        val brand : TextView = itemView.findViewById(R.id.shop_item_brand)
        val price : TextView = itemView.findViewById(R.id.shop_item_price)
        val rating : TextView = itemView.findViewById(R.id.shop_item_reyting)
        val cart : FloatingActionButton = itemView.findViewById(R.id.shop_item_cart_fab)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(parent.context).inflate(R.layout.shop_item, parent, false))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val product = products[position]
        holder.image.load(product.images[0])
        holder.brand.text = product.brand
        holder.rating.text = ((product.rating*10).roundToInt().toDouble()/10).toString()
        holder.title.text = product.title
        holder.price.text = product.price.toString() + " $"

        holder.itemView.setOnClickListener {
            productPressed.onPressed(product)
        }

        holder.cart.setOnClickListener {

        }

    }

    interface ProductPressed{
        fun onPressed(product: Product)
    }


}