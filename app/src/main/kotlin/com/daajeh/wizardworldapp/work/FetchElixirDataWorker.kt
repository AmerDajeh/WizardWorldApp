package com.daajeh.wizardworldapp.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.daajeh.wizardworldapp.data.local.dao.ElixirDao
import com.daajeh.wizardworldapp.data.local.dao.IngredientDao
import com.daajeh.wizardworldapp.data.local.dao.InventorDao
import com.daajeh.wizardworldapp.data.network.WizardWorldApi
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.getOrCreateScope
import org.koin.core.scope.Scope

class FetchElixirDataWorker(
    context: Context,
    private val parameters: WorkerParameters
) : CoroutineWorker(context, parameters), KoinScopeComponent {

    override val scope: Scope by getOrCreateScope()

    private val dao by scope.inject<ElixirDao>()
    private val ingredientDao by scope.inject<IngredientDao>()
    private val inventorDao by scope.inject<InventorDao>()
    private val api by scope.inject<WizardWorldApi>()

    override suspend fun doWork(): Result {
        try {
            val elixirId = parameters.inputData.getString(ELIXIR_ID_PARAMETER_NAME)
                ?: return Result.failure(workDataOf("message" to "can't find wizard id."))

            dao.getById(elixirId).first()
                ?.let {
                    return Result.success(workDataOf())
                }

            val localLightElixir = dao.getLightElixirById(elixirId)
                ?: throw RuntimeException("$NAME: can't find light version")

            val remoteData = api.getElixirDetails(elixirId)
            val entity = remoteData.toEntity(localLightElixir.wizardId)
            dao.insert(entity)

            val ingredients = remoteData.ingredients
                .map { it.toEntity(elixirId) }
            val inventors = remoteData.inventors
                .map { it.toEntity(elixirId) }

            ingredients.forEach { ingredientDao.insert(it) }
            inventors.forEach { inventorDao.insert(it) }

            return Result.success(workDataOf("message" to "successfully fetched elixir data"))
        } catch (e: Exception) {
            return Result.failure(workDataOf("message" to (e.message ?: "something went wrong")))
        }
    }


    companion object {
        private const val ELIXIR_ID_PARAMETER_NAME = "elixir_id"
        private const val NAME = "FetchElixirWorker"

        fun enqueue(context: Context, elixirId: String) {
            val data = Data.Builder()
                .putString(ELIXIR_ID_PARAMETER_NAME, elixirId)
                .build()

            val constraint = Constraints.Builder()
                .apply { setRequiredNetworkType(NetworkType.CONNECTED) }
                .build()

            val request = OneTimeWorkRequest.Builder(FetchElixirDataWorker::class.java)
                .setConstraints(constraint)
                .setInputData(data)

            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    NAME,
                    ExistingWorkPolicy.APPEND,
                    request.build()
                )
        }
    }
}