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
import android.widget.ListView
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


        val db= FirebaseFirestore.getInstance()

            db.collection("User Rejestration")
                .get()
                .addOnSuccessListener { snapshot ->

                        val myData: MutableList<DataModel> = snapshot.toObjects(DataModel::class.java)
                        if (myData != null) { // Check if myData is not null

//                            FashionList.add(myData)

                            val list = rowView.findViewById<ListView>(R.id.list_item)



                            list.adapter = activity?.let { it1 -> ListViewAdapter(it1, myData) }

                        }else {
                            Log.e(ContentValues.TAG, "Snapshot is null or doesn't exist")
                        }
                }
                .addOnFailureListener { e ->
                    Log.e(ContentValues.TAG, "Error adding document", e)
                }

        return rowView
    }


}