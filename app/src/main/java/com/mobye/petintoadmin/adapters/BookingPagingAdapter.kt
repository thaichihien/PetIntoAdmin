package com.mobye.petintoadmin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.ItemBookingBinding
import com.mobye.petintoadmin.databinding.ItemProductBinding
import com.mobye.petintoadmin.models.Booking
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.utils.Utils

class BookingPagingAdapter(
    private val detailListener :(Booking) -> Unit)
    : PagingDataAdapter<Booking, BookingPagingAdapter.BookingViewHolder>(DiffUtilCallBack()) {
    //copy y chang thay đổi kiểu biến cho phù hợp
    private lateinit var binding : ItemBookingBinding
    class DiffUtilCallBack : DiffUtil.ItemCallback<Booking>(){
        override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem.id == newItem.id
        }

    }


    inner class  BookingViewHolder() : RecyclerView.ViewHolder(binding.root) {
        fun setData(item : Booking){
            binding.apply {
                tvBookingType.text = item.service
                tvBookingService.text = item.type
                tvBookingDate.text = Utils.formatToLocalDate(item.checkIn)
                tvCustomerInfo.text = "${item.customerName} | ${item.phone}"
                tvPetInformation.text = item.petName
                tvPetGenre.text = item.genre
                tvStatus.text = item.status

                layoutBooking.setOnClickListener {
                    detailListener(item)
                }
            }
        }

    }


    //hàm lấy dữ liệu để truyền vào UI
    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = getItem(position)
        booking?.let {
            holder.setData(getItem(position)!!)
            holder.setIsRecyclable(false)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        binding = ItemBookingBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return BookingViewHolder()
    }
}