package com.example.teamproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Regist : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var isExistBlank = false
        var isPWSame = false
        var isExistId = false

        var btn_reg = findViewById<Button>(R.id.btn_reg)
        var regName = findViewById<EditText>(R.id.regName)
        var regId = findViewById<EditText>(R.id.regId)
        var regPass = findViewById<EditText>(R.id.regPass)
        var regPassCheck = findViewById<EditText>(R.id.regPassCheck)
        var CheckOverlap = findViewById<Button>(R.id.checkOverlap)

        CheckOverlap.setOnClickListener {
            var confirmname = regId.text.toString()
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference()
            myRef.child("user").child(confirmname).get().addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
                var name = it.child("id").value.toString()
//                Toast.makeText(this, "${name}",Toast.LENGTH_SHORT).show()
                if(name == confirmname){
                    Toast.makeText(this, "이미 존재하는 아이디입니다.",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "사용 가능한 아이디입니다.",Toast.LENGTH_SHORT).show()
                    isExistId = true
                }
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }

        }



        btn_reg.setOnClickListener {
            val name = regName.text.toString()
            val id = regId.text.toString()
            val pw = regPass.text.toString()
            val pw_ck = regPassCheck.text.toString()
            var startStock = ArrayList<Stock>(20)
            var currentStock = ArrayList<Stock>(20)
            var stockChange = ArrayList<Int>(20)
            var date = ArrayList<String>(20)
            var changeNum :Int = 0
            var stockNumber : Int = 0
            startStock.add(Stock())
            currentStock.add(Stock())
            stockChange.add(0)
            date.add("")

            if(id.isEmpty() || pw.isEmpty() || pw_ck.isEmpty() || name.isEmpty()){
                isExistBlank = true
            }
            else{
                isExistBlank = false
                if(pw == pw_ck){
                    isPWSame = true
                }
            }

            if(!isExistBlank && isPWSame && isExistId){
                Toast.makeText(this, "회원가입 성공",Toast.LENGTH_SHORT).show()
                var user : User  = User(
                    name, id, pw, startStock, currentStock, stockChange, date ,stockNumber, changeNum
                )
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("user")
                myRef.child(id).setValue(user)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            else{
                if(isExistBlank){
                    Toast.makeText(this, "빈칸을 채워주세요",Toast.LENGTH_SHORT).show()
                }
                else if(!isPWSame){
                    Toast.makeText(this, "비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show()
                }
                else if(!isExistId){
                    Toast.makeText(this, "아이디를 확인해주세요",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}