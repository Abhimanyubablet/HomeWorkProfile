package com.example.homeworkproject

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.support.annotation.NonNull
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso


class HomeFragment : Fragment() {
    private lateinit var rImage: ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rowView= inflater.inflate(R.layout.fragment_home, container, false)


        rImage = rowView.findViewById(R.id.rImage);
        val names=rowView.findViewById<TextView>(R.id.name)
        val ages=rowView.findViewById<TextView>(R.id.age)
        val auth = FirebaseAuth.getInstance().currentUser?.uid
        val db= FirebaseFirestore.getInstance()

        if (auth != null) {
            db.collection("User Rejestration").document(auth)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot != null && snapshot.exists()) { // Check if snapshot is not null and exists

                        val myData: DataModel? = snapshot.toObject(DataModel::class.java)
                        if (myData != null) { // Check if myData is not null

//                            FashionList.add(myData)

                            names.setText(myData.name)
                            ages.setText(myData.age)
//                            emails.setText(myData.email)
                            Glide.with(this)
                                .load(myData.imageUrl)
                                .error(R.drawable.ic_launcher_background)
                                .into(rImage)


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

        return rowView
    }


}