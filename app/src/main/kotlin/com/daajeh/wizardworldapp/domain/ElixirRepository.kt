package com.daajeh.wizardworldapp.domain

import com.daajeh.wizardworldapp.domain.entity.Elixir
import com.daajeh.wizardworldapp.domain.entity.LightElixir
import kotlinx.coroutines.flow.Flow

interface ElixirRepository {

    fun getElixirs(): Flow<List<LightElixir>>
    fun getElixirById(elixirId: String): Flow<Elixir?>

    suspend fun saveFavourite(elixirId: String)
    suspend fun removeFavourite(elixirId: String)
}