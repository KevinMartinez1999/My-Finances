package com.example.myfinances.activities

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.myfinances.R
import com.example.myfinances.databinding.ActivityLoginBinding
import com.example.myfinances.utils.EMPTY
import com.example.myfinances.utils.emailValidator
import com.example.myfinances.utils.passValidator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)

        with(loginBinding) {

            /*Cuando le damos click a la barra de texto de la contraseña
            * luego de que tenia el aviso de error, este se va a quitar para permitir
            * escribir de nuevo mas comodamente*/
            textPassword.setOnClickListener {
                if (password.error == getString(R.string.errorpass)) {
                    password.error = null
                    textPassword.setText(EMPTY)
                }
            }

            /*Muestra la contraseña y la oculta*/
            mostrar.setOnClickListener {
                if (!mostrar.isChecked) {
                    textPassword.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                } else {
                    textPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                }
            }

            /*Verifica cada momneto como cambia lo que hay en la barra de correo
            * para verificar si es un correo valido o no*/
            textEmail.doAfterTextChanged {
                if (!emailValidator(textEmail.text.toString())) {
                    email.error = getString(R.string.email_invalido)
                } else {
                    email.error = null
                }
            }

            /*Verifica cada momento como cambia lo que hay en la barra de contraseña
            * para verificar si es valida o no*/
            textPassword.doAfterTextChanged {
                if (!passValidator(textPassword.text.toString())) {
                    password.error = getString(R.string.digits6)
                } else {
                    password.error = null
                }
            }

            /*Funcion que se ejecuta cuando damos en el boton de inicio de sesion*/
            send.setOnClickListener {
                auth = Firebase.auth
                val email = textEmail.text.toString()
                val password = textPassword.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                if (user != null) {
                                    if (user.isEmailVerified) {
                                        goToMainActivity()
                                    } else {
                                        toastMessage(getString(R.string.correo_no_verificado))
                                    }
                                }
                            } else {
                                when (task.exception?.localizedMessage) {
                                    "The email address is badly formatted." -> {
                                        toastMessage(getString(R.string.email_incorrecto))
                                    }
                                    "There is no user record corresponding to this identifier. The user may have been deleted." -> {
                                        toastMessage(getString(R.string.usuario_no_existe))
                                    }
                                    "The password is invalid or the user does not have a password." -> {
                                        toastMessage(getString(R.string.pass_incorrecta))
                                    }
                                }
                                clearViews()
                            }
                        }
                } else {
                    toastMessage(getString(R.string.campos_vacios))
                }
            }

            register.setOnClickListener {
                goToRegisterActivity()
            }
        }

        forgot_password.setOnClickListener {
            goToRecoveryActivity()
        }
    }

    private fun clearViews() {
        with(loginBinding) {
            textEmail.setText(EMPTY)
            textPassword.setText(EMPTY)
        }
    }

    private fun goToRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToRecoveryActivity() {
        val intent = Intent(this, RecoveryActivity::class.java)
        startActivity(intent)
    }

    private fun toastMessage(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}
