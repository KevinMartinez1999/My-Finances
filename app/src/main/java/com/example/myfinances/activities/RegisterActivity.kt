package com.example.myfinances.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.myfinances.R
import com.example.myfinances.databinding.ActivityRegisterBinding
import com.example.myfinances.utils.emailValidator
import com.example.myfinances.utils.nameValidator
import com.example.myfinances.utils.passValidator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        registerBinding.textRepeatPassword.setOnClickListener {
            if (registerBinding.repeatPassword.error == getString(R.string.no_coincidencia)) {
                registerBinding.repeatPassword.error = null
                registerBinding.textRepeatPassword.setText("")
            }
        }

        registerBinding.registrar.setOnClickListener {
            val name = registerBinding.textUsername.text.toString()
            val email = registerBinding.textEmail.text.toString()
            val password = registerBinding.textPassword.text.toString()
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && (name.length >= 8)) {
                if (password == registerBinding.textRepeatPassword.text.toString()) {
                    registerBinding.repeatPassword.error = null
                    crearUsuario(email, password)
                } else {
                    registerBinding.repeatPassword.error = getString(R.string.no_coincidencia)
                }
            } else {
                Toast.makeText(this, getString(R.string.errorregister), Toast.LENGTH_LONG).show()
            }
        }

        registerBinding.textUsername.doAfterTextChanged {
            if (!nameValidator(registerBinding.textUsername.text.toString())) {
                registerBinding.username.error = getString(R.string.digits8)
            } else {
                registerBinding.username.error = null
            }
        }

        registerBinding.textEmail.doAfterTextChanged {
            if (!emailValidator(registerBinding.textEmail.text.toString())) {
                registerBinding.email.error = getString(R.string.email_invalido)
            } else {
                registerBinding.email.error = null
            }
        }

        registerBinding.textPassword.doAfterTextChanged {
            if (!passValidator(registerBinding.textPassword.text.toString())) {
                registerBinding.password.error = getString(R.string.digits6)
            } else {
                registerBinding.password.error = null
            }
        }
    }

    private fun crearUsuario(email: String, password: String) {
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("register", "createUserWithEmail:success")
                    //val user = auth.currentUser

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    if (task.exception?.localizedMessage == "The email address is badly formatted.") {
                        Toast.makeText(
                            baseContext, "El correo no esta escrito correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (task.exception?.localizedMessage == "The given password is invalid. [ Password should be at least 6 characters ]") {
                        Toast.makeText(
                            baseContext, "La contraseña debe contener 6 caracteres mínimo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    clearViews()
                }
            }
    }

    private fun clearViews() {
        with(registerBinding) {
            textUsername.setText("")
            textEmail.setText("")
            textPassword.setText("")
            textRepeatPassword.setText("")
        }
    }
}
