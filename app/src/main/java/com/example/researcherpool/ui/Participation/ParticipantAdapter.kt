package com.example.researcherpool.ui.Participation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.researcherpool.R
import com.example.researcherpool.api.response.ParticipateItem
import com.example.researcherpool.databinding.ItemRowLinkBinding

class ParticipantAdapter: ListAdapter<ParticipateItem, ParticipantAdapter.ListViewHolder>(DIFF_CALLBACK) {

    class ListViewHolder(val binding: ItemRowLinkBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(part : ParticipateItem){
            binding.txtParticipant.visibility = View.GONE
            binding.imgParticipant.visibility = View.GONE
            binding.txtTitle.text  = part.title
            binding.txtUser.text = part.username
            val stat = part.participantStatus
            binding.txtOpen.text = stat
            when(stat?.uppercase()){
                "APPROVED" ->{
                    binding.openbanner.setBackgroundResource(R.drawable.custom_open)
                }
                "DECLINE" ->{
                    binding.openbanner.setBackgroundResource(R.drawable.custom_close)
                }
                else ->{
                    binding.openbanner.setBackgroundResource(R.drawable.custom_pending)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =ItemRowLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val part= getItem(position)
        holder.bind(part)
    }




    companion object{
        const val TAG = "partAdapater"
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<ParticipateItem>(){
            override fun areItemsTheSame(
                oldItem: ParticipateItem,
                newItem: ParticipateItem
            ): Boolean {
                return oldItem ==  newItem
            }

            override fun areContentsTheSame(
                oldItem: ParticipateItem,
                newItem: ParticipateItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }


}