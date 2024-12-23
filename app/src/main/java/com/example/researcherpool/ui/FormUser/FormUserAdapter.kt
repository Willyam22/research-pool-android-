package com.example.researcherpool.ui.FormUser

import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.researcherpool.api.response.UserItem
import com.example.researcherpool.databinding.ItemRowLinkBinding
import com.example.researcherpool.ui.DetailUser.DetailUserActivity
import com.example.researcherpool.ui.DetailUser.DetailUserViewModel
import com.example.researcherpool.ui.MyForm.FormAdapter
import java.time.ZonedDateTime

class FormUserAdapter: ListAdapter<UserItem, FormUserAdapter.ListViewHolder>(DIFF_CALLBACK) {

    class ListViewHolder(val binding:ItemRowLinkBinding):RecyclerView.ViewHolder(binding.root){
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(formr: UserItem){
            binding.txtTitle.text = formr.title
            binding.txtUser.text = formr.username
            val dateString = formr.createdAt
            val zonedDateTime = ZonedDateTime.parse(dateString)
            val formattedDate = zonedDateTime.toLocalDate().toString()

            binding.txtDate.text = formattedDate
            binding.txtOpen.text = formr.status

            binding.txtParticipant.text = formr.participantCount.toString()
            itemView.setOnClickListener {
                val intentUser = Intent(itemView.context, DetailUserActivity::class.java)
                intentUser.putExtra(DetailUserActivity.ID, "${formr.id}")
                itemView.context.startActivity(intentUser)
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
        const val TAG = "formuseradapter"
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<UserItem>(){
            override fun areItemsTheSame(
                oldItem: UserItem,
                newItem: UserItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: UserItem,
                newItem: UserItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}