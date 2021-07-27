package com.dianait.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import services.firestore.FirestoreService


interface CellClickListener2 {
    fun onCellClickListener(id: String)
    fun onDeleteReminder(id: String)
}

class HomeFragment : Fragment(R.layout.fragment_home), CellClickListener2 {

    enum class Emoji(val src: String) {
        BUY("🛒"),
        PERSONAL("☘️"),
        PICK("↗️")
    }

    private lateinit var itemsList: MutableList<DocumentSnapshot>
    private lateinit var customAdapter: RecyclerAdapter
    private lateinit var db: FirestoreService
    private lateinit var btnAddReminder: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirestoreService()
        itemsList = mutableListOf<DocumentSnapshot>()
        recyclerView = view.findViewById(R.id.reminder_list)
        db.getAllReminders { data: QuerySnapshot ->
            itemsList.addAll(data!!)
            customAdapter.notifyDataSetChanged()
        }
        customAdapter = RecyclerAdapter(itemsList, this)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter
        btnAddReminder = view.findViewById<FloatingActionButton>(R.id.btn_add_reminder)
        btnAddReminder.setOnClickListener {
            // TODO: Go to form fragment
            navigationToFormFragment()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCellClickListener(id: String) {
        navigationToDetailFragment(id)
    }

     override fun onDeleteReminder(id: String){
        db.deleteReminder(id)
        var reminderToDelete: DocumentSnapshot? = itemsList.find { it.id == id }
        itemsList.remove(reminderToDelete)
        customAdapter.notifyDataSetChanged()
    }

    private fun navigationToFormFragment() {
        parentFragmentManager.setFragmentResult("goToForm",
            bundleOf("goToForm" to "goToForm"))
    }

    private fun navigationToDetailFragment(id: String) {
        parentFragmentManager.setFragmentResult("goToDetail",
            bundleOf("id" to id))

    }
}
