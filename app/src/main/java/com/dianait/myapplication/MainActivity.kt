package com.dianait.myapplication

import Location
import Reminder
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.RecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


interface CellClickListener {
    fun onCellClickListener(id: String)
}

class MainActivity : AppCompatActivity(), CellClickListener {

    enum class Emoji(val src: String) {
        BUY("🛒"),
        PERSONAL("☘️"),
        PICK("↗️")
    }

    private lateinit var itemsList: MutableList<DocumentSnapshot>
    private lateinit var customAdapter: RecyclerAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        db = Firebase.firestore
        itemsList = mutableListOf<DocumentSnapshot>()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "\uD83D\uDD2E Remembrall"
        initRecycler()
        val btnAddReminder = findViewById<FloatingActionButton>(R.id.btn_add_reminder)
        btnAddReminder.setOnClickListener {
            val intent = Intent(this, FormReminder::class.java)
            startActivity(intent)
        }
    }
    
 /*   private fun getReminders(reminder: Reminder = Reminder(22222.toString(), "Nothing", Emoji.PICK.src, Location(id = 1, name = "Supermarket", lat = 39.4664413, lng = -0.3787043))){


        itemsList.add(Reminder(id = "1", text = "Buy x", emoji = Emoji.BUY.src, Location(id = 1, name = "Supermarket", lat = 39.4664413, lng = -0.3787043)))
        itemsList.add(Reminder(id = "2", text = "Do x", emoji = Emoji.PERSONAL.src, Location(id = 2, name = "Gym", lat = 39.4664413, lng = -0.3787043)))
        itemsList.add(Reminder(id = "3", text = "Get x", emoji = Emoji.PICK.src, Location(id = 3, name = "Clinic", lat = 39.4664413, lng = -0.3787043)))
        if (reminder.text != "Nothing") itemsList.add(reminder)
        customAdapter.notifyDataSetChanged()
    }

      */


    override fun onCellClickListener(id: String) {
        // Toast.makeText(this,"Cell clicked", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, DetailReminder::class.java).apply {
            putExtra("id", id)
        }
        startActivity(intent)

    }

    private fun initRecycler(){
        val recyclerView: RecyclerView = findViewById(R.id.reminder_list)
        getCollectionFromFirebase(db, "reminders")
        Log.d("diana", itemsList.toString())
        customAdapter = RecyclerAdapter(itemsList, this)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter


    }

    private fun getCollectionFromFirebase(db: FirebaseFirestore, collection: String) {
        db.collection(collection)
            .get()
            .addOnSuccessListener { result ->
                    itemsList.addAll(result!!)
                    customAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

}

// Log.d("Diana ", Emoji.PERSONAL)
// Log.d("Diana " , "${Emoji.PERSONAL.src::class.simpleName}")
