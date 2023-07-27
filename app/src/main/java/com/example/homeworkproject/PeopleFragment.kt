package com.example.homeworkproject

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PeopleFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rowView= inflater.inflate(R.layout.fragment_people, container, false)



       val mAuth=FirebaseAuth.getInstance()

        val db= FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance().currentUser?.uid

        val gallaryAccess = rowView.findViewById<ImageView>(R.id.images)
        val names = rowView.findViewById<TextView>(R.id.names)
//        val number = rowView.findViewById<EditText>(R.id.numbers)
        val emails = rowView.findViewById<TextView>(R.id.emails)
        val logoutBtn=rowView.findViewById<Button>(R.id.logout)

        val EditButton = rowView.findViewById<Button>(R.id.edit)

        EditButton.setOnClickListener {
            startActivity(Intent(requireContext(), EditGoogleAuthProfileActivity::class.java))

        }
//        for listview & and Adapter

//        val list=rowView.findViewById<ListView>(R.id.list_item)

//        db.collection("users").get().addOnSuccessListener {
//            val dataModels = it.toObjects(DataModel::class.java)
//            list.adapter= activity?.let { it1 -> ListViewAdapter(it1,dataModels) }
//        }.addOnFailureListener {
//        }


        if (auth != null) {
            db.collection("users").document(auth)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot != null && snapshot.exists()) { // Check if snapshot is not null and exists

                        val myData: DataModel? = snapshot.toObject(DataModel::class.java)
                        if (myData != null) { // Check if myData is not null

//                            FashionList.add(myData)

                            names.setText(myData.name)
//                            number.setText(myData.number)
                            emails.setText(myData.email)
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




        logoutBtn.setOnClickListener {
          mAuth.signOut()
            startActivity(Intent(activity,LoginActivity::class.java) )
            Toast.makeText(requireContext(), "Logout success", Toast.LENGTH_SHORT).show()
        }
        return rowView
    }


}