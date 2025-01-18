package com.godrone.app

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializar la base de datos
        dbHelper = DatabaseHelper(this)

        // Encontrar los elementos de la interfaz por su ID
        val firstName = findViewById<EditText>(R.id.inputFirstName)
        val secondName = findViewById<EditText>(R.id.inputSecondName)
        val firstLastName = findViewById<EditText>(R.id.inputFirstLastName)
        val secondLastName = findViewById<EditText>(R.id.inputSecondLastName)
        val username = findViewById<EditText>(R.id.inputUsername)
        val password = findViewById<EditText>(R.id.inputPassword)
        val userTypeGroup = findViewById<RadioGroup>(R.id.userTypeGroup)
        val pilotCode = findViewById<EditText>(R.id.inputPilotCode)
        val registerButton = findViewById<Button>(R.id.btnSubmitRegister)

        // Mostrar u ocultar el campo del código de piloto según el tipo de usuario seleccionado
        userTypeGroup.setOnCheckedChangeListener { _, checkedId ->
            pilotCode.visibility = if (checkedId == R.id.radioPilot) EditText.VISIBLE else EditText.GONE
        }

        // Acción para el botón de registro
        registerButton.setOnClickListener {
            val user = username.text.toString().trim()
            val pass = password.text.toString().trim()

            // Validar los campos obligatorios
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Usuario y contraseña son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear los valores para insertar en la base de datos
            val contentValues = ContentValues().apply {
                put(DatabaseHelper.COLUMN_FIRST_NAME, firstName.text.toString())
                put(DatabaseHelper.COLUMN_SECOND_NAME, secondName.text.toString())
                put(DatabaseHelper.COLUMN_FIRST_LAST_NAME, firstLastName.text.toString())
                put(DatabaseHelper.COLUMN_SECOND_LAST_NAME, secondLastName.text.toString())
                put(DatabaseHelper.COLUMN_USERNAME, user)
                put(DatabaseHelper.COLUMN_PASSWORD, pass)
                put(DatabaseHelper.COLUMN_USER_TYPE, findViewById<RadioButton>(userTypeGroup.checkedRadioButtonId)?.text.toString())
                put(DatabaseHelper.COLUMN_PILOT_CODE, if (pilotCode.visibility == EditText.VISIBLE) pilotCode.text.toString() else null)
            }

            // Insertar el usuario en la base de datos
            val result = dbHelper.insertUser(contentValues)
            if (result != -1L) {
                Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
                // Redirigir al AuthActivity
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Error: El usuario ya existe o no se pudo registrar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}






