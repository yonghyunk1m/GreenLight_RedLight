package com.example.greenlight_redlight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.Toast
import com.example.greenlight_redlight.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    private lateinit var PlayerButton: ImageButton
    private lateinit var TaggerButton: ImageButton
    private lateinit var BackButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        PlayerButton = binding.playerButton
        TaggerButton = binding.taggerButton
        BackButton = binding.backButton

        PlayerButton.setOnClickListener({
            val intent = Intent(this, PlayerActivity::class.java)
            startActivity(intent) // Transition to the next(MainActivity2) window
            finish() // CLOSE current(MainActivity) window
        })

        TaggerButton.setOnClickListener({
            val intent = Intent(this, TaggerActivity::class.java)
            startActivity(intent) // Transition to the next(MainActivity2) window
            finish() // CLOSE current(MainActivity) window
        })

        BackButton.setOnClickListener({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // Transition to the next(MainActivity2) window
            finish() // CLOSE current(MainActivity) window
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