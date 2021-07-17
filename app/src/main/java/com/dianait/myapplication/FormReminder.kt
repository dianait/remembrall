package com.dianait.myapplication

import Reminder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.GeoPoint
import services.firestore.FirestoreService

class FormReminder : AppCompatActivity() {
    private lateinit var db: FirestoreService
    override fun onCreate(savedInstanceState: Bundle?) {
        db = FirestoreService()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_reminder)
        val inputTextReminder = findViewById<TextInputEditText>(R.id.input_reminder)
        val inputEmoji = findViewById<TextInputEditText>(R.id.input_emoji_text)
        val inputLatitude = findViewById<TextInputEditText>(R.id.input_latitude_text)
        val inputLongitude = findViewById<TextInputEditText>(R.id.input_longitude_text)
        val inputNameLocation = findViewById<TextInputEditText>(R.id.input_name_location_text)


        val btnAddReminder = findViewById<FloatingActionButton>(R.id.btn_set_reminder)
        btnAddReminder.setOnClickListener {

            var reminder = Reminder(id = "", text = inputTextReminder.text.toString(),   emoji = inputEmoji.text.toString(), location = GeoPoint(inputLatitude.text.toString().toDouble(), inputLongitude.text.toString().toDouble()),name = inputNameLocation.text.toString())
            db.addReminder(reminder)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
