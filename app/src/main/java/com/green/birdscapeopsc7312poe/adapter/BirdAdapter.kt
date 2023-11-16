package com.green.birdscapeopsc7312poe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.green.birdscapeopsc7312poe.R
import com.green.birdscapeopsc7312poe.dataclass.BirdObservation

class BirdAdapter(private val birdList: List<BirdObservation>) :
    RecyclerView.Adapter<BirdAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val birdNameTextView: TextView = itemView.findViewById(R.id.birdNameTextView)
        val sciNameTextView: TextView = itemView.findViewById(R.id.sciNameTextView)
        val locNameTextView: TextView = itemView.findViewById(R.id.locNameTextView)
        val obsDtTextView: TextView = itemView.findViewById(R.id.obsDtTextView)
        val howManyTextView: TextView = itemView.findViewById(R.id.howManyTextView)
        val latTextView: TextView = itemView.findViewById(R.id.latTextView)
        val lngTextView: TextView = itemView.findViewById(R.id.lngTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the layout for each bird list item
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.bird_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Retrieve the BirdObservation object for the current position
        val bird = birdList[position]

        // Set the text for each TextView in the ViewHolder
        holder.birdNameTextView.text = "Common Name: ${bird.comName}"
        holder.sciNameTextView.text = "Scientific Name: ${bird.sciName}"
        holder.locNameTextView.text = "Location: ${bird.locName}"
        holder.obsDtTextView.text = "Observation Date: ${bird.obsDt}"
        holder.howManyTextView.text = "How Many: ${bird.howMany}"
        holder.latTextView.text = "Latitude: ${bird.lat}"
        holder.lngTextView.text = "Longitude: ${bird.lng}"
    }

    override fun getItemCount(): Int {
        // Return the size of the birdList
        return birdList.size
    }
}
