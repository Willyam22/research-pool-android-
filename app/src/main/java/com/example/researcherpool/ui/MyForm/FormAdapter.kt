package com.example.researcherpool.ui.MyForm

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.researcherpool.R
import com.example.researcherpool.api.response.ResultItem
import com.example.researcherpool.databinding.ItemRowLinkBinding
import com.example.researcherpool.ui.UpdateForm.UpdatePostActivity
import java.time.ZonedDateTime
import kotlin.coroutines.coroutineContext

class FormAdapter: ListAdapter<ResultItem, FormAdapter.ListViewHolder>(DIFF_CALLBACK) {

    class ListViewHolder(val binding:ItemRowLinkBinding):RecyclerView.ViewHolder(binding.root){
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(formr: ResultItem?) {
            binding.txtUser.text ="${formr?.username}"
            binding.txtTitle.text = "${formr?.title}"
            val dateString = formr?.createdAt
            val zoneDateTime = ZonedDateTime.parse(dateString)
            val formattedDate = zoneDateTime.toLocalDate().toString()
            binding.txtDate.text = formattedDate
            binding.txtParticipant.text = "${formr?.participantCount}"
            when (formr?.status?.uppercase()) {
                "OPEN" -> {
                    binding.txtOpen.text = formr.status
                    binding.openbanner.setBackgroundResource(R.drawable.custom_open)
                }
                "CLOSED" -> {
                    binding.txtOpen.text = formr.status
                    binding.openbanner.setBackgroundResource(R.drawable.custom_close)
                }
                else -> {
                    binding.txtOpen.text = formr?.status
                    binding.openbanner.setBackgroundResource(R.drawable.custom_open) // default
                }
            }
            itemView.setOnClickListener {
                val intentUpdate = Intent(itemView.context, UpdatePostActivity::class.java)
                intentUpdate.putExtra(UpdatePostActivity.ID, "${formr?.id}")
                itemView.context.startActivity(intentUpdate)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val formr = getItem(position)
        holder.bind(formr)
    }

    companion object{
        const val TAG ="FormAdapter"
        val DIFF_CALLBACK = object:DiffUtil.ItemCallback<ResultItem>(){
            override fun areItemsTheSame(
                oldItem: ResultItem,
                newItem: ResultItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ResultItem,
                newItem: ResultItem): Boolean {
                return oldItem == newItem
            }
        }
    }


}

