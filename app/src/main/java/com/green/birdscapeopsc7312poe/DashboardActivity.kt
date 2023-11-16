package com.green.birdscapeopsc7312poe

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.green.birdscapeopsc7312poe.activities.FirstActivity
import com.green.birdscapeopsc7312poe.dataclass.Bird
import com.green.birdscapeopsc7312poe.location.MapboxActivity
import com.green.birdscapeopsc7312poe.observation.ObservationActivity
import com.green.birdscapeopsc7312poe.settings.SettingsActivity

class DashboardActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize UI elements
        val btnLogouts = findViewById<Button>(R.id.btnLogout)
        val btnLocateBirds = findViewById<Button>(R.id.btnLocateBirds)
        val btnObservations = findViewById<Button>(R.id.btnObservations)
        val btnExplore = findViewById<Button>(R.id.btnExplore)
        val btnKnowledge = findViewById<Button>(R.id.btnBirdFeeding)

        // Button click listeners

        // Logout button click listener
        btnLogouts.setOnClickListener()
        {
            btnSignOut() // Calls the function to sign out the user
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // Redirects to the main activity (login page)
        }

        // Locate Birds button click listener
        btnLocateBirds.setOnClickListener()
        {
            val intent = Intent(this, MapboxActivity::class.java)
            startActivity(intent) // Redirects to the Mapbox activity for locating birds
        }

        // Observations button click listener
        btnObservations.setOnClickListener()
        {
            val intent = Intent(this, ObservationActivity::class.java)
            startActivity(intent) // Redirects to the Observation activity
        }

        // Explore button click listener
        btnExplore.setOnClickListener()
        {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent) // Redirects to the settings activity
        }

        // Knowledge button click listener
        btnKnowledge.setOnClickListener()
        {
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent) // Redirects to the knowledge activity
        }

        // Display a random bird when the activity is created
        displayRandomBird()
    }

    // Bird data
    private val birds = listOf(
        // ... (list of bird objects with name, description, and image)
        Bird("African Penguin", "African Penguins, have a black-and-white plumage with a pink patch of skin around their eyes.", R.drawable.africanpenguin),
        Bird("Cape Robin-Chat", "The Cape Robin-Chat is a small, charming bird with a striking orange breast and a melodious song.", R.drawable.caperobinchat),
        Bird("Secretary Bird", "A large bird of prey with long legs, it's known for its distinctive appearance and hunting style.", R.drawable.secretarybird),
        Bird("Crowned Crane", "A regal bird with a distinctive crown of golden feathers on its head.", R.drawable.crownedcrane),
        Bird("African Hoopoe", "Known for its distinctive 'oop-oop' call and a crest of feathers on its head.", R.drawable.africanhoopoe),
        Bird("Pied Kingfisher", "A black-and-white kingfisher often seen hovering over water before diving for prey.", R.drawable.piedkingfisher),
        Bird("Marabou Stork", "A large, scavenging bird with a naked head and a massive bill.", R.drawable.maraboustork),
        Bird("Greater Flamingo", "These tall, pink birds are often found in saltwater lagoons and estuaries.", R.drawable.greaterflamingo),
        Bird("African Jacana", "A wader bird known for its long toes and striking appearance.", R.drawable.africanjacana),
        Bird("Crowned Lapwing", "A striking bird with a distinctive crown-like crest on its head.", R.drawable.crownedlapwing),
        )

    // Function to display a random bird
    private fun displayRandomBird() {
        val randomIndex = (birds.indices).random()
        val randomBird = birds[randomIndex]

        // Update the ImageView
        val birdImageView = findViewById<ImageView>(R.id.birdImageView)
        birdImageView.setImageResource(randomBird.imageResId)

        // Update the TextViews
        val birdNameTextView = findViewById<TextView>(R.id.birdNameTextView)
        val birdDescriptionTextView = findViewById<TextView>(R.id.birdDescriptionTextView)
        birdNameTextView.text = randomBird.name
        birdDescriptionTextView.text = randomBird.description
    }

    // Function to sign out the user
    private fun btnSignOut() {
        Firebase.auth.signOut() // Sign out the user using Firebase Authentication
        Toast.makeText(this, "Goodbye User: Signed Out", Toast.LENGTH_LONG).show() // Display a toast message indicating the user is signed out
    }
}