package com.example.myfinances.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myfinances.databinding.ActivityRecoveryBinding
import com.google.android.gms.tasks.OnCompleteListener
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
                    .addOnCompleteListener{ task ->
                        if(task.isSuccessful){
                            toastMessage("Correo de recuperación enviado con éxito")
                            gotoLoginActivity()
                        } else {
                            toastMessage("No fue posible enviar el correo de recuperación, verifique el correo.")
                        }
                    }
            } else {
                toastMessage("Ingrese un correo electrónico")
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
            Toast.LENGTH_SHORT
        ).show()
    }
}
