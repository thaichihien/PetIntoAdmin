package com.mobye.petintoadmin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.util.Util
import com.mobye.petintoadmin.databinding.ItemProductBinding
import com.mobye.petintoadmin.databinding.ItemReportBinding
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.models.Report
import com.mobye.petintoadmin.utils.Utils

class ReportPagingAdapter(
    private val detailListener : (Report) -> Unit
) : PagingDataAdapter<Report,ReportPagingAdapter.ReportViewHolder>(DiffUtilCallBack()) {

    private lateinit var binding : ItemReportBinding
    class DiffUtilCallBack : DiffUtil.ItemCallback<Report>(){
        override fun areItemsTheSame(oldItem: Report, newItem: Report): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Report, newItem: Report): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.date == newItem.date
        }
    }


    inner class  ReportViewHolder() : RecyclerView.ViewHolder(binding.root) {
        fun setData(item : Report){
            binding.apply {
                tvCustomerName.text = item.name
                tvEmail.text = item.email
                tvComment.text = item.comment
                tvDate.text = Utils.formatToLocalDate(item.date)
                layoutReport.setOnClickListener {
                    detailListener(item)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = getItem(position)
        report?.let {
            holder.setData(getItem(position)!!)
            holder.setIsRecyclable(false)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        binding = ItemReportBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ReportViewHolder()
    }


}




