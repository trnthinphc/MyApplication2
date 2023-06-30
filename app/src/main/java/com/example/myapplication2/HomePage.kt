package com.example.myapplication2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication2.adapter.HorizontalRecycleView
import com.example.myapplication2.databinding.ActivityHomePageBinding
import com.example.myapplication2.databinding.ActivitySignUpBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomePage : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var bottomNavigationView: BottomNavigationView // declare the variable

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HorizontalRecycleView

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = findViewById(R.id.bottomNavigationView) // initialize the variable
        bottomNavigationView.menu.getItem(1).isEnabled = false
        bottomNavigationView.background = null

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            userId = currentUser.uid
            readUserDataFromFirestore(userId)
        } else {
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show()
        }


        recyclerView = findViewById(R.id.recyclerView)
        val dataList: List<RecycleViewSlide> =
            listOf(
                RecycleViewSlide(R.drawable.pt1, "Tree"),
                RecycleViewSlide(R.drawable.pt2, "Climbers"),
                RecycleViewSlide(R.drawable.pt3, "Shrubs"),
                RecycleViewSlide(R.drawable.pt4, "Creepers"),
                RecycleViewSlide(R.drawable.pt5, "Herbs")
            )
        adapter = HorizontalRecycleView(dataList)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.miHome -> {
                    // Handle home button click
                    val intent = Intent(this, HomePage::class.java)
                    startActivity(intent)
                    true
                }
                R.id.placeholder -> {
                    // Handle add button click
                    true
                }
                R.id.miProfile -> {
                    // Handle profile button click
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        binding.button1.setOnClickListener {
            val intent = Intent(this, Camera::class.java)
            startActivity(intent)
        }
        binding.button2.setOnClickListener {
            val intent = Intent(this, Species::class.java)
            startActivity(intent)
        }
        binding.button3.setOnClickListener {
            val intent = Intent(this, Articles::class.java)
            startActivity(intent)
        }
        binding.fab.setOnClickListener {
            val intent = Intent(this, Camera::class.java)
            startActivity(intent)
        }
    }
    private fun readUserDataFromFirestore(userId: String) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(userId)

        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val email = documentSnapshot.getString("email")
                    val name = documentSnapshot.getString("name")
                    // Hiển thị name trong giao diện người dùng
                    binding.nameEditText.setText("Hello "+name)
                } else {
                    Toast.makeText(this, "User document does not exist", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to read user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}



