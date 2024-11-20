package com.example.app_yournewsontime

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {
    @SuppressLint("InvalidAnalyticsName")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Lanzamiento de eventos personalizados en nuestra aplicacion
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integracion de Firebase completa")
        analytics.logEvent("Inicio de sesion",bundle)

        // Encuentra el botón de inicio y configura el listener
        val getStartedButton: Button = findViewById(R.id.get_started_button)
        getStartedButton.setOnClickListener {
            setContentView(R.layout.activity_register)
        }
    }
}