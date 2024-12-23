package com.example.researcherpool.ui.Verification

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.researcherpool.EntryActivity
import com.example.researcherpool.R
import com.example.researcherpool.api.response.DetailItem
import com.example.researcherpool.databinding.ActivityVerificationBinding
import java.io.File
import java.io.FileOutputStream

class VerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerificationBinding
    private lateinit var verificationViewModel: VerificationViewModel
    private lateinit var currentImageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getStringExtra(ID)
        verificationViewModel = ViewModelProvider(this).get(VerificationViewModel::class.java)
        if (id != null) {
            verificationViewModel.getDetailPart(id)
        }

        verificationViewModel.Status.observe(this){stat->
            val status = stat?.uppercase()
            if(status != "PENDING"){
                binding.btnDecline.isEnabled = false
                binding.btnDecline.visibility = View.GONE
                binding.btnApproved.isEnabled = false
                binding.btnApproved.visibility = View.GONE
                binding.btnStat.visibility = View.VISIBLE
                binding.btnStat.text = status
                if(status == "APPROVED"){
                    binding.btnStat.setBackgroundColor(ContextCompat.getColor(this, R.color.greenopen))
                }else if(status == "DECLINE"){
                    binding.btnStat.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                }
            }
        }

        verificationViewModel.isError.observe(this){err->
            if(err){
                verificationViewModel.Msg.observe(this){msg->
                    if (msg != null) {
                        showErrorDialog(msg)
                    }
                }
            }else{
                verificationViewModel.detailVerif.observe(this){list->
                    list?.get(0)?.let { bind(it) }
                }
            }
        }

        binding.imagePreview.setOnClickListener {
            showFullScreenImage(currentImageUri)
        }

        binding.btnApproved.setOnClickListener {
            if (id != null) {
                showConfirmDialog(id, "APPROVED")
            }
        }

        binding.btnDecline.setOnClickListener {
            if (id != null) {
                showConfirmDialog(id,"DECLINE")
            }
        }

        verificationViewModel.isErroru.observe(this){erru->
            if(erru){
                verificationViewModel.Msg.observe(this){msg->
                    if (msg != null) {
                        showErrorDialog(msg)
                    }
                }
            }else{
                verificationViewModel.Msg.observe(this){msg->
                    if (msg != null) {
                        showSuccessDialog(msg)
                    }
                }
            }
        }

        verificationViewModel.isLoading.observe(this){
            showLoading(it)
        }
    }

    fun bind(list: DetailItem){
        binding.txtUser.text = list.emailUser
        binding.txtTitle.text = list.title
        val path = list.photo
        val name = path?.substringAfterLast("\\")
        val finalname = name?.substringBeforeLast(".")
        Log.d(TAG, "this is name $name")
        val resourceId = resources.getIdentifier(finalname, "drawable", "com.example.researcherpool")
        val bitmap = BitmapFactory.decodeResource(resources, resourceId)

        // Scale the drawable image to match the gallery image size (e.g., to fit ImageView)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap,300, 300, true)
        val scaledBitmap1 = Bitmap.createScaledBitmap(bitmap,500, 500, true)
        binding.imagePreview.setImageBitmap(scaledBitmap)

        val imageUri = getImageUriFromBitmap(scaledBitmap1)

        // Set the URI for the currentImageUri variable
        currentImageUri = imageUri

    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val file = File(cacheDir, "temp_image.jpg") // You can choose the file format (e.g., .jpg, .png)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream) // Compress the image to the file
        outputStream.flush()
        outputStream.close()
        return Uri.fromFile(file) // Return the URI of the file
    }


    private fun showFullScreenImage(imageUri: Uri?) {
        imageUri?.let { uri ->
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_full_screen_image)

            val imageView: ImageView = dialog.findViewById(R.id.fullScreenImageView)

            // Load the image from the URI into the ImageView
            imageView.setImageURI(uri)

            // Set the dialog properties for full screen
            dialog.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )

            // Make the dialog background transparent
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.setCancelable(true) // Allow closing the dialog when clicked outside
            dialog.show()
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

    private fun showConfirmDialog(id : String, stat:String){
        val dialog =Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_confirm)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnYes : Button = dialog.findViewById(R.id.btn_yes)
        val btnNo: Button =dialog.findViewById(R.id.btn_no)
        val txt: TextView = dialog.findViewById(R.id.txt_warning)

        txt.text = "do you want to $stat"

        btnYes.setOnClickListener {
            verificationViewModel.verifyparticipant(id, stat)
            dialog.dismiss()
        }
        btnNo.setOnClickListener {
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

    companion object{
        const val TAG = "verificationActivity"
        const val ID = "id"
    }
}