package com.example.hacaton2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class Regestration_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.regestration_layout)
        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
         */

        class Person(var surname: String?, var name: String?, var phone: String?)

        var person = Person(null, null, null)

        val auth: FirebaseAuth = Firebase.auth
        val Fire_auth: FirebaseAuth = FirebaseAuth.getInstance()

        val flipper: ViewFlipper = findViewById(R.id.flipper)

        val reg_etSurname: EditText = findViewById(R.id.registration_etSurname)
        val reg_etName: EditText = findViewById(R.id.registration_etName)
        val reg_etPhone: EditText = findViewById(R.id.registration_etPhone)
        val reg_etEmail: EditText = findViewById(R.id.registration_etEmail)
        val reg_etPass: EditText = findViewById(R.id.registration_etPass)
        val reg_etRepeatPass: EditText = findViewById(R.id.registration_etRepeatPass)

        val reg_bNext: Button = findViewById(R.id.registration_bNext)
        val reg_bFinish: Button = findViewById(R.id.registration_bFinish)

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        fun String.isLongEnough() = length >= 8
        fun String.hasEnoughDigits() = count(Char::isDigit) > 0
        fun String.isMixedCase() = any(Char::isLowerCase) && any(Char::isUpperCase)
        fun String.hasSpecialChar() = any { it in "!,+^" }
        val requirements = listOf(
            String::isLongEnough,
            String::hasEnoughDigits,
            String::isMixedCase,
            String::hasSpecialChar
        )

        fun String.meetsRequirements() = requirements.all { check -> check(this) }


        reg_bNext.setOnClickListener {
            val Surname: String = reg_etSurname.text.toString()
            val Name: String = reg_etName.text.toString()
            val Phone: String = reg_etPhone.text.toString()
            val a = runCatching {
                require(Surname.isNotBlank()) { reg_etSurname.setError("Поле не должно быть пустым") }
                require(Name.isNotBlank()) { reg_etName.setError("Поле не должно быть пустым") }
                require(Phone.isNotBlank()) { reg_etPhone.setError("Поле не должно быть пустым") }
            }
            if (a.isSuccess) {
                flipper.showNext()
                person = Person(Surname, Name, Phone)
            }
        }
        reg_bFinish.setOnClickListener {
            val Email: String = reg_etEmail.text.toString()
            val Pass: String = reg_etPass.text.toString()
            val Repeat_Pass: String = reg_etRepeatPass.text.toString()
            val a = runCatching {
                require(Email.isNotBlank()) { reg_etEmail.setError("Поле не должно быть пустым") }
                require(Email.matches(emailRegex.toRegex())) {reg_etEmail.setError("Введите корректный E-mail")}
                require(Pass.isNotBlank()) { reg_etPass.setError("Поле не должно быть пустым") }
                require(Pass.meetsRequirements()) { reg_etPass.setError("Пароль неккоректный") }
                require(Repeat_Pass.isNotBlank()) { reg_etRepeatPass.setError("Поле не должно быть пустым") }
                require(Repeat_Pass == Pass) { reg_etRepeatPass.setError("Пароли не совпадают") }
            }
            if (a.isSuccess) {
                auth.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Успешная регистрация!", Toast.LENGTH_SHORT).show()
                        Fire_auth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(this) {

                            if (it.isSuccessful) {
                                val user = FirebaseAuth.getInstance().currentUser
                                val firebaseDatabase = FirebaseDatabase.getInstance()
                                val database = Firebase.database
                                if (user!= null){
                                    val myRef = Firebase.database.reference
                                    myRef.child("User").child(user.uid).setValue(person)
                                        .addOnSuccessListener {
                                            Toast.makeText(this, "Data added successfully!", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener { error ->
                                            Toast.makeText(this, "Failed to add data: ${error.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                                val intent = Intent(this, user_activity::class.java)
                                this.startActivity(intent)
                            }
                        }
                        } else {
                        Toast.makeText(this, "Регистрация не удалась...", Toast.LENGTH_SHORT).show()
                    }
                }
            }


        }
    }
}

