package com.dianait.myapplication
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.dianait.myapplication.CellClickListener
import com.dianait.myapplication.R
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.firestore.DocumentSnapshot
import services.firestore.FirestoreService

enum class Emoji(val src: String) {
    BUY("🛒"),
    PERSONAL("☘️"),
    PICK("↗️")
}

internal class RecyclerAdapter(private var itemsList: MutableList<DocumentSnapshot>, private val cellClickListener: CellClickListener) :

    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Define where we have to put the information
        // Title
        var itemTextView: TextView = view.findViewById(R.id.text_reminder)
        // subtitle
        var locationTextView: TextView = view.findViewById(R.id.location)
        // switch done
        var switchDone = view.findViewById<SwitchMaterial>(R.id.switch_done)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reminder, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var db = FirestoreService()
        // For each item get the info required and show it in the right place
        val item: DocumentSnapshot = itemsList[position]
        // Title
        var emoji: String = getEmoji(item.getString("emoji").toString())
        holder.itemTextView.text = emoji + " " + item.getString("text")
        // Subtitle
        holder.locationTextView.text = showLocation(item)
        // Swith for delete reminder
        holder.switchDone.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cellClickListener.onDeleteReminder(item.id)
            }
        }

        holder.itemView.setOnClickListener {
            var idReminder = item.id
            cellClickListener.onCellClickListener(idReminder)
        }
    }
    override fun getItemCount(): Int {
        return itemsList.size
    }

    private fun showLocation(element: DocumentSnapshot): String {
        if (element.getGeoPoint("location")?.latitude == null) return "\uD83D\uDCCD Not location avaliable"
        return "\uD83D\uDCCD" + element.getGeoPoint("location")?.longitude + ", " + element.getGeoPoint("location")?.latitude
    }

    private fun getEmoji(name: String): String{
        return when (name) {
            "BUY" -> Emoji.BUY.src
            "PERSONAL" -> Emoji.PERSONAL.src
            "PICK" -> Emoji.PICK.src
            else -> Emoji.BUY.src
        }
    }
}
