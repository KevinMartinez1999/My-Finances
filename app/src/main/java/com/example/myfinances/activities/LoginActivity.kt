package com.example.myfinances.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.myfinances.MyFinancesApp
import com.example.myfinances.R
import com.example.myfinances.data.dao.UserDAO
import com.example.myfinances.data.entities.User
import com.example.myfinances.databinding.ActivityLoginBinding
import com.example.myfinances.utils.EMPTY
import com.example.myfinances.utils.emailValidator
import com.example.myfinances.utils.passValidator
import java.sql.Types

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding

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
        loginBinding.send.setOnClickListener {
            val email = loginBinding.textEmail.text.toString()
            val flag = buscarusuario(email)
            if (flag) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, getString(R.string.errorlogin), Toast.LENGTH_LONG).show()
            }
        }

        val getData =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val name: String = data!!.getStringExtra("name") as String
                    val email: String = data.getStringExtra("email") as String
                    val pass: String = data.getStringExtra("password") as String
                    loginBinding.textEmail.setText(EMPTY)
                    loginBinding.textPassword.setText(EMPTY)
                    crearusuario(name, email, pass)
                }
            }

        loginBinding.register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            getData.launch(intent)
        }
    }

    private fun buscarusuario(email: String): Boolean {
        val userDao: UserDAO = MyFinancesApp.database.userDao()
        val user = userDao.searchUser(email)
        if (user != null) {
            with(loginBinding) {
                if (user.email == textEmail.text.toString() && user.password == textPassword.text.toString()) {
                    return true
                }
            }
        }
        return false
    }

    private fun crearusuario(name: String?, email: String?, pass: String?) {
        val user = User(id = Types.NULL, nombre = name, email = email, password = pass)
        val userDao: UserDAO = MyFinancesApp.database.userDao()
        userDao.insertUser(user)
    }
}
