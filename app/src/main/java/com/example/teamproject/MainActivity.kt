package com.example.teamproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

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

            val sharedPreference = getSharedPreferences("file name", Context.MODE_PRIVATE)
            val savedId = sharedPreference.getString("id","")
            val savedPw = sharedPreference.getString("pw","")

            if(id == savedId && pw == savedPw){
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()

                val intentMenu = Intent(this, Menu::class.java)
                startActivity(intentMenu)

            }
            else{
                Toast.makeText(this, "로그인 실패",Toast.LENGTH_SHORT).show()
            }


        }

        btnSign.setOnClickListener {
            val intent = Intent(this, Regist::class.java)
            startActivity(intent)
        }



    }
}