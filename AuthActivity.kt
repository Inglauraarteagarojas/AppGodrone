package com.godrone.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Inicializar los elementos de la interfaz
        val username = findViewById<EditText>(R.id.inputUsername)
        val password = findViewById<EditText>(R.id.inputPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val registerButton = findViewById<Button>(R.id.btnRegister)

        // Inicializar el helper de la base de datos
        val dbHelper = DatabaseHelper(this)

        // Configuración del botón de inicio de sesión
        loginButton.setOnClickListener {
            val user = username.text.toString().trim()
            val pass = password.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                // Llamar a la función de autenticación
                val isAuthenticated = dbHelper.authenticateUser(user, pass)
                if (isAuthenticated) {
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    // Redirigir a MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Finalizar AuthActivity
                } else {
                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Configuración del botón de registro
        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}


