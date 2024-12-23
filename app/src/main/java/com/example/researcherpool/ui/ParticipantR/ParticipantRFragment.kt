package com.example.researcherpool.ui.ParticipantR

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.researcherpool.R
import com.example.researcherpool.ViewModelFactory
import com.example.researcherpool.api.response.PartR
import com.example.researcherpool.api.response.ParticipateItem
import com.example.researcherpool.databinding.FragmentParticipantrBinding
import com.example.researcherpool.preference.DataStoreViewModel
import com.example.researcherpool.preference.UserPreference
import com.example.researcherpool.preference.dataStore
import com.example.researcherpool.ui.Participation.ParticipantAdapter

class ParticipantRFragment : Fragment() {

    private var _binding: FragmentParticipantrBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val participantRViewModel =
            ViewModelProvider(this).get(ParticipantRViewModel::class.java)

        _binding = FragmentParticipantrBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = UserPreference.getInstance(requireContext().applicationContext.dataStore)
        val dataStoreViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            DataStoreViewModel::class.java)

        val participantRViewModel =
            ViewModelProvider(this).get(ParticipantRViewModel::class.java)

        dataStoreViewModel.getEmail().observe(viewLifecycleOwner){
            participantRViewModel.getMyPartR(it)
        }

        participantRViewModel.isError.observe(viewLifecycleOwner){err->
            if(err){
                participantRViewModel.Msg.observe(viewLifecycleOwner){msg->
                    if(msg == "response is null"){
                        binding.txtEmpty.visibility = View.VISIBLE
                        binding.rvMyform.visibility = View.GONE
                    }else{
                        showErrorDialog(msg)
                    }
                }
            }else{
                participantRViewModel.myPartR.observe(viewLifecycleOwner){list->
                    if (list != null) {
                        showRecyclerList(list)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading:Boolean){
        if(isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    fun showRecyclerList(list: List<PartR>){
        binding.rvMyform.layoutManager = LinearLayoutManager(requireContext())
        val listFomAdapter = ParticipantRAdapter()
        listFomAdapter.submitList(list)
        binding.rvMyform.adapter =listFomAdapter
    }

    private fun showErrorDialog(msg:String){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_warning)
        dialog.setCancelable(false )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnOK : Button =dialog.findViewById(R.id.btn_ok_err)
        val txterr : TextView = dialog.findViewById(R.id.txt_warning)

        txterr.text = msg

        btnOK.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}