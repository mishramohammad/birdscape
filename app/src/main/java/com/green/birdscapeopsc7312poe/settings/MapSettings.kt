package com.green.birdscapeopsc7312poe.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.green.birdscapeopsc7312poe.DashboardActivity
import com.green.birdscapeopsc7312poe.R
import com.green.birdscapeopsc7312poe.location.MapboxActivity

class MapSettings : AppCompatActivity() {

    // distance range every 5 intervals to display in spinner for max distance displayed
    val distance = arrayOf("5-10", "10-15", "20-35", "40-45", "50-55")

    private lateinit var auth: FirebaseAuth
    private lateinit var spinner: Spinner // Move the spinner declaration here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_settings)

        auth = Firebase.auth

        // Declaration of xml UI elements such as spinner for unit selection, and the different buttons
        val unitSelect = findViewById<RadioGroup>(R.id.radioGroupUnitSelect)
        val metricSelect = findViewById<RadioButton>(R.id.radioButtonMetric)
        val imperialSelect = findViewById<RadioButton>(R.id.radioButtonImperial)
        val dashboard = findViewById<Button>(R.id.btnDashboard)
        val btnSave = findViewById<Button>(R.id.btnSaveSettings)
        spinner = findViewById<Spinner>(R.id.spinnerMaxDistance)

        // Set up the adapter outside the setOnClickListener block
        val arrayAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, distance)
        spinner.adapter = arrayAdapter

        btnSave.setOnClickListener {
            val savebtn: Int = unitSelect!!.checkedRadioButtonId
            val btnSave = findViewById<RadioButton>(savebtn)

            // Now you can access the selected item without any issues
            val position = spinner.selectedItemPosition
            val selectedDistance = distance[position]
            val selectedUnit = btnSave.text.toString()

            Toast.makeText(
                applicationContext,
                "Selected distance: $selectedDistance, Selected unit: $selectedUnit",
                Toast.LENGTH_SHORT
            ).show()

            // Call your Firebase save logic here, using selectedDistance and selectedUnit
            saveToFirebase(selectedDistance, selectedUnit)
        }

        dashboard.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveToFirebase(selectedDistance: String, selectedUnit: String) {
        // Get the user's UID from Firebase Auth
        val uid = auth.currentUser?.uid

        // Save settings to Firebase Database
        val database = Firebase.database
        val settingsRef = database.getReference("users/$uid/settings")

        // Generate a unique key for each entry
        val entryKey = settingsRef.push().key

        if (entryKey != null) {
            // Use the generated key to create a new path for the entry
            val entryRef = settingsRef.child(entryKey)

            // Use updateChildren to update specific fields without overwriting the entire node
            val settingsData = mapOf(
                "distance" to selectedDistance,
                "unit" to selectedUnit
            )

            // Save the settings under the unique key
            entryRef.setValue(settingsData)
                .addOnSuccessListener {
                    Toast.makeText(this@MapSettings, "Settings saved successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@MapSettings, "Failed to save settings", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Handle the case where generating a key failed
            Toast.makeText(this@MapSettings, "Failed to generate a unique key", Toast.LENGTH_SHORT).show()
        }
    }
}
