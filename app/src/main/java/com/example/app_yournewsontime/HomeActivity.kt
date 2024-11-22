package com.example.app_yournewsontime

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val bundle=intent.extras
        val email=bundle?.getString("email")
        if (email != null) {
            setup(email)

        }else{
            setup("")
        }
        //guardado de datos
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.apply()
    }






    private fun setup(email:String){
        title="Inicio"
        val EmailTextView= this.findViewById<TextView>(R.id.EmailTextView)
        EmailTextView.text=email

        val LoginOutButton=findViewById<Button>(R.id.LoginOutButton)
        LoginOutButton.setOnClickListener{
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()


            FirebaseAuth.getInstance().signOut()
            onBackPressed()

        }
    }
}