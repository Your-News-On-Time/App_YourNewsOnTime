package com.example.app_yournewsontime

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    @SuppressLint("InvalidAnalyticsName")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d("MAIN", "Esto es el main")
        setContentView(R.layout.activity_main)

        // Lanzamiento de eventos personalizados en nuestra aplicacion
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integracion de Firebase completa")
        analytics.logEvent("Inicio de sesion",bundle)

        // Encuentra el botón de inicio y configura el listener
       /// val getStartedButton: Button = findViewById(R.id.get_started_button)
        //getStartedButton.setOnClickListener {
          //  setContentView(R.layout.activity_register)
        //}
        val goToRegisterButton: Button = findViewById(R.id.get_started_button)
        goToRegisterButton.setOnClickListener {
            RegisterMethod()
        }


    }

    private fun RegisterMethod() {
        // Cambia a la vista de registro
        setContentView(R.layout.activity_register)
        // Configura los elementos y funcionalidades para el registro
        setup()
    }

    private fun setup(){
        title="Autenticacion"
        //val signUpButton: Button = findViewById(R.id.get_started_button)
        val signUpButton: Button = findViewById(R.id.SingUpbutton)
        val EmailEditText= findViewById<EditText>(R.id.EmailEditText)
        val PasswordEditText= findViewById<EditText>(R.id.PasswordEditText)


        signUpButton.setOnClickListener{
            Log.d("RegisterActivity", "Botón Sign Up presionado")
            if(EmailEditText.text.isNotEmpty() && PasswordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(EmailEditText.text.toString(),
                    PasswordEditText.text.toString()).addOnCompleteListener{
                    println("${it.result?.user?.email}")
                    if (it.isSuccessful){

                        news(it.result?.user?.email ?: "")
                    }else{
                        showAlert()
                    }
                }
            }else{
                showAlert()
            }

        }


        val loginButton = findViewById<Button>(R.id.LogInpButton)
        loginButton.setOnClickListener{
            Log.d("RegisterActivity", "Botón Sign In presionado")
            if(EmailEditText.text.isNotEmpty() && PasswordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(EmailEditText.text.toString(),
                    PasswordEditText.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        news(it.result?.user?.email ?: "")
                    }else{
                        showAlert()
                    }
                }
            }else{
                showAlert()
            }

        }



    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error a la hora de autenticar usuario")
        builder.setPositiveButton("aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun news(email:String){
        val newsIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email",email)
        }
        startActivity(newsIntent)

    }

}