package com.dianait.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.RecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import services.firestore.FirestoreService


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
    private lateinit var db: FirestoreService

    override fun onCreate(savedInstanceState: Bundle?) {
        db = FirestoreService()
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


    override fun onCellClickListener(id: String) {
        val intent = Intent(this, DetailReminder::class.java).apply {
            putExtra("id", id)
        }
        startActivity(intent)

    }

    private fun initRecycler() {
        val recyclerView: RecyclerView = findViewById(R.id.reminder_list)
        db.getAllReminders { data: QuerySnapshot ->
            itemsList.addAll(data!!)
            customAdapter.notifyDataSetChanged()
        }
        customAdapter = RecyclerAdapter(itemsList, this)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter


    }

}
// Log.d("Diana ", Emoji.PERSONAL)
// Log.d("Diana " , "${Emoji.PERSONAL.src::class.simpleName}")
// Toast.makeText(this,"Cell clicked", Toast.LENGTH_SHORT).show()
