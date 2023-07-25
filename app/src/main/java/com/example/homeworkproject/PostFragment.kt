package com.example.homeworkproject

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class PostFragment : Fragment() {

    private  lateinit var imageView: ImageView
    private lateinit var selectImgBtn:TextView
    private lateinit var uploadImgBtn: Button
    private var storageRef= Firebase.storage.reference
    private lateinit var uri: Uri

    var uploadv: Button? = null
    var progressDialog: ProgressDialog? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rowView= inflater.inflate(R.layout.fragment_post, container, false)

        val storageRef = Firebase.storage.reference;

        val selectImgBtn = rowView.findViewById<TextView>(R.id.select_image)
        val uploadImgBtn = rowView.findViewById<Button>(R.id.upload_img)
        val imageView = rowView.findViewById<ImageView>(R.id.image_upload)
        uploadv = rowView.findViewById(R.id.uploadv);
        val galleryImage=registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                imageView.setImageURI(it)
                if (it != null) {
                    uri =it
                }
            }
        )

        selectImgBtn.setOnClickListener {
            galleryImage.launch("image/*")
        }

        uploadImgBtn.setOnClickListener {
            if (uri != null) {
                val fileName = System.currentTimeMillis().toString()
                val imageRef = storageRef.child("image/").child(fileName)

                imageRef.putFile(uri)
                    .addOnSuccessListener { taskSnapshot ->
                        // File uploaded successfully
                        Toast.makeText(requireActivity(), "Upload success", Toast.LENGTH_SHORT).show()

                        taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                            val downloadUrl = downloadUri.toString()
                            // Process the download URL or save it to the database
                        }.addOnFailureListener { exception ->
                            Toast.makeText(requireActivity(), exception.toString() + "download URL", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        // File upload failed, handle the error
                        Toast.makeText(requireActivity(), "Upload image $exception", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireActivity(), "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }


        return rowView
    }



}