package com.example.myapplication2

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication2.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        setContentView(binding.root)


        binding.tvSignIn.setOnClickListener{
            val intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)
        }

        // Hàm kiểm tra mật khẩu
        fun isPasswordValid(password: String): Boolean {
            val passwordPattern = Pattern.compile(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{11,}$"
            )
            return passwordPattern.matcher(password).matches()
        }
        // Hàm kiểm tra định dạng email
        fun isEmailValid(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
        binding.button.setOnClickListener{
            val email = binding.emailEt.text.toString()
            val pass = binding.passEt.text.toString()
            val confirmPass = binding.confirmPass.text.toString()
            val name = binding.nameEt.text.toString()



            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (isEmailValid(email)) {
                    if (pass == confirmPass) {
                        if (isPasswordValid(pass)) {
                            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        val user = firebaseAuth.currentUser
                                        val userId = user?.uid

                                        if (userId != null) {
                                            saveUserDataToFirestore(email, name, userId)
                                        }

                                        val intent = Intent(this, SignInActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Failed to sign up: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            Toast.makeText(
                                this,
                                "Password must be at least 11 characters long and contain at least one lowercase letter, one uppercase letter, one digit, and one special character.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserDataToFirestore(email: String, name: String, userId: String) {
        val db = FirebaseFirestore.getInstance()
        val userMap = hashMapOf(
            "email" to email,
            "name" to name
        )
        db.collection("users")
            .document(userId)
            .set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "User created and data saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

