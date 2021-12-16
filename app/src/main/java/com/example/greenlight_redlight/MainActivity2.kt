package com.example.greenlight_redlight

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import com.example.greenlight_redlight.databinding.ActivityMain2Binding
import com.google.firebase.database.*

class MainActivity2 : AppCompatActivity() {

    private lateinit var PlayerButton: ImageButton
    private lateinit var TaggerButton: ImageButton
    private lateinit var BackButton: ImageButton

    var playerName: String? = "";
    var roomName: String? = "";
    lateinit var roomsList: MutableList<String>;

    lateinit var database: FirebaseDatabase
    lateinit var roomRef: DatabaseReference
    lateinit var usersRef: DatabaseReference
    lateinit var roomsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        PlayerButton = binding.playerButton
        TaggerButton = binding.taggerButton
        BackButton = binding.backButton

        database = FirebaseDatabase.getInstance()
        val preferences: SharedPreferences = getSharedPreferences("PREFS", 0)
        playerName = preferences.getString("playerName", "")
        roomName = playerName

        roomsList = mutableListOf();

        PlayerButton.setOnClickListener({
            val intent = Intent(this, RoomActivity::class.java)
            startActivity(intent) // Transition to the next(MainActivity2) window
            finish() // CLOSE current(MainActivity) window
        })

        TaggerButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                roomName = playerName
                roomRef = database.getReference("rooms/$roomName/player1")
                usersRef = database.getReference("rooms/$roomName/users")
                addRoomEventListener();
                roomRef.setValue(playerName)
                usersRef.setValue(1)
            }
        })
        addRoomsEventListener()

        /*
        TaggerButton.setOnClickListener({
            val intent = Intent(this, TaggerActivity::class.java)
            startActivity(intent) // Transition to the next(MainActivity2) window
            finish() // CLOSE current(MainActivity) window
        })*/

        BackButton.setOnClickListener({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // Transition to the next(MainActivity2) window
            finish() // CLOSE current(MainActivity) window
        })
    }

    private fun addRoomEventListener() {
        roomRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var intent = Intent(applicationContext, TaggerActivity::class.java)
                intent.putExtra("roomName", roomName)
                startActivity(intent)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MainActivity2, "Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addRoomsEventListener() {
        roomsRef = database.getReference("rooms")
        roomsRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                roomsList.clear();
                var rooms: Iterable<DataSnapshot> = dataSnapshot.children;
                for(snapshot: DataSnapshot in rooms) {
                    snapshot.key?.let { roomsList.add(it) }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                //nothing
            }
        })
    }

    // Back Press Caution Function //
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