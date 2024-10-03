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
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.WizardRepository
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope

class UpdateDataWorker(
    context: Context,
    parameters: WorkerParameters
): CoroutineWorker(context, parameters), KoinScopeComponent {

    override val scope: Scope
        get() = createScope()

    private val wizardRepository by scope.inject<WizardRepository>()

    override suspend fun doWork(): Result =
        try {
            wizardRepository.fetchNetworkData()
               .onFailure { throw it }
            Result.success(workDataOf("message" to "successfully update the cache"))
        } catch (e: Exception){
            Result.failure(workDataOf("message" to (e.message ?: "something went wrong")))
        }


    companion object {
        private const val NAME = "UpdateDataWorker"
        fun enqueue(context: Context) {
            val constraint = Constraints.Builder()
                .apply { setRequiredNetworkType(NetworkType.CONNECTED) }
                .build()

            val request = OneTimeWorkRequest.Builder(UpdateDataWorker::class.java)
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