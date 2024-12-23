package com.example.researcherpool.ui.MyForm

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.Guideline
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.researcherpool.R
import com.example.researcherpool.ViewModelFactory
import com.example.researcherpool.api.response.ResultItem

import com.example.researcherpool.databinding.FragmentMyformBinding
import com.example.researcherpool.preference.DataStoreViewModel
import com.example.researcherpool.preference.UserPreference
import com.example.researcherpool.preference.dataStore
import com.example.researcherpool.ui.PostForm.PostFormActivity

class MyFormFragment : Fragment() {

    private var _binding: FragmentMyformBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myFormViewModel =
            ViewModelProvider(this).get(MyFormViewModel::class.java)

        _binding = FragmentMyformBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myFormViewModel = ViewModelProvider(this).get(MyFormViewModel::class.java)
        val pref = UserPreference.getInstance(requireContext().applicationContext.dataStore)
        val dataStoreViewModel = ViewModelProvider(this,ViewModelFactory(pref)).get(DataStoreViewModel::class.java)
        dataStoreViewModel.getEmail().observe(viewLifecycleOwner){
            myFormViewModel.getFormlist(it)
        }

        myFormViewModel.isError.observe(viewLifecycleOwner){err->
            if(err){
                myFormViewModel.Msg.observe(viewLifecycleOwner){msg->
                    showErrorDialog(msg)
                }
            }else{
                myFormViewModel.formMyForm.observe(viewLifecycleOwner){list->
                    if (list != null) {
                        showRecyclerList(list)
                        Log.d(TAG, list.toString())
                        val totalitem = binding.rvMyform.adapter?.itemCount
                        Log.d(TAG, "total Item: $totalitem")
                    }
                }
            }
        }
        myFormViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }



        binding.fabInsert.setOnClickListener {
            val intentPost = Intent(requireContext(), PostFormActivity::class.java)
            startActivity(intentPost)
        }
    }

    companion object{
        const val TAG = "MyFormFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    fun showRecyclerList(list: List<ResultItem>){
        binding.rvMyform.layoutManager = LinearLayoutManager(requireContext())
        val listFomAdapter = FormAdapter()
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


}