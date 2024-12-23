package com.example.researcherpool.ui.DetailUser

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
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.researcherpool.EntryActivity
import com.example.researcherpool.R
import com.example.researcherpool.UserEntryActivity
import com.example.researcherpool.ViewModelFactory
import com.example.researcherpool.api.response.ItemDetail
import com.example.researcherpool.api.response.UploadParticipantResponse
import com.example.researcherpool.api.retrofit.ApiConfig
import com.example.researcherpool.databinding.ActivityDetailUserBinding
import com.example.researcherpool.preference.DataStoreViewModel
import com.example.researcherpool.preference.UserPreference
import com.example.researcherpool.preference.dataStore
import com.example.researcherpool.reduceFileImage
import com.example.researcherpool.uriToFile
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel
    private var currentImageUri: Uri? = null

    private lateinit var link: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        detailUserViewModel = ViewModelProvider(this).get(DetailUserViewModel::class.java)

        val idIntent = intent.getStringExtra(ID)
        if (idIntent != null) {
            detailUserViewModel.getDetail(idIntent)

        }

        binding.btnParticipate.isEnabled = true


        detailUserViewModel.myDetail.observe(this){list->
            list?.get(0)?.let { bind(it) }
        }

        binding.btnLink.setOnClickListener {
            val url = "https://$link"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.btnGallery.setOnClickListener {
            startgallery()
        }


        detailUserViewModel.isError.observe(this){err->
            if(err){
                detailUserViewModel.Msg.observe(this){msg->
                    showErrorDialog(msg)
                }
            }
        }
        val pref = UserPreference.getInstance(application.dataStore)
        val dataStoreViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            DataStoreViewModel::class.java)
        binding.btnParticipate.setOnClickListener {
            dataStoreViewModel.getEmail().observe(this){email->
                if (idIntent != null) {
                    uploadImage(idIntent,email)
                }
                Log.d(TAG, "email: $email")
            }
        }

        binding.imagePreview.setOnClickListener {
            showFullScreenImage(currentImageUri)
        }

        dataStoreViewModel.getEmail().observe(this){email->
            if (idIntent != null) {
                detailUserViewModel.getVerif(idIntent, email)
            }
        }

        detailUserViewModel.ispart.observe(this){part->
            if(part){
                binding.btnGallery.isEnabled = false
                binding.btnGallery.visibility = View.GONE
                binding.btnParticipate.isEnabled = false
                binding.btnParticipate.visibility = View.GONE
                binding.btnStatus.visibility = View.VISIBLE
                detailUserViewModel.photo.observe(this){photo->
                    val resourceId = resources.getIdentifier(photo , "drawable", "com.example.researcherpool")
                    val bitmap = BitmapFactory.decodeResource(resources, resourceId)

                    // Scale the drawable image to match the gallery image size (e.g., to fit ImageView)
                    val scaledBitmap = Bitmap.createScaledBitmap(bitmap,bitmap.width, bitmap.height, true)
                    val scaledBitmap1 = Bitmap.createScaledBitmap(bitmap,bitmap.width, bitmap.height, true)
                    binding.imagePreview.setImageBitmap(scaledBitmap)

                    val imageUri = getImageUriFromBitmap(scaledBitmap1)

                    // Set the URI for the currentImageUri variable
                    currentImageUri = imageUri
                }
                detailUserViewModel.status.observe(this){stat->
                    binding.btnStatus.text = stat
                    when(stat?.uppercase()){
                        "APPROVED" ->{
                            binding.btnStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.greenopen))
                        }
                        "DECLINE" ->{
                            binding.btnStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                        }
                        else ->{
                            binding.btnStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
                        }
                    }
                }
            }
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val file = File(cacheDir, "temp_image.jpg") // You can choose the file format (e.g., .jpg, .png)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream) // Compress the image to the file
        outputStream.flush()
        outputStream.close()
        return Uri.fromFile(file) // Return the URI of the file
    }

    private fun startgallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imagePreview.setImageURI(it)
        }
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

    private fun uploadImage(id_pool:String, email_user:String){
        currentImageUri?.let{uri->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "file",
                imageFile.name,
                requestImageFile
            )

            showLoading(true)
            binding.btnParticipate.isEnabled= false

            val idPoolReqBody = RequestBody.create("text/plain".toMediaTypeOrNull(), id_pool)
            val emailUserReqBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email_user)

            val client = ApiConfig.getApiService().uploadParticipant(idPoolReqBody, emailUserReqBody, multipartBody)
            client.enqueue(object:Callback<UploadParticipantResponse>{
                override fun onResponse(
                    call: Call<UploadParticipantResponse>,
                    response: Response<UploadParticipantResponse>
                ) {
                    binding.btnParticipate.isEnabled = true
                    if(response.isSuccessful){
                        showLoading(false)
                        val responseBody = response.body()
                        if(responseBody != null){
                            responseBody.msg?.let { showSuccessDialog(it) }
                        }else{
                            showErrorDialog("response is null")
                        }
                    }else{
                        showErrorDialog("failed to catch response")
                    }
                }

                override fun onFailure(call: Call<UploadParticipantResponse>, t: Throwable) {
                    binding.btnParticipate.isEnabled = true
                    t.message?.let { showErrorDialog(it) }
                }

            })
        }?: showToast("image is empty")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading:Boolean){
        if(isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    fun bind (list :ItemDetail){
        binding.txtTitle.text = list.title
        binding.txtDescription.text = list.description
        binding.txtEmail.text = list.researcher
        link = list.link.toString()
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
            val intentEntry = Intent(this, UserEntryActivity::class.java)
            startActivity(intentEntry)
        }

        dialog.show()
    }
    companion object{
        const val ID = "id"
        const val TAG = "DetailUserActivity"
    }
}