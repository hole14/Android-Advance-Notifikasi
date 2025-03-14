
package com.example.simplenotif

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.simplenotif.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification permission rejected", Toast.LENGTH_SHORT).show()
            }
        }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        val title = getString(R.string.notification_title)
        val message = getString(R.string.notification_message)

        binding.btnSend.setOnClickListener {
            sendNotification(title, message)
        }
    }

    private fun sendNotification(title: String, message: String) {
        val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://pijar.petik.or.id"))
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0 )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSubText(getString(R.string.notification_seubtext))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "petik channel"
    }
}