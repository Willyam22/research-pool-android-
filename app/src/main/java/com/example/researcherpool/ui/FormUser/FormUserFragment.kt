package com.example.researcherpool.ui.FormUser

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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
import com.example.researcherpool.api.response.ResultItem
import com.example.researcherpool.api.response.UserItem
import com.example.researcherpool.databinding.FragmentForumUserBinding
import com.example.researcherpool.preference.DataStoreViewModel
import com.example.researcherpool.preference.UserPreference
import com.example.researcherpool.preference.dataStore
import com.example.researcherpool.ui.MyForm.FormAdapter

class FormUserFragment : Fragment() {

    private var _binding: FragmentForumUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val formUserViewModel =
            ViewModelProvider(this).get(FormUserViewModel::class.java)

        _binding = FragmentForumUserBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val formUserViewModel = ViewModelProvider(this).get(FormUserViewModel::class.java)
        formUserViewModel.getUserList()
        formUserViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }




        formUserViewModel.isError.observe(viewLifecycleOwner){err->
            if(err){
                formUserViewModel.Msg.observe(viewLifecycleOwner){msg->
                    if (msg != null) {
                        showErrorDialog(msg)
                    }
                }
            }else{
                formUserViewModel.formUser.observe(viewLifecycleOwner){list->
                    Log.d(TAG, "$list")
                    if (list != null) {
                        showRecyclerList(list)
                    }
                }
            }
        }

        formUserViewModel.formUser.observe(viewLifecycleOwner){list->
            Log.d(TAG, "ini list: $list")
        }
    }

    private fun showLoading(isLoading:Boolean){
        if(isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerList(list: List<UserItem>){
        binding.rvMyform.layoutManager = LinearLayoutManager(requireContext())
        val listFomUserAdapter = FormUserAdapter()
        listFomUserAdapter.submitList(list)
        binding.rvMyform.adapter =listFomUserAdapter
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

    companion object{
        const val TAG = "formuser"
    }
}