package com.mobye.petintoadmin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobye.petintoadmin.databinding.ItemNotificationBinding
import com.mobye.petintoadmin.models.Notification

class NotificationItemAdapter (
    private val removedListener: (Int) -> Unit,
    private val detailListener : (String) -> Unit
) : RecyclerView.Adapter<NotificationItemAdapter.NotificationViewHolder>()  {

    private lateinit var binding: ItemNotificationBinding

    private val differCallBack = object : DiffUtil.ItemCallback<Notification>(){
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this,differCallBack)


    inner class NotificationViewHolder : RecyclerView.ViewHolder(binding.root){
        fun setData(item: Notification){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return NotificationViewHolder()
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = differ.currentList[position]

        binding.apply {
            tvTitle.text = item.title
            tvContent.text = item.content
            tvDate.text = item.date
            btnDelete.setOnClickListener{
                //swipeLayout.close(true)
                swipeLayout.visibility = View.GONE
                removedListener(holder.absoluteAdapterPosition)
            }
            swipeLayout.setOnClickListener{
                detailListener(item.type)
            }

        }
        holder.setIsRecyclable(false)

    }
}