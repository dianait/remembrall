package com.dianait.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import com.google.firebase.firestore.DocumentSnapshot
import services.firestore.FirestoreService

class DetailFragment : Fragment(R.layout.fragment_detail) {

    enum class Emoji(val src: String) {
        BUY("🛒"),
        PERSONAL("☘️"),
        PICK("↗️"),
        LOCATION("\uD83D\uDCCD")
    }

    private lateinit var reminderTitle: TextView
    private lateinit var reminderLocation: TextView
    private lateinit var db: FirestoreService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db = FirestoreService()
        reminderTitle = view.findViewById<TextView>(R.id.txt_reminder_title)
        reminderLocation = view.findViewById<TextView>(R.id.txt_location)

        var id: String = arguments?.getString("id").toString()

        db.getReminder(id){ data: DocumentSnapshot ->
            var text = data.getString("text").toString()
            var emoji: String = getEmoji(data.getString("emoji").toString())
            reminderTitle.text = "$emoji $text"
            var emojiLocation = getEmoji("LOCATION")
            var locationName = data.getString("name")
            reminderLocation.text = "$emojiLocation $locationName"
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getEmoji(name: String): String{
        return when (name) {
            "BUY" -> Emoji.BUY.src
            "PERSONAL" -> Emoji.PERSONAL.src
            "PICK" -> Emoji.PICK.src
            "LOCATION" -> Emoji.LOCATION.src
            else -> Emoji.BUY.src
        }
    }
}
