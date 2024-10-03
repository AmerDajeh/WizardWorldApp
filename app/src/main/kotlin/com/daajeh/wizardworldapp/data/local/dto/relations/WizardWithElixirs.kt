package com.daajeh.wizardworldapp.data.local.dto.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.ElixirEntity
import com.daajeh.wizardworldapp.data.local.dto.wizard.WizardEntity

data class WizardWithElixirs(
    @Embedded val wizard: WizardEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "wizardId"
    )
    val elixirs: List<ElixirEntity>
)
