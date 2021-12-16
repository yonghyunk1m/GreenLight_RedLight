package com.example.greenlight_redlight

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class RoomActivity : AppCompatActivity() {
    lateinit var listView: ListView;
    lateinit var roomsList: MutableList<String>;

    var number: Any = 0;
    var prevNumber: Any = 0;
    var playerName: String? = "";
    var roomName: String? = "";

    lateinit var database: FirebaseDatabase
    lateinit var roomRef: DatabaseReference
    lateinit var usersRef: DatabaseReference
    lateinit var roomsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        database = FirebaseDatabase.getInstance()
        val preferences: SharedPreferences = getSharedPreferences("PREFS", 0)
        playerName = preferences.getString("playerName", "")
        roomName = playerName

        listView = findViewById(R.id.listView)

        roomsList = mutableListOf();

        listView.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                roomName = roomsList.get(position)
                usersRef = database.getReference("rooms/$roomName/users")
                prevNumber = number
                usersRef.get().addOnSuccessListener {
                    number = it.value as Long + 1
                    roomRef = database.getReference("rooms/$roomName/player${number}")
                    addRoomEventListener();
                    roomRef.setValue(playerName)
                    usersRef.setValue(number)
                }.addOnCanceledListener {
                    // nothing
                }
            }
        })

        addRoomsEventListener()
    }

    private fun addRoomEventListener() {
        roomRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(number != prevNumber) {
                    prevNumber = number
                    var intent = Intent(applicationContext, PlayerActivity::class.java)
                    Log.d("********", "$number")
                    intent.putExtra("roomName", roomName)
                    intent.putExtra("playerNumber", number as Long)
                    startActivity(intent)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@RoomActivity, "Error!", Toast.LENGTH_SHORT).show()
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
                    var adapter: ArrayAdapter<String> = ArrayAdapter(this@RoomActivity,
                        android.R.layout.simple_list_item_1, roomsList)
                    listView.adapter = adapter
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                //nothing
            }
        })
    }
}

