package com.example.myfinances.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.myfinances.R
import com.example.myfinances.Users
import com.example.myfinances.databinding.ActivityLoginBinding
import com.example.myfinances.utils.EMPTY
import com.example.myfinances.utils.emailValidator
import com.example.myfinances.utils.passValidator

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private var banEmail = false
    private var banPass = false
    private var usuarios: MutableList<Users> = mutableListOf()
    private lateinit var nick: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        // Modo test: Usar para evitar registro ya que no se cuenta con base de datos aun
        usuarios.add(Users("kdmz1999", "kevinmartinez9907@gmail.com", "123456"))

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
        loginBinding.send.setOnClickListener {
            for (u in usuarios) {
                if (loginBinding.textEmail.text.toString() == u.email) {
                    banEmail = true
                    if (loginBinding.textPassword.text.toString() == u.password) {
                        banPass = true
                        nick = u.nickname.toString()
                    }
                }
            }

            if (banEmail) {
                if (banPass) {
                    banEmail = false
                    banPass = false
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("email", loginBinding.textEmail.text.toString())
                    intent.putExtra("pass", loginBinding.textPassword.text.toString())
                    intent.putExtra("nick", nick)
                    startActivity(intent)
                    finish()
                } else {
                    loginBinding.password.error = getString(R.string.errorpass)
                }
            } else {
                Toast.makeText(this, getString(R.string.errorlogin), Toast.LENGTH_LONG).show()
            }
        }

        loginBinding.register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val user: Users = data!!.getSerializableExtra("user") as Users
            loginBinding.textEmail.setText(EMPTY)
            loginBinding.textPassword.setText(EMPTY)
            usuarios.add(user)
            //guardarusuario(user.nickname.toString(), user.email.toString(), user.password.toString())
        }
    }

    /*
    private fun guardarusuario(name: String?, email: String?, password: String?) {
        val usuario = User(id = Types.NULL, nombre = name, email = email, password = password)
        val userdao: UserDAO = MyFinancesApp.database.userDao()
        userdao.insertUser(usuario)
    }*/
}
