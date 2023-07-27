package com.example.homeworkproject

import android.app.Activity
import android.app.DownloadManager
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class EditGoogleAuthProfileActivity : AppCompatActivity() {
    private var storageRef = Firebase.storage.reference
    lateinit var downloadUrl: String
    private lateinit var uri: Uri

    private val db = FirebaseFirestore.getInstance()
    private val auth= FirebaseAuth.getInstance().currentUser?.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_google_auth_profile)



        val gallaryAccess=findViewById<ImageView>(R.id.update_images)
        val name=findViewById<EditText>(R.id.update_names)
        val email=findViewById<EditText>(R.id.update_emails)


        gallaryAccess.setOnClickListener {
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImg)
        }
        val button=findViewById<Button>(R.id.update_submit)

        button.setOnClickListener {


            val db = FirebaseFirestore.getInstance()
            val user:Map<String,Any> = hashMapOf(
                "name" to name.text.toString(),
                "email" to email.text.toString(),

            )

// Add a new document with a generated ID
            if (auth != null) {
                db.collection("users").document(auth)
                    .update(user )
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "DocumentSnapshot added with ID")

                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error adding document", e)
                    }
            }

            startActivity(Intent(this,DashBoardActivity::class.java))

        }

// Add a new document with a generated ID
        if (auth != null) {
            db.collection("users").document(auth)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot != null && snapshot.exists()) { // Check if snapshot is not null and exists

                        val myData: DataModel? = snapshot.toObject(DataModel::class.java)
                        if (myData != null) { // Check if myData is not null

//                            FashionList.add(myData)

                            name.setText(myData.name)
                            email.setText(myData.email)
                            Glide.with(this)
                                .load(myData.image)
                                .error(R.drawable.ic_launcher_background)
                                .into(gallaryAccess)


                        } else {
                            Log.e(ContentValues.TAG, "DataModel is null")
                        }
                    } else {
                        Log.e(ContentValues.TAG, "Snapshot is null or doesn't exist")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(ContentValues.TAG, "Error adding document", e)
                }
        }

    }

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val imgUri = data?.data

                if (imgUri != null) {
                    val fileName = System.currentTimeMillis().toString()
                    val imageRef = storageRef.child("image/").child(fileName)

                    imageRef.putFile(imgUri)
                        .addOnSuccessListener {
                            // File downloaded successfully
                            Toast.makeText(this, "upload success", Toast.LENGTH_SHORT).show()
                            it.storage.downloadUrl.addOnSuccessListener { uri ->
                                downloadUrl = imgUri.toString()
                                if (auth != null) {
                                    val image:Map<String,Any> = hashMapOf(

                                        "image" to downloadUrl,

                                        )
                                    db.collection("users").document(auth)
                                        .update(image)
                                        .addOnSuccessListener {
                                            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID")

                                        }
                                        .addOnFailureListener { e ->
                                            Log.w(ContentValues.TAG, "Error adding document", e)
                                        }
                                }


                                // Process the downloaded file or handle the download URL
                            }.addOnFailureListener { exception ->
                                Toast.makeText(this, exception.toString()+"download url", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { exception ->
                            // File download failed, handle the error
                            Toast.makeText(this, "upload image$exception", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
}