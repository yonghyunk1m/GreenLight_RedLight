package com.example.greenlight_redlight

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class TaggerActivity : AppCompatActivity() {
    lateinit var button: Button
    var playerName: String? = ""
    var roomName: String? = ""
    var message: String = ""

    lateinit var database: FirebaseDatabase
    lateinit var messageRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tagger)

        button = findViewById(R.id.button)
        button.isEnabled = true

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

        messageRef = database.getReference("rooms/$roomName/message")
        message = "host"
        messageRef.setValue(message)
        addRoomEventListener()
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

    private var doubleBackToExit = false
    override fun onBackPressed() {
        if (doubleBackToExit) {
            var roomRef = database.getReference("rooms/$playerName")
            roomRef.removeValue()
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