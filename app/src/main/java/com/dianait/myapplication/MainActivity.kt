package com.dianait.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import services.firestore.FirestoreService


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "\uD83D\uDD2E Remembrall"
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container_main, HomeFragment())
            .commit()

        supportFragmentManager.setFragmentResultListener(
            "goToForm",
            this,
            {
                    _,_ -> navigateToForm()
            }

        )

        supportFragmentManager.setFragmentResultListener(
            "goToDetail",
            this,
            {
                    _, bundle:Bundle ->
                var id = bundle.getString("id")
                if (id != null) {
                    navigateToDetailFragment(id)
                }
            }

        )

    }

    private fun navigateToForm(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_main, FormFragment())
            .addToBackStack("FormFragment")
            .commit()
    }

    private fun navigateToDetailFragment(id: String){
        val  bundle: Bundle = Bundle();
        bundle.putString("id", id)
        val detailFragment = DetailFragment()
        detailFragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_main, detailFragment)
            .addToBackStack("DetailFragment")
            .commit()
    }





}
// Log.d("Diana ", Emoji.PERSONAL)
// Log.d("Diana " , "${Emoji.PERSONAL.src::class.simpleName}")
// Toast.makeText(this,"Cell clicked", Toast.LENGTH_SHORT).show()
