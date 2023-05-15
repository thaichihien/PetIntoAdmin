package com.mobye.petintoadmin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobye.petintoadmin.databinding.ItemOrderBinding
import com.mobye.petintoadmin.models.Order
import com.mobye.petintoadmin.utils.Utils

class OrderPagingAdapter(
    private val detailListener :(Order) -> Unit
)
    : PagingDataAdapter<Order, OrderPagingAdapter.OrderViewHolder>(DiffUtilCallBack()) {

    private lateinit var binding : ItemOrderBinding
    class DiffUtilCallBack : DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }


    }


    inner class  OrderViewHolder() : RecyclerView.ViewHolder(binding.root) {
        fun setData(item : Order){
            binding.apply {
                tvId.text = item.id
                tvTotal.text = Utils.formatMoneyVND(item.total)
                tvAmount.text = item.amount.toString()
                tvCustomerName.text = item.customerName
                tvStatus.text = item.status
                tvOrderDate.text = Utils.formatToLocalDate(item.orderDate)
                itemLayout.setOnClickListener{
                    detailListener(item)
                }
                if(item.petId.isNotBlank()){
                    tvTotal.text = "1"
                    layoutAmount.visibility = View.GONE
                }
            }
        }
    }


    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = getItem(position)
        order?.let {
            holder.setData(getItem(position)!!)
            holder.setIsRecyclable(false)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return OrderViewHolder()
    }
}