package com.green.birdscapeopsc7312poe.activities


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.green.birdscapeopsc7312poe.R
import com.green.birdscapeopsc7312poe.DashboardActivity
import com.green.birdscapeopsc7312poe.adapter.BirdAdapter
import com.green.birdscapeopsc7312poe.dataclass.BirdObservation
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class FirstActivity : AppCompatActivity() {
    private lateinit var birdRecyclerView: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ebird)

        val btnOBSER = findViewById<Button>(R.id.bottomButton)

        birdRecyclerView = findViewById(R.id.birdRecyclerView)
        birdRecyclerView.layoutManager = LinearLayoutManager(this)

        // Logout button click listener
        btnOBSER.setOnClickListener()
        {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent) // Redirects to the main activity (login page)
        }

        fetchBirdObservations()
    }

    private fun fetchBirdObservations() {
        Thread {
            try {
                val apiKey = "5ia6u8qs174j" // Replace with your eBird API key
                val url = URL("https://api.ebird.org/v2/data/obs/ZA/recent")
                val connection = url.openConnection() as HttpURLConnection

                connection.setRequestProperty("x-ebirdapitoken", apiKey)

                if (connection.responseCode == 200) {
                    val inputSystem = connection.inputStream
                    val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                    val response = Gson().fromJson(inputStreamReader, Array<BirdObservation>::class.java)

                    runOnUiThread {
                        birdRecyclerView.adapter = BirdAdapter(response.toList())
                    }

                    inputStreamReader.close()
                    inputSystem.close()
                } else {
                    runOnUiThread {
                        // Handle failed connection
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    // Handle other exceptions
                }
            }
        }.start()
    }
}
