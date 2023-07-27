package com.example.homeworkproject

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class RegistrationPageActivity : AppCompatActivity() {
    private  lateinit var imageView: ImageView
    private lateinit var selectImg:TextView
    private var storageRef= Firebase.storage.reference
    lateinit var downloadUrl: String

    lateinit var auth: FirebaseAuth
    private lateinit var uri: Uri

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_page)


        auth = Firebase.auth
        val authCurrent=FirebaseAuth.getInstance().currentUser?.uid
        val db= FirebaseFirestore.getInstance()

//        val name=findViewById<EditText>(R.id.name)
//        val age=findViewById<EditText>(R.id.age)
        val email=findViewById<EditText>(R.id.email)
        val phone=findViewById<EditText>(R.id.phone)
//        imageView=findViewById<ImageView>(R.id.image)
//        val selectImg=findViewById<TextView>(R.id.select_img)
        val signUp=findViewById<Button>(R.id.signup)
        val signIn=findViewById<TextView>(R.id.signin)

//        selectImg.setOnClickListener {
//            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//            changeImage.launch(pickImg)
//        }



//        val galleryImage=registerForActivityResult(
//            ActivityResultContracts.GetContent(),
//            ActivityResultCallback {
//                imageView.setImageURI(it)
//                if (it != null) {
//                    uri =it
//                }
//            }
//        )
//
//        selectImg.setOnClickListener {
//            galleryImage.launch("image/*")
//        }


        signUp.setOnClickListener {
//            val user = hashMapOf(
//                "name" to name.text.toString(),
//                "age" to age.text.toString(),
//                "email" to email.text.toString(),
//                "phone" to phone.text.toString(),
//            )
//
//
//            if (authCurrent != null) {
//                db.collection("users").document(authCurrent)
//                    .set(user)
//                    .addOnSuccessListener {
//                        Toast.makeText(this, "Successfully data upload DocumentSnapshot added with ID", Toast.LENGTH_SHORT).show()
//        //
//                        name.text.clear()
//                    }
//                    .addOnFailureListener {
//                        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
//                    }
//            }
//
//
//
//            if (uri != null) {
//                val fileName = System.currentTimeMillis().toString()
//                val imageRef = storageRef.child("image/").child(fileName)
//
//                imageRef.putFile(uri)
//                    .addOnSuccessListener { taskSnapshot ->
//                        // File uploaded successfully
//                        Toast.makeText(this, "Upload Image Successfully", Toast.LENGTH_SHORT).show()
//
//                        taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
//                            val downloadUrl = downloadUri.toString()
//                            // Process the download URL or save it to the database
//                        }.addOnFailureListener { exception ->
//                            Toast.makeText(this, exception.toString() + "download URL", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                    .addOnFailureListener { exception ->
//                        // File upload failed, handle the error
//                        Toast.makeText(this, "Upload image $exception", Toast.LENGTH_SHORT).show()
//                    }
//            } else {
//                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
//            }

            auth.createUserWithEmailAndPassword(email.text.toString() , phone.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this, "create account auth", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }


            email.text.clear()
            phone.text.clear()
        }



        signIn.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
//    private val changeImage =
//        registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val data = result.data
//                val imgUri = data?.data
//                imageView.setImageURI(imgUri)
//
//                if (imgUri != null) {
//                    val fileName = System.currentTimeMillis().toString()
//                    val imageRef = storageRef.child("image/").child(fileName)
//
//                    imageRef.putFile(imgUri)
//                        .addOnSuccessListener {
//                            // File downloaded successfully
//                            Toast.makeText(this, "upload success", Toast.LENGTH_SHORT).show()
//                            it.storage.downloadUrl.addOnSuccessListener { uri ->
//                                val downloadUrl = uri.toString()
//                                // Process the downloaded file or handle the download URL
//                            }.addOnFailureListener { exception ->
//                                Toast.makeText(this, exception.toString()+"download url", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                        .addOnFailureListener { exception ->
//                            // File download failed, handle the error
//                            Toast.makeText(this, "upload image$exception", Toast.LENGTH_SHORT).show()
//                        }
//                }
//            }
//        }
}