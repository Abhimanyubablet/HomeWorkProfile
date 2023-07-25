package com.example.homeworkproject

import android.os.Bundle
import android.support.annotation.NonNull
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class HomeFragment : Fragment() {
    var rImage: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rowView= inflater.inflate(R.layout.fragment_home, container, false)


        rImage = rowView.findViewById(R.id.rImage);
        val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

        val databaseReference = firebaseDatabase.reference

        val getImage = databaseReference.child("image")

        getImage.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(
                 dataSnapshot: DataSnapshot
                ) {
                    val link = dataSnapshot.getValue(String::class.java)
                    Picasso.get().load(link).into(rImage)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast
                        .makeText(
                            requireContext(),
                            "Error Loading Image",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            })


        return rowView
    }

}