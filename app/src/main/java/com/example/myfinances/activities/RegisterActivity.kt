package com.example.myfinances.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.myfinances.R
import com.example.myfinances.data.server.UserServer
import com.example.myfinances.databinding.ActivityRegisterBinding
import com.example.myfinances.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = registerBinding.root
        setContentView(view)

        registerBinding.radioButtonHombre.isChecked = true

        with(registerBinding) {

            textRepeatPassword.setOnClickListener {
                if (repeatPassword.error == getString(R.string.no_coincidencia)) {
                    repeatPassword.error = null
                    textRepeatPassword.setText(EMPTY)
                }
            }

            registrar.setOnClickListener {
                val name = textUsername.text.toString()
                val email = textEmail.text.toString()
                val password = textPassword.text.toString()
                var sexo = ""
                var defaultPicture = ""
                when {
                    radioButtonHombre.isChecked -> {
                        sexo = getString(R.string.hombre)
                        defaultPicture =
                            "https://firebasestorage.googleapis.com/v0/b/my-finances-6136c.appspot.com/o/hombre.jpg?alt=media&token=c7b7ea07-3372-4f6d-bce1-a92b6d904dec"
                    }
                    radioButtonMujer.isChecked -> {
                        sexo = getString(R.string.mujer)
                        defaultPicture =
                            "https://firebasestorage.googleapis.com/v0/b/my-finances-6136c.appspot.com/o/mujer.jpg?alt=media&token=77e1615f-da72-4c2a-a252-b7d6fd587dea"
                    }
                }
                if (name.isNotEmpty() &&
                    email.isNotEmpty() &&
                    password.isNotEmpty() &&
                    (name.length >= MIN_LENGHT_USER) &&
                    (radioButtonHombre.isChecked || radioButtonMujer.isChecked)
                ) {
                    if (password == textRepeatPassword.text.toString()) {
                        repeatPassword.error = null
                        crearUsuario(name, email, sexo, defaultPicture, password)
                    } else {
                        repeatPassword.error = getString(R.string.no_coincidencia)
                    }
                } else {
                    toastMessage(getString(R.string.campos_vacios))
                }
            }

            textUsername.doAfterTextChanged {
                if (!nameValidator(textUsername.text.toString())) {
                    username.error = getString(R.string.digits8)
                } else {
                    username.error = null
                }
            }

            textEmail.doAfterTextChanged {
                if (!emailValidator(textEmail.text.toString())) {
                    email.error = getString(R.string.email_invalido)
                } else {
                    email.error = null
                }
            }

            textPassword.doAfterTextChanged {
                if (!passValidator(textPassword.text.toString())) {
                    password.error = getString(R.string.digits6)
                } else {
                    password.error = null
                }
            }
        }
    }

    private fun crearUserServer(
        id: String,
        name: String,
        email: String,
        sexo: String,
        defaultPicture: String
    ) {
        val db = Firebase.firestore
        val userServer = UserServer(
            id = id,
            nickname = name,
            email = email,
            sexo = sexo,
            picture = defaultPicture
        )
        db.collection("users").document(id).set(userServer)
        gotoLoginActivity()
    }

    private fun crearUsuario(
        name: String,
        email: String,
        sexo: String,
        defaultPicture: String,
        password: String
    ) {
        auth = Firebase.auth
        var uid: String
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    uid = user?.uid.toString()
                    crearUserServer(uid, name, email, sexo, defaultPicture)
                    user?.sendEmailVerification()
                    toastMessage("Usuario creado - Se envió Correo de Verificación")
                } else {
                    if (task.exception?.localizedMessage == "The email address is badly formatted.") {
                        toastMessage(getString(R.string.email_incorrecto))
                    } else if (task.exception?.localizedMessage == "The given password is invalid. [ Password should be at least 6 characters ]") {
                        toastMessage(getString(R.string.pass_invalida))
                    }
                    clearViews()
                }
            }
    }

    private fun clearViews() {
        with(registerBinding) {
            textUsername.setText(EMPTY)
            textEmail.setText(EMPTY)
            textPassword.setText(EMPTY)
            textRepeatPassword.setText(EMPTY)
        }
    }

    private fun gotoLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun toastMessage(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}
