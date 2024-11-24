package com.example.app_yournewsontime

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri.Builder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    val GOOGLE_SIGN_IN = 100

    @SuppressLint("InvalidAnalyticsName")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Verificar si es la primera ejecución
        if (isFirstRun()) {
            // Mostrar pantalla inicial (carga)
            setContentView(R.layout.activity_main)

            val goToRegisterButton: Button = findViewById(R.id.get_started_button)
            goToRegisterButton.setOnClickListener {
                // Redirigir al registro o inicio de sesión
                RegisterMethod()
            }
        } else {
            // Si no es la primera ejecución, verificar la sesión
            if (session()) {
                // Si hay sesión activa, redirigir al inicio
                return
            } else {
                // Si no hay sesión activa, mostrar inicio de sesión
                RegisterMethod()
            }
        }
    }
    private fun isFirstRun(): Boolean {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val isFirstRun = prefs.getBoolean("is_first_run", true)

        if (isFirstRun) {
            // Si es la primera vez, actualizar el valor para futuras ejecuciones
            prefs.edit().putBoolean("is_first_run", false).apply()
        }

        return isFirstRun
    }



    private fun RegisterMethod() {
        // Cambia a la vista de registro
        setContentView(R.layout.activity_register)
        // Configura los elementos y funcionalidades para el registro
        setup()
        session()
    }

    @SuppressLint("ResourceType")
    private fun session(): Boolean {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        if (email != null) {
            news(email)
            return true
        }
        return false
    }

    private fun setup(){
        title="Autenticacion"
        //val signUpButton: Button = findViewById(R.id.get_started_button)
        val signUpButton: Button = findViewById(R.id.SingUpbutton)
        val EmailEditText= findViewById<EditText>(R.id.EmailEditText)
        val PasswordEditText= findViewById<EditText>(R.id.PasswordEditText)
        val googleButton: Button = findViewById(R.id.googleButton)


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

        googleButton.setOnClickListener {
           val confGoogle = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               .requestIdToken(getString(R.string.default_web_client_id))
               .requestEmail()
               .build()

            val googleClient = GoogleSignIn.getClient(this,confGoogle)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)


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

    private fun showAlert2(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error a la hora de autenticar usuario con Google")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int , data: Intent?){
        super.onActivityResult(requestCode,resultCode,data)
        if (requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
                        if (it.isSuccessful){
                            news(account.email ?: "")
                        }else{
                            showAlert2()
                        }
                    }

            }
            }catch (e:ApiException){
                showAlert2()
            }
        }

    }





}