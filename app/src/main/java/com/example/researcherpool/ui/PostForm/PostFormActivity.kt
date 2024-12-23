package com.example.researcherpool.ui.PostForm

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.researcherpool.EntryActivity
import com.example.researcherpool.R
import com.example.researcherpool.ViewModelFactory
import com.example.researcherpool.databinding.ActivityPostFormBinding
import com.example.researcherpool.preference.DataStoreViewModel
import com.example.researcherpool.preference.UserPreference
import com.example.researcherpool.preference.dataStore

class PostFormActivity : AppCompatActivity() {
   private lateinit var binding : ActivityPostFormBinding
   private lateinit var postFormViewModel: PostFormViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostFormBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pref = UserPreference.getInstance(application.dataStore)
        val dataStoreViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            DataStoreViewModel::class.java)

        postFormViewModel = ViewModelProvider(this).get(PostFormViewModel::class.java)
        binding.btnPost.setOnClickListener {
            val Title = binding.edttitle.text.toString()
            val description = binding.edtDesc.text.toString()
            val link = binding.edtLink.text.toString()
            dataStoreViewModel.getEmail().observe(this){email->
                postFormViewModel.postForum(Title, description, link, email)
            }
        }

        postFormViewModel.isLoading.observe(this){
            showLoading(it)
        }


        postFormViewModel.isEmpty.observe(this){empty->
            if(empty){
                showErrorDialog("all fields need to be filled")
            }else{
                postFormViewModel.isError.observe(this){err->
                    postFormViewModel.Msg.observe(this){msg->
                        if(err){
                            showErrorDialog(msg)
                        }else{
                            showSuccessDialog(msg)
                        }
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

    private fun showErrorDialog(msg:String){
        val dialog = Dialog(this)
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

    private fun showSuccessDialog(msg:String){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_success)
        dialog.setCancelable(false )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnOK : Button =dialog.findViewById(R.id.btn_ok_err)
        val txtsuccess: TextView = dialog.findViewById(R.id.txt_warning)

        txtsuccess.text = msg

        btnOK.setOnClickListener {
            val intentEntry = Intent(this, EntryActivity::class.java)
            startActivity(intentEntry)
        }

        dialog.show()
    }
}