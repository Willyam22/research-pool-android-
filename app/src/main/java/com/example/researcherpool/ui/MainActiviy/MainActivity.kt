package com.example.researcherpool.ui.MainActiviy

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.researcherpool.EntryActivity
import com.example.researcherpool.R
import com.example.researcherpool.UserEntryActivity
import com.example.researcherpool.ViewModelFactory
import com.example.researcherpool.databinding.ActivityMainBinding
import com.example.researcherpool.preference.DataStoreViewModel
import com.example.researcherpool.preference.UserPreference
import com.example.researcherpool.preference.dataStore

class MainActivity : AppCompatActivity() {
    private lateinit var binding :ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pref = UserPreference.getInstance(application.dataStore)
        val dataStoreViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(DataStoreViewModel::class.java)

        mainViewModel =ViewModelProvider(this).get(MainViewModel::class.java)

        binding.btnsubmit.setOnClickListener {
            if(binding.txterre.text.toString() != ""){
                Toast.makeText(this, "need to fulfill the requirements", Toast.LENGTH_SHORT).show();
            }else{
                val email = binding.edtemail.text.toString()
                val password = binding.edtpassword.text.toString()
                mainViewModel.login(email,password)
            }

        }

        mainViewModel.isEmpty.observe(this){
            if (it){
                showErrorDialog("Any fields cannot be empty")
            }
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val startMain = Intent(Intent.ACTION_MAIN)
                startMain.addCategory(Intent.CATEGORY_HOME)
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(startMain)
            }
        })

        mainViewModel.isError.observe(this){
            if(it == false ){
                mainViewModel.email.observe(this){email->
                    if (email != null) {
                        dataStoreViewModel.setEmail(email)
                    }
                }
                mainViewModel.type.observe(this){type->
                    if (type != null) {
                        dataStoreViewModel.setType(type)
                    }
                    if(type == "researcher"){
                        val intentEntry = Intent(this,EntryActivity::class.java)
                        startActivity(intentEntry)
                    }else if (type == "user"){
                        val intentEntry = Intent(this, UserEntryActivity::class.java)
                        startActivity(intentEntry)
                    }
                }

                mainViewModel.username.observe(this){username->
                    if (username != null) {
                        dataStoreViewModel.setUsername(username)
                    }
                }

            }else{
                mainViewModel.txtMsg.observe(this){txt->
                    showErrorDialog(txt)
                }
            }
        }

        mainViewModel.isLoading.observe(this){
            showLoading(it)
        }

        binding.btnsubmitreg.setOnClickListener {

        }

        binding.edtemail.addTextChangedListener {
            if(isValidEmail(it.toString())){
                binding.txterre.text = ""
            }else{
                binding.txterre.text = "email tidak sesuai format"
            }
        }

        binding.edtemailreg.addTextChangedListener {
            if(isValidEmail(it.toString())){
                binding.txterrereg.text = ""
            }else{
                binding.txterrereg.text = "email tidak sesuai format"
            }
        }

        binding.edtconpasswordreg.addTextChangedListener {
            val pw = binding.edtpasswordreg.text.toString()
            val pw1 = binding.edtconpasswordreg.text.toString()
            Log.d(TAG, "string is: $pw")
            Log.d(TAG, "string1 is: $pw1")
            if(pw == pw1){
                Log.d(TAG, "String is equal")
                binding.txterreregcon.text = ""
            }else{
                Log.d(TAG, "String not equal")
                binding.txterreregcon.text = "password doesnt match"
            }
        }

        binding.edtuserreg.addTextChangedListener {
            val username = binding.edtuserreg.text.toString()
            if(username.length < 4 ){
                binding.txterrereguser.text = "username tidak boleh lebih kecil dari 4"
            }else if(username.length > 8){
                binding.txterrereguser.text = "username tidak boleh lebih besar dari 8"
            }else{
                binding.txterrereguser.text = ""
            }
        }

        binding.btnsubmitreg.setOnClickListener {
            val err1 = binding.txterrereg.text.toString()
            val err2 = binding.txterrereguser.text.toString()
            val err3 = binding.txterreregcon.text.toString()
            if(err1 != "" || err2 != "" || err3 != ""){

                Toast.makeText(this, "need to fulfill the requirements", Toast.LENGTH_SHORT).show();
            }else{
                val email = binding.edtemailreg.text.toString()
                mainViewModel.verifres(email)
            }
        }

        mainViewModel.isErrorV.observe(this){err->
            if(err){
                mainViewModel.MsgV.observe(this){msg->
                    if (msg != null) {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }else{
                val email = binding.edtemailreg.text.toString()
                val username = binding.edtuserreg.text.toString()
                val pw = binding.edtpasswordreg.text.toString()
                val pw1 = binding.edtconpasswordreg.text.toString()
                showConfirmDialog(email, username, pw , pw1)
            }
        }

        mainViewModel.isEmptyReg.observe(this){err->
            if(err){
                mainViewModel.msgR.observe(this){msg->
                    showErrorDialog(msg)
                }
            }else{
                mainViewModel.msgR.observe(this){msg->
                    showSuccessDialog(msg)
                }
            }
        }
    }
    private fun showConfirmDialog( email:String, username:String, pw:String, pw1:String){
        val dialog =Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_confirm)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnYes : Button = dialog.findViewById(R.id.btn_yes)
        val btnNo: Button =dialog.findViewById(R.id.btn_no)
        val txt: TextView = dialog.findViewById(R.id.txt_warning)
        btnYes.text = "researcher"
        btnNo.text = "user"
        txt.text = "what would you regis as?"

        btnYes.setOnClickListener {
            mainViewModel.register(email, username, pw,pw1, "researcher")
            dialog.dismiss()
        }
        btnNo.setOnClickListener {
            mainViewModel.register(email, username, pw, pw1, "user")
            dialog.dismiss()
        }
        dialog.show()
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



    private fun isValidEmail(email:String): Boolean{
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        var result:Boolean
        if(emailRegex.matches(email)){
            result = true
        }else{
            result  =false
        }
        return result
    }

    companion object{
        const val TAG = "MainActivity"
    }
}