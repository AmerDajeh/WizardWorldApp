package com.daajeh.wizardworldapp.domain.entity

data class Wizard(
    val elixirs: List<LightElixir>,
    val firstName: String,
    val id: String,
    val lastName: String,
    val isFavorite: Boolean = false
) {

    fun getName() =
        "$firstName $lastName"


//    suspend fun getHousesBasedOnElixirs(): House? {
//        val elixirs = fetchElixirs()
//        val houses = fetchHouses()
//
//        return wizards.associateWith { wizard ->
//
//        }
//        // Assuming you have a way to get elixirs that the wizard has worked on
//        val wizardElixirs = getElixirsForWizard(wizard)
//
//        // Create a set to collect possible houses based on elixirs
//        val possibleHouses = wizardElixirs.mapNotNull { elixir ->
//            houses.find { house ->
//                matchesHouseWithElixir(house, elixir)
//            }
//        }
//
//        // You can return the first matching house or any logic you prefer
//        return possibleHouses.firstOrNull()
//    }
//
//    fun matchesHouseWithElixir(house: House, elixir: Elixir?): Boolean {
//        // Example logic to match based on characteristics and house properties
//        val elixirCharacteristics = elixir?.characteristics?.lowercase() ?: ""
//        val houseColors = house.houseColours?.toLowerCase() ?: ""
//
//        // Check if house colors match elixir characteristics
//        return houseColors in elixirCharacteristics || elixir.animal?.toLowerCase() == house.animal?.toLowerCase()
//    }

}