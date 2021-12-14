package com.example.greenlight_redlight

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/*
class LoginActivity : AppCompatActivity() {
    lateinit var emailEt: EditText
    lateinit var passwordEt: EditText
    lateinit var loginBtn: Button

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        emailEt = findViewById(R.id.email_et)
        passwordEt = findViewById(R.id.pwd_et)
        loginBtn = findViewById(R.id.button)

        loginBtn.setOnClickListener {
            var email = emailEt.text.toString()
            var password = passwordEt.text.toString()
            auth.createUserWithEmailAndPassword(email,password) // 회원 가입
                .addOnCompleteListener {
                        result ->
                    if(result.isSuccessful){
                        Toast.makeText(this,"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                        if(auth.currentUser!=null){
                            var intent = Intent(this, RoomActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    else if(result.exception?.message.isNullOrEmpty()){
                        Toast.makeText(this,"오류가 발생했습니다.",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        login(email,password)
                    }
                }
        }
    }

    fun login(email:String,password:String){
        auth.signInWithEmailAndPassword(email,password) // 로그인
            .addOnCompleteListener {
                    result->
                if(result.isSuccessful){
                    var intent = Intent(this, RoomActivity::class.java)
                    startActivity(intent)
                }
            }
    }
}
 */

class LoginActivity : AppCompatActivity() {
    lateinit var editText: EditText
    lateinit var button: Button

    var playerName: String? = ""
    lateinit var database: FirebaseDatabase
    lateinit var playerRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editText = findViewById(R.id.editText)
        button = findViewById(R.id.button)

        database = FirebaseDatabase.getInstance()

        var preferences: SharedPreferences = getSharedPreferences("PREFS", 0)
        playerName = preferences.getString("playerName", "")
        if(!playerName.equals("")) {
            playerRef = database.getReference("players/$playerName")
            addEventListener()
            playerRef.setValue("")
        }
        button.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View) {
                playerName = editText.getText().toString()
                editText.setText("")
                if(!playerName.equals("")) {
                    button.setText("LOGGING IN")
                    button.isEnabled = false
                    playerRef = database.getReference("players/$playerName")
                    addEventListener()
                    playerRef.setValue("")
                }
            }
        })
    }

    private fun addEventListener() {
        playerRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(!playerName.equals("")) {
                    var preferences: SharedPreferences = getSharedPreferences("PREFS", 0)
                    var editor: SharedPreferences.Editor = preferences.edit()
                    editor.putString("playerName", playerName)
                    editor.apply()

                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                button.setText("LOG IN")
                button.isEnabled = true
                Toast.makeText(this@LoginActivity, "Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}