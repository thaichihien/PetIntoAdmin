package com.mobye.petintoadmin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.ItemProductBinding
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.utils.Utils

class ProductPagingAdapter(
    private val detailListener :(Product) -> Unit    // dùng để bắt sự kiện nhấn vào item -> mở ra chi tiết
)
    : PagingDataAdapter<Product,ProductPagingAdapter.ProductViewHolder>(DiffUtilCallBack()) {

    //copy y chang thay đổi kiểu biến cho phù hợp
    private lateinit var binding : ItemProductBinding
    class DiffUtilCallBack : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.id == newItem.id
        }

    }

    //lớp ViewHolder dùng để set giá trị từ item vào UI
    inner class  ProductViewHolder() : RecyclerView.ViewHolder(binding.root) {
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
                tvName.setOnClickListener {
                    detailListener(item)
                }
                ivProduct.setOnClickListener {
                    detailListener(item)
                }

            }
        }

    }


    //hàm lấy dữ liệu để truyền vào UI
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        product?.let {
            holder.setData(getItem(position)!!)
            holder.setIsRecyclable(false)
        }

    }

    //hàm tạo view holder, thay đổi kiểu biến cho phù hợp
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ProductViewHolder()
    }
}