package com.example.hacaton2


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class log_in_Kotlin : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.log_in)
        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

         */
                var auth: FirebaseAuth = FirebaseAuth.getInstance()
                val button_login: Button = findViewById(R.id.button)
                val Email_et: EditText = findViewById(R.id.Reg_et_email)
                val Pass_et: EditText = findViewById(R.id.Reg_et_pass)
                button_login.setOnClickListener{
                        val email = Email_et.text.toString()
                        val pass = Pass_et.text.toString()
                        if (email.isBlank() || pass.isBlank()) {
                                Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()

                        }
                        else {
                                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
                                        if (it.isSuccessful) {
                                                val intent = Intent(this, tasks::class.java)
                                                this.startActivity(intent)
                                        } else
                                                Toast.makeText(this, "Авторизация не удалась", Toast.LENGTH_SHORT).show()
                                }
                        }

                }
        }
    }