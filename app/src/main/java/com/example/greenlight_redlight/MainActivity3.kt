package com.example.greenlight_redlight

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity3 : AppCompatActivity() {
    lateinit var button: Button
    var playerName: String? = ""
    var roomName: String? = ""
    var role: String = ""
    var message: String = ""

    lateinit var database: FirebaseDatabase
    lateinit var messageRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        button = findViewById(R.id.button)
        button.isEnabled = false

        database = FirebaseDatabase.getInstance()

        val preferences: SharedPreferences = getSharedPreferences("PREFS", 0)
        playerName = preferences.getString("playerName", "")

        var extras: Bundle? = intent.extras
        if (extras != null) {
            roomName = extras.getString("roomName")
            if (roomName.equals(playerName)) {
                role = "host"
            } else {
                role = "guest"
            }
        }

        button.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View) {
                button.isEnabled = false
                message = "$role:Poked!"
                messageRef.setValue(message)
            }
        })

        messageRef = database.getReference("rooms/$roomName/message")
        message = "$role:Poked!"
        messageRef.setValue(message)
        addRoomEventListener()
    }

    private fun addRoomEventListener() {
        messageRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(role == "host") {
                    if(dataSnapshot.getValue(String::class.java)?.contains("guest:") as Boolean) {
                        button.isEnabled = true
                        Toast.makeText(this@MainActivity3, "" +
                            dataSnapshot.getValue(String::class.java)!!.replace("guest:", ""),
                            Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if(dataSnapshot.getValue(String::class.java)?.contains("host:") as Boolean) {
                        button.isEnabled = true
                        Toast.makeText(this@MainActivity3, "" +
                                dataSnapshot.getValue(String::class.java)!!.replace("host:", ""),
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                messageRef.setValue(message)
            }
        })
    }
}