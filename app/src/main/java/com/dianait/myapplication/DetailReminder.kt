package com.dianait.myapplication

import Location
import Reminder
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import services.firestore.FirestoreService

class DetailReminder : AppCompatActivity() {
    private lateinit var db: FirestoreService

    override fun onCreate(savedInstanceState: Bundle?) {
        db = FirestoreService()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_reminder)
        initReminder()
        title = "\uD83D\uDD2E Remembrall"
    }

    private fun initReminder() {
        var reminderTitle = findViewById<TextView>(R.id.txt_reminder_title)
        var id = intent.getStringExtra("id")!!
        db.getReminder(id) { data ->
            reminderTitle.text = data.getString("emoji") + " " + data.getString("text")
        }

    }
}
