package com.example.myfinances.activities

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
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

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        /*Cuando le damos click a la barra de texto de la contraseña
        * luego de que tenia el aviso de error, este se va a quitar para permitir
        * escribir de nuevo mas comodamente*/
        loginBinding.textPassword.setOnClickListener {
            if (loginBinding.password.error == getString(R.string.errorpass)) {
                loginBinding.password.error = null
                loginBinding.textPassword.setText(EMPTY)
            }
        }

        loginBinding.mostrar.setOnClickListener {
            if (!loginBinding.mostrar.isChecked) {
                loginBinding.textPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            } else {
                loginBinding.textPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            }
        }

        /*Verifica cada momneto como cambia lo que hay en la barra de correo
        * para verificar si es un correo valido o no*/
        loginBinding.textEmail.doAfterTextChanged {
            if (!emailValidator(loginBinding.textEmail.text.toString())) {
                loginBinding.email.error = getString(R.string.email_invalido)
            } else {
                loginBinding.email.error = null
            }
        }

        /*Verifica cada momento como cambia lo que hay en la barra de contraseña
        * para verificar si es valida o no*/
        loginBinding.textPassword.doAfterTextChanged {
            if (!passValidator(loginBinding.textPassword.text.toString())) {
                loginBinding.password.error = getString(R.string.digits6)
            } else {
                loginBinding.password.error = null
            }
        }

        /*Funcion que se ejecuta cuando damos en el boton de inicio de sesion*/
        with(loginBinding) {
            send.setOnClickListener {
                auth = Firebase.auth
                val email = textEmail.text.toString()
                val password = textPassword.text.toString()

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login", "signInWithEmail:success")
                            //val user = auth.currentUser

                            val intent = Intent(baseContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            when (task.exception?.localizedMessage) {
                                "The email address is badly formatted." -> {
                                    Toast.makeText(
                                        baseContext, "El correo no esta escrito correctamente",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                "There is no user record corresponding to this identifier. The user may have been deleted." -> {
                                    Toast.makeText(
                                        baseContext, "El usuario no existe. Por favor regístrate",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                "The password is invalid or the user does not have a password." -> {
                                    Toast.makeText(
                                        baseContext, "Contraseña incorrecta. Intente de nuevo",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
            }
        }

        loginBinding.register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            //getData.launch(intent)
        }

        /*
        val getData =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val name: String = data?.getStringExtra("name") as String
                    val email: String = data.getStringExtra("email") as String
                    val pass: String = data.getStringExtra("password") as String
                    //crearusuario(email, pass) //para recibir los datos y almacenar localmente
                    loginBinding.textEmail.setText(EMPTY)
                    loginBinding.textPassword.setText(EMPTY)
                }
            }*/
    }
}
