package com.example.homeworkproject

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ListViewAdapter(val context: Context, var arrayList: List<DataModel>): BaseAdapter() {
    override fun getCount(): Int {
        return  arrayList.size
    }

    override fun getItem(p0: Int): Any {
        return  arrayList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return  p0.toLong()
    }

    @SuppressLint("ViewHolder", "MissingInflatedId")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val rowView= LayoutInflater.from(context).inflate(R.layout.layoutlistview,p2,false)
        val data=arrayList[p0]
        rowView.findViewById<TextView>(R.id.get_name).text=data.name
        rowView.findViewById<TextView>(R.id.get_email).text=data.email
        return  rowView
    }

}