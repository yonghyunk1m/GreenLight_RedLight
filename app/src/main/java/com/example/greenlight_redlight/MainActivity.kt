package com.example.greenlight_redlight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.greenlight_redlight.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var NextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        NextButton = binding.button

        NextButton.setOnClickListener({
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent) // Transition to the next(MainActivity2) window
            finish() // CLOSE current(MainActivity) window
        })

    // Develop1
    }
}
//TEST