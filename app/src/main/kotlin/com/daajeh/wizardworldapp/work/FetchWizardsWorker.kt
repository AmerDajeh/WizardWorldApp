package com.daajeh.wizardworldapp.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.daajeh.wizardworldapp.data.local.dao.WizardDao
import com.daajeh.wizardworldapp.data.network.WizardWorldApi
import com.daajeh.wizardworldapp.domain.ElixirRepository
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.getOrCreateScope
import org.koin.core.scope.Scope

class FetchWizardsWorker(
    context: Context,
    parameters: WorkerParameters
) : CoroutineWorker(context, parameters), KoinScopeComponent {

    override val scope: Scope by getOrCreateScope()

    private val wizardDao by scope.inject<WizardDao>()
    private val api by scope.inject<WizardWorldApi>()
    private val elixirRepository by scope.inject<ElixirRepository>()

    override suspend fun doWork(): Result =
        try {
            if (wizardDao.getWizards().first().isEmpty()) {
                api
                    .getWizards()
                    .onSuccess {
                        it.forEach { wizard ->
                            wizardDao.insert(wizard.toEntity())
                            elixirRepository.saveWizardLightElixirs(wizard.id, wizard.elixirs)
                        }
                    }
                Result.success(workDataOf())
            } else
                kotlin.Result.success(Unit)
            Result.success(workDataOf("message" to "successfully update the cache"))
        } catch (e: Exception) {
            Result.failure(workDataOf("message" to (e.message ?: "something went wrong")))
        }


    companion object {
        private const val NAME = "FetchWizardsWorker"
        fun enqueue(context: Context) {
            val constraint = Constraints.Builder()
                .apply { setRequiredNetworkType(NetworkType.CONNECTED) }
                .build()

            val request = OneTimeWorkRequest.Builder(FetchWizardsWorker::class.java)
                .setConstraints(constraint)

            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    NAME,
                    ExistingWorkPolicy.REPLACE,
                    request.build()
                )
        }
    }
}