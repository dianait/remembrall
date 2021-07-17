package services.firestore

import Reminder
import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

 class FirestoreService {
     private var db: FirebaseFirestore = Firebase.firestore

    fun getAllReminders(callback: (QuerySnapshot)->Unit = { data ->   println("data: $data") }){
        db.collection("reminders")
            .get()
            .addOnSuccessListener { result ->
                callback(result)
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }


     fun getReminder(id: String,callback: (DocumentSnapshot)-> Unit = {} ) {
         var result = db.collection("reminders").document(id).get().addOnSuccessListener { document ->
             if (document != null) {
                 callback(document)
             } else {
                 Log.d("debugInit", "No such document")
             }
         }
             .addOnFailureListener { exception ->
                 Log.d("debugInit", "get failed with ", exception)
             }

     }

     fun deleteReminder(id: String){
         db.collection("reminders").document(id).delete().addOnSuccessListener {
             Log.d("debugInit", "elemento eliminado $id")
         }.addOnFailureListener { exception ->
             Log.d("debugInit", "get failed with ", exception)
         }

     }

     fun addReminder(reminder: Reminder){
         val r = hashMapOf(
             "text" to reminder.text,
             "emoji" to reminder.emoji,
             "name" to reminder.name,
             "location" to GeoPoint(reminder.location.latitude, reminder.location.longitude)
         )

         db.collection("reminders").document()
             .set(r)
             .addOnSuccessListener { Log.d("debugInit", "DocumentSnapshot successfully written!") }
             .addOnFailureListener { e -> Log.w("debugInit", "Error writing document", e) }
     }


 }

