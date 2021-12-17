package com.example.greenlight_redlight

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.example.greenlight_redlight.databinding.ActivityTaggerBinding


class TaggerActivity : AppCompatActivity() {
    lateinit var button: Button
    var playerName: String? = ""
    var roomName: String? = ""
    var message: String = ""
    lateinit var usersList: MutableList<String>;
    lateinit var listView: ListView;

    lateinit var database: FirebaseDatabase
    lateinit var messageRef: DatabaseReference
    lateinit var playersRef: DatabaseReference
    private lateinit var lengthRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tagger)

        button = findViewById(R.id.button)
        button.isEnabled = false

        listView = findViewById(R.id.listView)
        usersList = mutableListOf();

        database = FirebaseDatabase.getInstance()

        val preferences: SharedPreferences = getSharedPreferences("PREFS", 0)
        playerName = preferences.getString("playerName", "")

        var extras: Bundle? = intent.extras
        if (extras != null) {
            roomName = extras.getString("roomName")
        }

        button.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View) {
                button.isEnabled = false
                message = "host:START!!!"
                messageRef.setValue(message)
            }
        })

        lengthRef = database.getReference("rooms/$roomName/length")
        lengthEventListener()

        playersRef = database.getReference("rooms/$roomName/players")
        addPlayersEventListener()

        messageRef = database.getReference("rooms/$roomName/message")
        message = "host"
        messageRef.setValue(message)
        addRoomEventListener()
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
                        this@TaggerActivity,
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

    private fun lengthEventListener() {
        lengthRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                button.isEnabled = dataSnapshot.getValue(Long::class.java)!! > 1
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // nothing
            }
        })
    }

    private fun addRoomEventListener() {
        messageRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.getValue(String::class.java)?.contains("guest:") as Boolean) {
                    Toast.makeText(this@TaggerActivity, "" +
                            dataSnapshot.getValue(String::class.java)!!.replace("guest:", ""),
                        Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                messageRef.setValue(message)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        var roomRef = database.getReference("rooms/$playerName")
        roomRef.removeValue()
    }

    private var doubleBackToExit = false
    override fun onBackPressed() {
        if (doubleBackToExit) {
            finishAffinity()
        } else {
            Toast.makeText(this, "종료하시려면 뒤로가기를 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
            doubleBackToExit = true
            runDelayed(1500L) {
                doubleBackToExit = false
            }
        }
    }
    fun runDelayed(millis: Long, function: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(function, millis)
    }
}