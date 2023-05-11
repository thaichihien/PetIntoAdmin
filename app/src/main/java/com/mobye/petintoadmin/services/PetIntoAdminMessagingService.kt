package com.mobye.petintoadmin.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.views.MainActivity

class PetIntoAdminMessagingService : FirebaseMessagingService() {

    private val TAG = "PetIntoMessagingService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            //Log.d(TAG, "title: ${remoteMessage.data["title"]}")
            val body = remoteMessage.data["body"]
            val title = remoteMessage.data["title"]
            val type = remoteMessage.data["type"]

            showNotification(title!!,body!!, type!!)

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob()
//            } else {
//                // Handle message within 10 seconds
//                handleNow()
//            }
        }

        // Check if message contains a notification payload.
//        remoteMessage.notification?.let {
//            Log.d(TAG, "Message Notification Body: ${it.body}")
//
//            val body = remoteMessage.notification!!.body
//            val title = remoteMessage.notification!!.title
//            val tag = remoteMessage.notification!!.tag
//
//            showNotification(title!!,body!!,tag!!)
//
//        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.


    }

    private fun showNotification(title: String, body: String,type : String) {
        val channelID = "PetIntoChannel"
        val notificationID = 111111

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                channelID,"PetInto",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "This is PetInto notification"
            }

            val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }



        val builder = NotificationCompat.Builder(this,channelID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(
                NotificationCompat.BigTextStyle()
                .bigText(body))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        if(type == "REPORT"){
//            val profileIntent = Intent(this,MainActivity::class.java)
//            profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            profileIntent.putExtra("fragment","report")
//            val pendingIntent = PendingIntent.getActivity(this,0,
//                profileIntent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

            val pendingIntent = NavDeepLinkBuilder(this)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.reportManagementFragment)
                .setComponentName(MainActivity::class.java)
                .createPendingIntent()


            builder.setContentIntent(pendingIntent)



        }


        with(NotificationManagerCompat.from(this)){
            if (ActivityCompat.checkSelfPermission(
                    this@PetIntoAdminMessagingService,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                Log.e(TAG,"Permission denied")
                return
            }
            notify(notificationID,builder.build())
        }
    }



    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }
}