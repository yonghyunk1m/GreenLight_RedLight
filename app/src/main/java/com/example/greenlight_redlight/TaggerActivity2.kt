package com.example.greenlight_redlight

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import com.example.greenlight_redlight.databinding.ActivityTagger2Binding
import com.google.firebase.database.*

class TaggerActivity2 : AppCompatActivity() {

    private lateinit var GreenButton: ImageButton
    private lateinit var RedButton: ImageButton

    var playerName: String? = ""
    var roomName: String? = ""
    var message: String = ""
    lateinit var listView: ListView;

    lateinit var database: FirebaseDatabase
    //lateinit var messageRef: DatabaseReference
    lateinit var playersRef: DatabaseReference
    private lateinit var usersList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tagger2)


        database = FirebaseDatabase.getInstance()
        playersRef = database.getReference("rooms/$roomName/players")

        val binding = ActivityTagger2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        GreenButton = binding.greenButton
        RedButton = binding.redButton
        listView = binding.listView

        val extras: Bundle? = intent.extras
        if (extras != null) {
            usersList = extras.getStringArrayList("usersList") as ArrayList<String>
        }

        GreenButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                binding.background.setBackgroundResource(R.drawable.tagger_green_background)
            }
        })

        RedButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                binding.background.setBackgroundResource(R.drawable.tagger_red_background)
            }
        })

        addUsersList()
    }

    private fun addUsersList() {
        Log.d("*******", "$usersList, $listView")
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this@TaggerActivity2,
            android.R.layout.simple_list_item_1, usersList
        )
        listView.adapter = adapter
    }

}