package com.green.birdscapeopsc7312poe.observation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.green.birdscapeopsc7312poe.DashboardActivity
import com.green.birdscapeopsc7312poe.MainActivity
import com.green.birdscapeopsc7312poe.R
import com.green.birdscapeopsc7312poe.adapter.BirdObservationAdapter
import com.green.birdscapeopsc7312poe.databinding.ActivityRecyclerviewBinding
import com.green.birdscapeopsc7312poe.dataclass.UserObservation

class ObservationActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityRecyclerviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Use View Binding to inflate the layout
        binding = ActivityRecyclerviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Read data from Firebase
        readData()

        // Get reference to the button
        val buttonAddObservation = findViewById<Button>(R.id.buttonAddObservation)

        // Set OnClickListener for the button
        buttonAddObservation.setOnClickListener {
            val intent = Intent(this, AddObservationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun readData() {
        Log.d("ObservationActivity", "readData() called")

        database.child("observations").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val observationList = mutableListOf<UserObservation>()

                for (childSnapshot in snapshot.children) {
                    val observation = childSnapshot.getValue(UserObservation::class.java)
                    observation?.let {
                        observationList.add(it)
                    }
                }

                //reference: https://developer.android.com/topic/libraries/data-binding/binding-adapters

                val adapter = BirdObservationAdapter(observationList)
                binding.recyclerView.adapter = adapter
                adapter.notifyDataSetChanged() // Notify adapter about the data change

                showToast("Data loaded successfully!")
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Failed to load data. Please try again.")
            }
        })
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
