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
    lateinit var roomsList: MutableList<String>;

    var playerName: String? = ""

    var roomName: String? = ""
    var message: String = ""
    lateinit var listView: ListView;

    lateinit var database: FirebaseDatabase
    lateinit var messageRef: DatabaseReference
    lateinit var playersRef: DatabaseReference
    private lateinit var usersList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tagger2)

        val preferences: SharedPreferences = getSharedPreferences("PREFS", 0)
        playerName = preferences.getString("playerName", "")

        var extras: Bundle? = intent.extras
        if (extras != null) {
            usersList = extras.getStringArrayList("usersList") as ArrayList<String>
        }
        roomName= playerName // ID (nickname)
        database = FirebaseDatabase.getInstance()
        //playersRef = database.getReference("rooms/$roomName/players")
        messageRef = database.getReference("rooms/$roomName/message")

        val binding = ActivityTagger2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        GreenButton = binding.greenButton
        RedButton = binding.redButton
        listView = binding.listView

        GreenButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                binding.background.setBackgroundResource(R.drawable.tagger_green_background)
                message = "Green Light"
                messageRef.setValue(message)
            }
        })

        RedButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                binding.background.setBackgroundResource(R.drawable.tagger_red_background)
                message = "Red Light"
                messageRef.setValue(message)
            }
        })

        addUsersList()

        playersRef = database.getReference("rooms/$roomName/players")
        addPlayersEventListener()

        messageRef = database.getReference("rooms/$roomName/message")
        message = "host"
        messageRef.setValue(message)
        addRoomEventListener()
    }

    private fun addUsersList() {
        Log.d("*******", "$usersList, $listView")
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this@TaggerActivity2,
            android.R.layout.simple_list_item_1, usersList
        )
        listView.adapter = adapter
    }

    private fun addPlayersEventListener() {
        playersRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                usersList.clear()
                val users: Iterable<DataSnapshot> = dataSnapshot.children
                for(snapshot: DataSnapshot in users) {
                    val name = snapshot.getValue(String::class.java)
                    if(name != roomName) {
                        if (name != null) {
                            usersList.add(name)
                        }
                    }
                    val adapter: ArrayAdapter<String> = ArrayAdapter(
                        this@TaggerActivity2,
                        android.R.layout.simple_list_item_1, usersList
                    )
                    listView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // nothing
            }
        })
    }
    private fun addRoomEventListener() {
        messageRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue(String::class.java)?.contains("fail") as Boolean) {
                    Toast.makeText(this@TaggerActivity2, "" +
                            dataSnapshot.getValue(String::class.java)!!.replace("host:", ""),
                        Toast.LENGTH_SHORT).show()
                }
                else if(dataSnapshot.getValue(String::class.java)?.contains("guest:") as Boolean) {
                    val binding = ActivityTagger2Binding.inflate(layoutInflater)
                    setContentView(binding.root)
                    listView = binding.listView
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                messageRef.setValue(message)
            }
        })
    }
}