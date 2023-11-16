package com.green.birdscapeopsc7312poe.dataclass

data class UserObservation(
    var birdName: String = "",
    var birdSpecies: String = "",
    var birdDescription: String = "",
    var longitude: Double = 0.0,
    var latitude: Double = 0.0,
    var imageUrl: String = ""
) {
    constructor() : this("", "", "", 0.0, 0.0, "")
}
