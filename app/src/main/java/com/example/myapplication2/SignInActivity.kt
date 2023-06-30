package com.example.myapplication2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import com.example.myapplication2.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var rememberMeCheckbox: CheckBox
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("RememberMePrefs", Context.MODE_PRIVATE)
        rememberMeCheckbox = binding.rememberMeCheckbox

        checkRememberMe()

        binding.tvSignIn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.tvforgot.setOnClickListener {
            val intent = Intent(this, ForgotPassActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passEt.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signIn(email, password)
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkRememberMe() {
        val isRemembered = sharedPreferences.getBoolean("rememberMe", false)
        if (isRemembered) {
            val email = sharedPreferences.getString("email", "")
            val password = sharedPreferences.getString("password", "")
            if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                binding.emailEt.setText(email)
                binding.passEt.setText(password)
                signIn(email, password)
            }
        }
    }

    private fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, HomePage::class.java)
                    startActivity(intent)
                    finish()

                    val editor = sharedPreferences.edit()
                    editor.putBoolean("rememberMe", rememberMeCheckbox.isChecked)
                    if (rememberMeCheckbox.isChecked) {
                        editor.putString("email", email)
                        editor.putString("password", password)
                    } else {
                        editor.remove("email")
                        editor.remove("password")
                    }
                    editor.apply()
                } else {
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
