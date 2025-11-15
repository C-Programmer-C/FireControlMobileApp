package com.example.hacaton2


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login_activity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_layout)
        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

         */
                var Fire_auth: FirebaseAuth = FirebaseAuth.getInstance()

                val button_login: Button = findViewById(R.id.button)
                val et_email: EditText = findViewById(R.id.login_etEmail)
                val et_pass: EditText = findViewById(R.id.login_etPass)
                val tv_regestration: TextView =  findViewById(R.id.login_tvRegestration)


                val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"


                button_login.setOnClickListener{
                        val email = et_email.text.toString()
                        val pass = et_pass.text.toString()
                        val a = runCatching {
                                require(email.isNotBlank()) {et_email.setError("Поле не должно быть пустым")}
                                require(email.matches(emailRegex.toRegex())) {et_email.setError("Введите корректный E-mail")}
                                require(pass.isNotBlank()) {et_pass.setError("Поле не должно быть пустым")}
                                require(pass.length >= 6) { et_pass.setError("Пароль должен быть более 6 символов") }
                        }
                        if(a.isSuccess){
                                Fire_auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
                                        if (it.isSuccessful) {
                                                val intent = Intent(this, user_activity::class.java)
                                                this.startActivity(intent)
                                        } else
                                                Toast.makeText(this, "Авторизация не удалась", Toast.LENGTH_SHORT).show()
                                }
                        }
                }
                tv_regestration.setOnClickListener{
                        val intent = Intent(this, Regestration_activity::class.java)
                        this.startActivity(intent)
                }

        }
    }