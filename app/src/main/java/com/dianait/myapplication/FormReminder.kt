package com.dianait.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FormReminder : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_reminder)
        val inputTextReminder = findViewById<TextView>(R.id.input_text_reminder)


        val btnAddReminder = findViewById<FloatingActionButton>(R.id.btn_set_reminder)
        btnAddReminder.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("textReminder", inputTextReminder.text.toString())
            }
            startActivity(intent)
        }
    }
}
