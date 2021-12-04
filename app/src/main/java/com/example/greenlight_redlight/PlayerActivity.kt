package com.example.greenlight_redlight

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast

import com.example.greenlight_redlight.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var AccelerometerSensor : Sensor ? = null
    private var CurrentData = 0f
    private var PreviousData = 0f
    private var difference = 0f
    private var threshold = 2f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.apply{
            AccelerometerSensor = getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        }

        //binding.redBackground.setBackgroundResource(R.drawable.red02_background)

    }
    override fun onResume() {
        super.onResume()
        Log.d("onResume","onResume")
        sensorManager.apply{
            registerListener(this@PlayerActivity, AccelerometerSensor, 1)
        }
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            val binding = ActivityPlayerBinding.inflate(layoutInflater)
            setContentView(binding.root)

            CurrentData = event.values[0] + event.values[1] + event.values[2]
            difference = Math.abs(CurrentData - PreviousData)
            if (PreviousData!=0f){
                if (difference < threshold/7) {
                    binding.redBackground.setBackgroundResource(R.drawable.red01_background)
                }
                else if (difference < threshold/6) {
                    binding.redBackground.setBackgroundResource(R.drawable.red02_background)
                }
                else if (difference < threshold/5) {
                    binding.redBackground.setBackgroundResource(R.drawable.red03_background)
                }
                else if (difference < threshold/4) {
                    binding.redBackground.setBackgroundResource(R.drawable.red04_background)
                }
                else if (difference < threshold/3) {
                    binding.redBackground.setBackgroundResource(R.drawable.red05_background)
                }
                else if (difference < threshold/2) {
                    binding.redBackground.setBackgroundResource(R.drawable.red06_background)
                }
                else if (difference < threshold) {
                    binding.redBackground.setBackgroundResource(R.drawable.red07_background)
                }
                else{
                    // Change to Failed Activity
                    val intent = Intent(this, FailActivity::class.java)
                    startActivity(intent) // Transition to the next(MainActivity2) window
                    finish() // CLOSE current(MainActivity) window
                    sensorManager.unregisterListener(this)
                }
            }
            PreviousData = CurrentData
        }
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