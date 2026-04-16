package com.coditos.splitmeet.core.network.fcm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.api.DetailOutingApi
import com.coditos.splitmeet.features.detailOuting.data.datasources.remote.model.ConfirmParticipationRequest
import com.coditos.splitmeet.features.group.data.datasources.remote.api.GroupApi
import com.coditos.splitmeet.features.group.data.datasources.remote.model.RespondInvitationRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var detailOutingApi: DetailOutingApi

    @Inject
    lateinit var groupApi: GroupApi

    companion object {
        const val ACTION_ACCEPT = "com.coditos.splitmeet.ACTION_ACCEPT"
        const val ACTION_REJECT = "com.coditos.splitmeet.ACTION_REJECT"
        
        const val EXTRA_TYPE = "extra_type" // "group_invitation" or "outing_invitation"
        const val EXTRA_ENTITY_ID = "extra_entity_id"
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
        
        private const val CHANNEL_ID = "splitmeet_alerts_high"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        val type = intent.getStringExtra(EXTRA_TYPE) ?: return
        val entityId = intent.getStringExtra(EXTRA_ENTITY_ID)?.toLongOrNull() ?: return
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)
        
        if (notificationId == -1) return

        val isAccepting = action == ACTION_ACCEPT

        // Evadir restricciones ANR/Limites del Receiver
        val pendingResult = goAsync()
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        
        scope.launch {
            try {
                if (type == "group_invitation") {
                    groupApi.respondInvitation(entityId, RespondInvitationRequest(isAccepting))
                } else if (type == "outing_invitation") {
                    detailOutingApi.confirmParticipation(entityId, ConfirmParticipationRequest(isAccepting))
                }
                
                // Actualizar UX visual sin abrir la aplicacion
                updateNotificationContent(context, notificationId, isAccepting)
            } catch (e: Exception) {
                Log.e("NotificationAction", "Network Error API: ${e.message}")
            } finally {
                pendingResult.finish()
            }
        }
    }

    private fun updateNotificationContent(context: Context, notificationId: Int, accepted: Boolean) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        val statusText = if (accepted) "Invitación Aceptada" else "Invitación Rechazada"

        val updatedNotification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(com.coditos.splitmeet.R.mipmap.ic_launcher)
            .setContentTitle("SplitMeet")
            .setContentText(statusText)
            .setAutoCancel(true)
            .setTimeoutAfter(3000) // Desaparece solita tras 3 segundos
            .build()
            
        notificationManager.notify(notificationId, updatedNotification)
    }
}
