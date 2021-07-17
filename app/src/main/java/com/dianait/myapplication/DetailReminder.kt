package com.dianait.myapplication

import Location
import Reminder
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailReminder : AppCompatActivity() {
    private lateinit var listReminders: ArrayList<DocumentSnapshot>
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        db = Firebase.firestore
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_reminder)
        initReminder()
        title = "\uD83D\uDD2E Remembrall"
    }

    private fun initReminder(){
        listReminders = ArrayList<DocumentSnapshot>()
        var id = intent.getStringExtra("id")!!
        getDocumentFromFirestore(db,"reminders", id)
        var reminderTitle = findViewById<TextView>(R.id.txt_reminder_title)
        var currentReminder: DocumentSnapshot = listReminders.component1()
        Log.d("debugInit", currentReminder.toString())
       //  reminderTitle.text = currentReminder.getString("emoji") + " " + currentReminder.getString("text")

    }

    private fun getDocumentFromFirestore(db: FirebaseFirestore, collection: String, id: String) {
        val docRef = db.collection(collection).document(id)
        docRef.get()
            .addOnSuccessListener { r ->
                    listReminders.add(0, r)
            }
            .addOnFailureListener { exception ->
                Log.d("debugInit", "get failed with ", exception)
            }

}

}
