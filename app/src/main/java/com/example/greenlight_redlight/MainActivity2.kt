package com.example.greenlight_redlight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.example.greenlight_redlight.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    private lateinit var PlayerButton: ImageButton
    private lateinit var TaggerButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        PlayerButton = binding.playerButton
        TaggerButton = binding.taggerButton

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
    }
}