package com.green.birdscapeopsc7312poe.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.green.birdscapeopsc7312poe.R
import com.green.birdscapeopsc7312poe.location.MapboxActivity
import com.green.birdscapeopsc7312poe.DashboardActivity
import com.green.birdscapeopsc7312poe.view.WeatherActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val dashboardButton = findViewById<Button>(R.id.btn_dashboard)
        val mapButton = findViewById<Button>(R.id.btn_map_settings)
        val weatherButton = findViewById<Button>(R.id.btn_weather_settings)
        dashboardButton.setOnClickListener()
        {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent) // Redirects to the dashboard activity
        }
        mapButton.setOnClickListener()
        {
            val intent = Intent(this,MapSettings::class.java)
            startActivity(intent)// redirects to map_activity settings page
        }

        weatherButton.setOnClickListener()
        {
            val intent = Intent(this,WeatherActivity::class.java)
            startActivity(intent)// redirects to map_activity settings page
        }
    }
}