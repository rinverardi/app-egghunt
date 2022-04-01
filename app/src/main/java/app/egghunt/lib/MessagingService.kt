package app.egghunt.lib

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import app.egghunt.competition.CompetitionRepo
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Actions.CHANGE_COMPETITION) {
                onChangeCompetition(intent)
            }
        }
    }

    var competition: DatabaseReference? = null

    private fun onChangeCompetition(intent: Intent) {
        val competitionDescription = intent.getStringExtra(Extras.COMPETITION_DESCRIPTION)!!
        val competitionTag = intent.getStringExtra(Extras.COMPETITION_TAG)!!

        competition = CompetitionRepo.sync(competitionDescription, competitionTag)
    }

    override fun onCreate() {
        super.onCreate()

        registerReceiver(broadcastReceiver, IntentFilter(Actions.CHANGE_COMPETITION))
    }

    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)

        super.onDestroy()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val intent = Intent()

        intent.action = Actions.NEW_MESSAGE

        intent.putExtra(Extras.BODY, message.notification!!.body)
        intent.putExtra(Extras.TITLE, message.notification!!.title)

        sendBroadcast(intent)
    }

    // @remo Implement me!

    override fun onNewToken(token: String) {
    }
}