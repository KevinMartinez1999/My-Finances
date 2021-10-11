package com.example.myfinances.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfinances.R
import com.example.myfinances.databinding.ActivityRecoveryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecoveryActivity : AppCompatActivity() {

    private lateinit var recoveryBinding: ActivityRecoveryBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recoveryBinding = ActivityRecoveryBinding.inflate(layoutInflater)
        setContentView(recoveryBinding.root)

        recoveryBinding.send.setOnClickListener {
            val email = recoveryBinding.textEmail.text.toString()
            auth = Firebase.auth
            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            toastMessage(getString(R.string.correo_recuperacion))
                            gotoLoginActivity()
                        } else {
                            when (task.exception?.localizedMessage) {
                                "The email address is badly formatted." -> {
                                    toastMessage(getString(R.string.email_invalido))
                                }
                                "There is no user record corresponding to this identifier. The user may have been deleted." -> {
                                    toastMessage(getString(R.string.verifique_correo_de_recuperacion))
                                }
                            }
                        }
                    }
            } else {
                toastMessage(getString(R.string.ingrese_correo))
            }
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
            Toast.LENGTH_LONG
        ).show()
    }
}
