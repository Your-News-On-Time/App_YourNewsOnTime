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
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //setup
        setup()

    }

    private fun setup(){
        title="Autenticacion"
        val signUpButton = findViewById<Button>(R.id.SingUpbutton)
        val EmailEditText= findViewById<EditText>(R.id.EmailEditText)
        val PasswordEditText= findViewById<EditText>(R.id.PasswordEditText)
        signUpButton.setOnClickListener{
            Log.d("RegisterActivity", "Botón Sign Up presionado")
            if(EmailEditText.text.isNotEmpty() && PasswordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(EmailEditText.text.toString(),
                    PasswordEditText.text.toString()).addOnCompleteListener{
                        if (it.isSuccessful){
                            news(it.result?.user?.email ?: "")
                        }else{
                            showAlert()
                        }
                }
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