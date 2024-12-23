package com.example.researcherpool.ui.Participation

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
import com.example.researcherpool.api.response.CountItem
import com.example.researcherpool.api.response.ParticipateItem
import com.example.researcherpool.api.response.ResultItem
import com.example.researcherpool.databinding.FragmentParticipationBinding
import com.example.researcherpool.preference.DataStoreViewModel
import com.example.researcherpool.preference.UserPreference
import com.example.researcherpool.preference.dataStore
import com.example.researcherpool.ui.MyForm.FormAdapter

class ParticipationFragment : Fragment() {

    private var _binding: FragmentParticipationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val participationViewModel =
            ViewModelProvider(this).get(ParticipationViewModel::class.java)

        _binding = FragmentParticipationBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = UserPreference.getInstance(requireContext().applicationContext.dataStore)
        val dataStoreViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            DataStoreViewModel::class.java)

        val participantViewModel = ViewModelProvider(this).get(ParticipationViewModel::class.java)

        dataStoreViewModel.getEmail().observe(viewLifecycleOwner){
            participantViewModel.getPart(it)
            participantViewModel.getCount(it)
        }

        participantViewModel.isError.observe(viewLifecycleOwner){err->
            if(err){
                participantViewModel.Msge.observe(viewLifecycleOwner){msg->
                    if(msg == "isEmpty"){
                        showErrorDialog("you havent participate")
                    }else{
                        showErrorDialog(msg)
                    }
                }
            }else{
                participantViewModel.MyParticipation.observe(viewLifecycleOwner){list->
                    if (list != null) {
                        showRecyclerList(list)
                    }
                }
            }
        }

        participantViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }


        participantViewModel.isErrorC.observe(viewLifecycleOwner){err->
            if(err){
                participantViewModel.MsgC.observe(viewLifecycleOwner){msg->
                    showErrorDialog(msg)
                }
            }else{
                participantViewModel.listCount.observe(viewLifecycleOwner){list->
                    list?.get(0)?.let { bindcount(it) }
                }
            }
        }

    }

    fun showRecyclerList(list: List<ParticipateItem>){
        binding.rvMyform.layoutManager = LinearLayoutManager(requireContext())
        val listFomAdapter = ParticipantAdapter()
        listFomAdapter.submitList(list)
        binding.rvMyform.adapter =listFomAdapter
    }

    private fun showLoading(isLoading:Boolean){
        if(isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun bindcount(list: CountItem){
        binding.txtApproved.text = "${list.approved} Approved"
        binding.txtPending.text = "${list.pending} Pending"
        binding.txtDecline.text = "${list.decline} Decline"
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