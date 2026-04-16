package com.coditos.splitmeet.core.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.coditos.splitmeet.features.home.domain.repositories.HomeRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class OutingSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val homeRepository: HomeRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.d("OutingSyncWorker", "Starting background sync...")
            // The HomeRepository implementation will fetch latest from API
            // and save to Room, effectively syncing changes that happened remotely
            // or catching up if local was offline but user was navigating.
            homeRepository.syncOutings()
            Log.d("OutingSyncWorker", "Background sync completed successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e("OutingSyncWorker", "Error during background sync", e)
            Result.retry()
        }
    }
}
