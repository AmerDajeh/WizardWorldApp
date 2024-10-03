package com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.inventor

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.daajeh.wizardworldapp.domain.entity.Inventor
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "inventors")
data class InventorEntity(
    @PrimaryKey val id: String,
    val firstName: String = "",
    val lastName: String = "",
    val elixirId: String
) {

    fun toDomain() =
        Inventor(
            firstName = this.firstName,
            lastName = this.lastName,
            id = this.id
        )
}
