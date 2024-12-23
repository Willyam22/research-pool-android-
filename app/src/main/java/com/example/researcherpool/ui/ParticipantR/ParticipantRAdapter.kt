package com.example.researcherpool.ui.ParticipantR

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.researcherpool.R
import com.example.researcherpool.api.response.PartR
import com.example.researcherpool.api.response.ParticipateItem
import com.example.researcherpool.databinding.ItemRowLinkBinding
import com.example.researcherpool.ui.Participation.ParticipantAdapter
import com.example.researcherpool.ui.Verification.VerificationActivity

class ParticipantRAdapter: ListAdapter<PartR, ParticipantRAdapter.ListViewHolder>(DIFF_CALLBACK) {

    class ListViewHolder(val binding: ItemRowLinkBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(part : PartR){
            binding.txtParticipant.visibility = View.GONE
            binding.imgParticipant.visibility = View.GONE
            binding.txtTitle.text  = part.title
            binding.txtUser.text = part.userUsername
            val stat = part.status
            binding.txtOpen.text = stat
            binding.txtDate.visibility = View.GONE
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

            itemView.setOnClickListener {
                val id = part.id
                val intentVerif = Intent(itemView.context, VerificationActivity::class.java)
                intentVerif.putExtra(VerificationActivity.ID, "$id")
                itemView.context.startActivity(intentVerif)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val part= getItem(position)
        holder.bind(part)
    }

    companion object{
        const val TAG = "partrAdapater"
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<PartR>(){
            override fun areItemsTheSame(
                oldItem: PartR,
                newItem: PartR
            ): Boolean {
                return oldItem ==  newItem
            }

            override fun areContentsTheSame(
                oldItem: PartR,
                newItem: PartR
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}