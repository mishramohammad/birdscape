package com.green.birdscapeopsc7312poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.green.birdscapeopsc7312poe.authentication.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the login button in the layout by its ID
        val btnLogin = findViewById<Button>(R.id.loginButton)

        // Set an OnClickListener to handle the button click
        btnLogin.setOnClickListener {
            // Create an Intent to navigate to the LoginActivity
            val intent = Intent(this, LoginActivity::class.java)

            // Start the LoginActivity when the button is clicked
            try {
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()

                // Handle the exception by displaying a toast message with an error message
                val errorMessage = "An error occurred: ${e.message}"
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
