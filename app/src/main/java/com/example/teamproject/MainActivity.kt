package com.example.teamproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btnLogin = findViewById<ImageButton>(R.id.btnLogin)
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
                        val name = it.child("name").value.toString()
                        val stockChange = it.child("stockChange").value as ArrayList<Int>
                        val date = it.child("date").value as ArrayList<String>
                        val stockNumber = Integer.parseInt(it.child("stockNumber").value.toString())
                        val changeNum = Integer.parseInt(it.child("changeNum").value.toString())
                        val currentStock = ArrayList<Stock>(1)
                        val startStock = ArrayList<Stock>(1)
                        for(i in 0..stockNumber - 1){
                            val stockCode = it.child("currentStock").child(i.toString()).child("stockCode").value.toString()
                            val stockName = it.child("currentStock").child(i.toString()).child("stockName").value.toString()
                            val stockPrice = Integer.parseInt(it.child("currentStock").child(i.toString()).child("stockPrice").value.toString())
                            val stockNum = Integer.parseInt(it.child("currentStock").child(i.toString()).child("stockNum").value.toString())
                            currentStock.add(i,Stock(
                                stockCode, stockName, stockPrice, stockNum
                            ))
                        }
                        for(i in 0..stockNumber - 1){
                            val stockCode = it.child("startStock").child(i.toString()).child("stockCode").value.toString()
                            val stockName = it.child("startStock").child(i.toString()).child("stockName").value.toString()
                            val stockPrice = Integer.parseInt(it.child("startStock").child(i.toString()).child("stockPrice").value.toString())
                            val stockNum = Integer.parseInt(it.child("startStock").child(i.toString()).child("stockNum").value.toString())
                            startStock.add(i,Stock(
                                stockCode, stockName, stockPrice, stockNum
                            ))
                        }
                        var user = User(
                            name, tempId, value, startStock, currentStock, stockChange,date, stockNumber, changeNum
                        )
                        Toast.makeText(this, "${user.getname()}님 환영합니다.", Toast.LENGTH_SHORT).show()
                        idedt1.setText("")
                        idedt2.setText("")
                        var intentMenu = Intent(this, Menu::class.java)
                        intentMenu.putExtra("user", user)
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