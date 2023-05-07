package com.mobye.petintoadmin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.ItemProductBinding
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.utils.Utils

class ProductItemAdapter : RecyclerView.Adapter<ProductItemAdapter.ProductViewHolder>()  {

    private lateinit var binding: ItemProductBinding
    private val differCallBack = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            //TEMP
            return oldItem.name == newItem.name
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
    var differ = AsyncListDiffer(this,differCallBack)

    inner class ProductViewHolder : ViewHolder(binding.root){
        fun setData(item : Product){
            binding.apply {
                tvName.text = item.name
                tvPrice.text = Utils.formatMoneyVND(item.price)
                tvTypePet.text = item.typePet
                tvStock.text = item.stock.toString()
                tvDetail.text = item.detail
                Glide.with(binding.root)
                    .load(item.image)
                    .placeholder(R.drawable.logo)
                    .into(ivProduct)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)

        return ProductViewHolder()
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.setData(product)
        holder.setIsRecyclable(false)
    }
}