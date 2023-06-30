package com.example.myapplication2

import com.example.myapplication2.SignInActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.myapplication2.databinding.ActivityForgotPassBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPassBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textviewSI.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        // Hàm kiểm tra định dạng email
        fun isEmailValid(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
        // Gửi yêu cầu đặt lại mật khẩu
        fun resetPassword(email: String) {
            val firebaseAuth = FirebaseAuth.getInstance()

            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Yêu cầu đặt lại mật khẩu thành công
                        Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                    } else {
                        // Yêu cầu đặt lại mật khẩu thất bại
                        Toast.makeText(this, "Failed to send password reset email", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        // Sử dụng phương thức resetPassword
        binding.resetButton.setOnClickListener {
            val email = binding.emailEt.text.toString()

            if (email.isNotEmpty()) {
                if (isEmailValid(email)) {
                    resetPassword(email)
                } else {
                    Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }
    }
}