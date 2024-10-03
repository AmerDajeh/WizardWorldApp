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
import com.daajeh.wizardworldapp.domain.WizardRepository
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope

class FetchWizardDataWorker(
    context: Context,
    private val parameters: WorkerParameters
) : CoroutineWorker(context, parameters), KoinScopeComponent {

    override val scope: Scope
        get() = createScope()

    private val wizardRepository by scope.inject<WizardRepository>()

    override suspend fun doWork(): Result {
        try {
            val wizardId = parameters.inputData.getString(WIZARD_ID_PARAMETER_NAME)
                ?: return Result.failure(workDataOf("message" to "can't find wizard id."))

            wizardRepository.fetchWizardNetworkData(wizardId)
                .onFailure { throw it }

            return Result.success(workDataOf("message" to "successfully fetched wizard data"))
        } catch (e: Exception) {
            return Result.failure(workDataOf("message" to (e.message ?: "something went wrong")))
        }
    }


    companion object {
        private const val WIZARD_ID_PARAMETER_NAME = "wizard_id"
        private const val NAME = "FetchWizardWorker"

        fun enqueue(context: Context, wizardId: String) {
            val data = Data.Builder()
                .putString(WIZARD_ID_PARAMETER_NAME, wizardId)
                .build()

            val constraint = Constraints.Builder()
                .apply { setRequiredNetworkType(NetworkType.CONNECTED) }
                .build()

            val request = OneTimeWorkRequest.Builder(FetchWizardDataWorker::class.java)
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