package com.example.greenlight_redlight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.greenlight_redlight.databinding.ActivityAppinfoBinding

class AppinfoActivity : AppCompatActivity() {

    private lateinit var BackButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appinfo)

        val binding = ActivityAppinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        BackButton = binding.backButton

        BackButton.setOnClickListener({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // Transition to the next(MainActivity2) window
            finish() // CLOSE current(MainActivity) window
        })
    }
}