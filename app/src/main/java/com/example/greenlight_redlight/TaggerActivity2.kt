package com.example.greenlight_redlight

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.example.greenlight_redlight.databinding.ActivityTagger2Binding
import com.google.firebase.database.*

class TaggerActivity2 : AppCompatActivity() {

    private lateinit var GreenButton: ImageButton
    private lateinit var RedButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tagger2)

        val binding = ActivityTagger2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        GreenButton = binding.greenButton
        RedButton = binding.redButton

        GreenButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                binding.background.setBackgroundResource(R.drawable.tagger_green_background)
            }
        })

        RedButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                binding.background.setBackgroundResource(R.drawable.tagger_red_background)
            }
        })


    }




}