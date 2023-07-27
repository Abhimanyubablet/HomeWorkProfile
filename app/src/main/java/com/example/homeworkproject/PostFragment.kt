package com.example.homeworkproject

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.UUID


class PostFragment : Fragment() {
    lateinit var downloadUrl: String

    private lateinit var imageView: ImageView
    private lateinit var selectImgBtn: TextView
    private lateinit var uploadImgBtn: Button
    private var storageRef = Firebase.storage.reference

    private lateinit var storageReference: StorageReference

    private lateinit var uri: Uri
    lateinit var videoRef: Button


    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rowView = inflater.inflate(R.layout.fragment_post, container, false)

//        / Initialize Firebase Storage for video
        storageReference = FirebaseStorage.getInstance().reference
        // Initialize Firebase Storage for get image
        val storageRef = Firebase.storage.reference;
       var auth = FirebaseAuth.getInstance().currentUser?.uid

        // Initialize Firebase Storage for get data
        val db= FirebaseFirestore.getInstance()



        val name=rowView.findViewById<EditText>(R.id.name)
        val age=rowView.findViewById<EditText>(R.id.age)
        val selectImgBtn = rowView.findViewById<TextView>(R.id.select_image)
        val uploadImgBtn = rowView.findViewById<Button>(R.id.upload_img)
        val imageView = rowView.findViewById<ImageView>(R.id.image_upload)



        videoRef = rowView.findViewById(R.id.uploadv);

        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                imageView.setImageURI(it)
                if (it != null) {
                    uri = it
                }
            }
        )

        selectImgBtn.setOnClickListener {
            galleryImage.launch("image/*")

        }


        uploadImgBtn.setOnClickListener {

//            val user:Map<String,Any> = hashMapOf(
//                "name" to name.text.toString(),
//                "age" to age.text.toString(),
//                "email" to email.text.toString(),
//                "phone" to phone.text.toString(),
//            )


//            if (auth != null) {
//                db.collection("User Rejestration").document(auth)
//                    .set(user)
//                    .addOnSuccessListener {
//                        Toast.makeText(requireContext(), "Successfully data upload DocumentSnapshot added with ID", Toast.LENGTH_SHORT).show()
//
//                        name.text.clear()
//                        age.text.clear()
//                    }
//                    .addOnFailureListener {
//                        Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
//                    }
//            }


            if (uri != null) {
                val fileName = System.currentTimeMillis().toString()
                val imageRef = storageRef.child("image/").child(fileName)

                imageRef.putFile(uri)
                    .addOnSuccessListener { taskSnapshot ->
                        // File uploaded successfully
                        Toast.makeText(requireActivity(), "Upload success", Toast.LENGTH_SHORT)
                            .show()
                        //get download url
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                            downloadUrl = downloadUri.toString()
                            val data = hashMapOf(
                                "imageUrl" to downloadUrl,
                                "name" to  name.text.toString(),
                                "age" to age.text.toString(),
                                "id" to fileName.toString()
                            )
                            //set donloadurl in firebase storage
//                            var auth = FirebaseAuth.getInstance().currentUser?.uid

                            if (auth != null) {
                                db.collection("User Rejestration").document(fileName).set(data)
                                    .addOnFailureListener {
                                        Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT)
                                            .show()

                                    }
                                    .addOnFailureListener {

                                    }
                            }
                            // Process the download URL or save it to the database
                        }.addOnFailureListener { exception ->
                            Toast.makeText(
                                requireActivity(),
                                exception.toString() + "download URL",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        // File upload failed, handle the error
                        Toast.makeText(
                            requireActivity(),
                            "Upload image $exception",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(requireActivity(), "Please select an image", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        videoRef.setOnClickListener {

            onSelectVideoButtonClicked()
        }


        return rowView
    }


//    for video upload function
    private fun uploadVideoToFirebase(videoUri: Uri) {
        val uniqueName = UUID.randomUUID().toString() + ".mp4" // Create a unique name for the video

        // Specify the storage location
        val videoRef = storageReference.child("videos/$uniqueName")

        // Upload the video to Firebase Storage
        videoRef.putFile(videoUri)
            .addOnSuccessListener {
                // Video uploaded successfully
                // You can now get the download URL if needed
                Toast.makeText(requireActivity(), "Video uploaded successfully", Toast.LENGTH_SHORT)
                    .show()
                videoRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val downloadUrl = downloadUri.toString()
                    // Do something with the download URL, like save it in your database
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occurred during the video upload
            }
    }

    private fun onSelectVideoButtonClicked() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "video/*"
        startActivityForResult(intent, REQUEST_VIDEO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_VIDEO && resultCode == Activity.RESULT_OK) {
            val selectedVideoUri = data?.data
            selectedVideoUri?.let { videoUri ->
                uploadVideoToFirebase(videoUri)
            }
        }
    }
    companion object {
        private const val REQUEST_VIDEO = 1
    }
}

