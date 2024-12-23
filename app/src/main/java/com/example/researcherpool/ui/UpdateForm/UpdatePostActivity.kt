package com.example.researcherpool.ui.UpdateForm

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.researcherpool.EntryActivity
import com.example.researcherpool.R
import com.example.researcherpool.api.response.ItemDetail
import com.example.researcherpool.databinding.ActivityUpdatePostBinding
import kotlin.math.log

class UpdatePostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdatePostBinding
    private lateinit var updatePostViewModel: UpdatePostViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityUpdatePostBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        updatePostViewModel = ViewModelProvider(this).get(UpdatePostViewModel::class.java)

        val id = intent.getStringExtra(ID)
        if (id != null) {
            updatePostViewModel.getDetail(id)
        }

        updatePostViewModel.isLoading.observe(this){
            showLoading(it)
        }

        updatePostViewModel.isError.observe(this){err->
            Log.d(TAG, "$err")
            if(err){
                updatePostViewModel.Msge.observe(this){msg->
                    if (msg != null) {
                        showErrorDialog(msg)
                    }
                }
            }else{
                updatePostViewModel.myDetail.observe(this){detail->
                    bind(detail?.get(0))
                }
            }
        }



        binding.btnStatus.setOnClickListener {
            toggleBtn(updatePostViewModel.Status.value?: "CLOSED")

        }
        binding.btnUpdate.setOnClickListener {
            val title = binding.edttitle.text.toString()
            val description = binding.edtDesc.text.toString()
            val link = binding.edtLink.text.toString()
            val status = binding.btnStatus.text.toString()

            if (id != null) {
                updatePostViewModel.updateDetail(id, title, description,link,status)
            }
        }

        updatePostViewModel.isEmpty.observe(this){emp->
            if(emp){
                showErrorDialog("All Fields cannot be empty")
            }
        }

        updatePostViewModel.isErroru.observe(this){err->
            if(err){
                updatePostViewModel.Msge.observe(this){msg->
                    if (msg != null) {
                        showErrorDialog(msg)
                    }
                }
            }else{
                updatePostViewModel.Msge.observe(this){msg1->
                    if (msg1 != null) {
                        showSuccessDialog(msg1)
                    }
                }
            }
        }

        updatePostViewModel.Status.observe(this){stat->
            fetchBtn(stat)
        }


    }

    private fun showLoading(isLoading:Boolean){
        if(isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun bind(detail : ItemDetail?){
        binding.edttitle.setText(detail?.title)
        binding.edtDesc.setText(detail?.description)
        binding.edtLink.setText(detail?.link)
        val stat = detail?.status?.uppercase()
        detail?.status?.uppercase()?.let { updatePostViewModel.setStatus(it) }
    }

    private fun toggleBtn(stat: String){
        val status  = stat.uppercase()
        if(status == "OPEN"){
            updatePostViewModel.setStatus("CLOSED")
        }else if(status == "CLOSED"){
            updatePostViewModel.setStatus("OPEN")
        }
    }

    private fun fetchBtn(stat: String){
        val status = stat.uppercase()
        if(status == "OPEN"){
            binding.btnStatus.setText("OPEN")
            binding.btnStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.greenopen))
        }else if(status == "CLOSED"){
            binding.btnStatus.setText("CLOSED")
            binding.btnStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
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

    companion object{
        const val ID = "id"
        const val TAG = "UpdatePost"
    }
}
