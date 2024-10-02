package com.daajeh.wizardworldapp.data

import com.daajeh.wizardworldapp.data.local.WizardWorldDatabase
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.entity.Elixir
import com.daajeh.wizardworldapp.domain.entity.Ingredient
import com.daajeh.wizardworldapp.domain.entity.Inventor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class ElixirRepositoryImpl(
    private val database: WizardWorldDatabase
): ElixirRepository {

    override fun getElixirs(): Flow<List<Elixir>> =
        flowOf(listOf( Elixir(
            characteristics = "Restores health and boosts energy",
            difficulty = "Medium",
            effect = "Healing and energy boost",
            id = "1",
            ingredients = listOf(
                Ingredient(id = "1", name = "Herb"),
                Ingredient(id = "2", name = "Magic Water")
            ),
            inventors = listOf(
                Inventor(firstName = "John", lastName = "Doe", id = "1"),
                Inventor(firstName = "Jane", lastName = "Smith", id = "2")
            ),
            manufacturer = "Herb Co.",
            name = "Healing Elixir",
            sideEffects = "Mild nausea",
            time = "5 minutes"
        )))
    override fun getElixirById(elixirId: String): Flow<Elixir?> =
        if (elixirId == "1")
            flowOf(
                Elixir(
                    characteristics = "Restores health and boosts energy",
                    difficulty = "Medium",
                    effect = "Healing and energy boost",
                    id = "1",
                    ingredients = listOf(
                        Ingredient(id = "1", name = "Herb"),
                        Ingredient(id = "2", name = "Magic Water")
                    ),
                    inventors = listOf(
                        Inventor(firstName = "John", lastName = "Doe", id = "1"),
                        Inventor(firstName = "Jane", lastName = "Smith", id = "2")
                    ),
                    manufacturer = "Herb Co.",
                    name = "Healing Elixir",
                    sideEffects = "Mild nausea",
                    time = "5 minutes"
                )
            )
    else
        emptyFlow()
}