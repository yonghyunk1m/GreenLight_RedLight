package com.example.greenlight_redlight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.Toast
import com.example.greenlight_redlight.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var StartButton: ImageButton
    private lateinit var CreditButton: ImageButton
    private lateinit var AppinfoButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        StartButton = binding.startButton
        CreditButton = binding.creditButton
        AppinfoButton = binding.appinfoButton

        StartButton.setOnClickListener({
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent) // Transition to the next(MainActivity2) window
            finish() // CLOSE current(MainActivity) window
        })
        CreditButton.setOnClickListener({
            val intent = Intent(this, CreditActivity::class.java)
            startActivity(intent) // Transition to the next(MainActivity2) window
            finish() // CLOSE current(MainActivity) window
        })
        AppinfoButton.setOnClickListener({
            val intent = Intent(this, AppinfoActivity::class.java)
            startActivity(intent) // Transition to the next(MainActivity2) window
            finish() // CLOSE current(MainActivity) window
        })
    // Develop1
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
//TEST