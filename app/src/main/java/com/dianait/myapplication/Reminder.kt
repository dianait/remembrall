import android.util.Log
import com.dianait.myapplication.MainActivity
import com.google.firebase.firestore.GeoPoint

data class Reminder(val id: String, val text: String, val emoji: String, val location: GeoPoint, val name: String) {

    init {
        Log.d("debugInit", "Reminder with the text: $text created")
    }

}


