package com.example.teamproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btnLogin = findViewById<Button>(R.id.btnLogin)
        var btnSign = findViewById<Button>(R.id.btnSign)
        var idedt1 = findViewById<EditText>(R.id.idedt1)
        var idedt2 = findViewById<EditText>(R.id.idedt2)

        btnLogin.setOnClickListener {
            var id = idedt1.text.toString()
            var pw = idedt2.text.toString()
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference()
            var value = ""
            myRef.child("user").child(id).get().addOnSuccessListener {
                var tempId = it.child("id").value.toString()
                value = it.child("password").value.toString()
                if(tempId != id){
                    Toast.makeText(this, "아이디가 존재하지 않습니다.",Toast.LENGTH_SHORT).show()
                }
                else {
                    if (pw == value) {
                        Toast.makeText(this, "${tempId}로그인 성공", Toast.LENGTH_SHORT).show()

                        val intentMenu = Intent(this, Menu::class.java)
                        startActivity(intentMenu)

                    } else {
                        Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener{
                Toast.makeText(this, "아이디가 존재하지 않습니다.",Toast.LENGTH_SHORT).show()

            }
        }

        btnSign.setOnClickListener {
            val intent = Intent(this, Regist::class.java)
            startActivity(intent)
        }



    }
}