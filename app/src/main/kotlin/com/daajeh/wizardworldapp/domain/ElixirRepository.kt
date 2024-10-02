package com.daajeh.wizardworldapp.domain

import com.daajeh.wizardworldapp.domain.entity.Elixir
import kotlinx.coroutines.flow.Flow

interface ElixirRepository {

    fun getElixirs(): Flow<List<Elixir>>
    fun getElixirById(elixirId: String): Flow<Elixir?>
}