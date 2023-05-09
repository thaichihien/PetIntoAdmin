package com.mobye.petintoadmin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.ItemPetBinding
import com.mobye.petintoadmin.databinding.ItemProductBinding
import com.mobye.petintoadmin.models.Pet
import com.mobye.petintoadmin.utils.Utils

class PetPagingAdapter(private val detailListener :(Pet) -> Unit    // dùng để bắt sự kiện nhấn vào item -> mở ra chi tiết
)
    : PagingDataAdapter<Pet, PetPagingAdapter.PetViewHolder>(DiffUtilCallBack()) {

    //copy y chang thay đổi kiểu biến cho phù hợp
    private lateinit var binding : ItemPetBinding
    class DiffUtilCallBack : DiffUtil.ItemCallback<Pet>(){
        override fun areItemsTheSame(oldItem: Pet, newItem: Pet): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Pet, newItem: Pet): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.id == newItem.id
        }

    }

    //lớp ViewHolder dùng để set giá trị từ item vào UI
    inner class  PetViewHolder() : RecyclerView.ViewHolder(binding.root) {
        fun setData(item : Pet){
            binding.apply {
                petName.text = item.name
                petPrice.text = Utils.formatMoneyVND(item.price)
                petType.text = item.type
                petGender.text = item.gender
                Glide.with(binding.root)
                    .load(item.image)
                    .placeholder(R.drawable.logo)
                    .into(petImg)
                petName.setOnClickListener {
                    detailListener(item)
                }
                petImg.setOnClickListener {
                    detailListener(item)
                }

            }
        }

    }


    //hàm lấy dữ liệu để truyền vào UI
    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val Pet = getItem(position)
        Pet?.let {
            holder.setData(getItem(position)!!)
            holder.setIsRecyclable(false)
        }

    }


    //hàm tạo view holder, thay đổi kiểu biến cho phù hợp
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        binding = ItemPetBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return PetViewHolder()
    }
}
