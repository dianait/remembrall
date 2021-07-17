import android.util.Log
import com.dianait.myapplication.MainActivity
import com.google.firebase.firestore.GeoPoint

data class Reminder(val id: String, val text: String, val emoji: String, val location: GeoPoint, val name: String) {
constructor():this("","","",GeoPoint(0.0, 0.0), "")
}


