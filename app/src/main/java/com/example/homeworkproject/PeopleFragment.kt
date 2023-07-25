package com.example.homeworkproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PeopleFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rowView= inflater.inflate(R.layout.fragment_people, container, false)
         val logoutBtn=rowView.findViewById<Button>(R.id.logout)
        val list=rowView.findViewById<ListView>(R.id.list_item)

       val mAuth=FirebaseAuth.getInstance()

        val db= FirebaseFirestore.getInstance()
        db.collection("users").get().addOnSuccessListener {
            val dataModels = it.toObjects(DataModel::class.java)
            list.adapter= activity?.let { it1 -> ListViewAdapter(it1,dataModels) }
        }.addOnFailureListener {
        }

        logoutBtn.setOnClickListener {
          mAuth.signOut()
            startActivity(Intent(activity,LoginActivity::class.java) )
            Toast.makeText(requireContext(), "Logout success", Toast.LENGTH_SHORT).show()
        }
        return rowView
    }


}