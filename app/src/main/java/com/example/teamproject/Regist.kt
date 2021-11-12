package com.example.teamproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Regist : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var isExistBlank = false
        var isPWSame = false

        var btn_reg = findViewById<Button>(R.id.btn_reg)
        var regName = findViewById<EditText>(R.id.regName)
        var regId = findViewById<EditText>(R.id.regId)
        var regPass = findViewById<EditText>(R.id.regPass)
        var regPassCheck = findViewById<EditText>(R.id.regPassCheck)


        btn_reg.setOnClickListener {
            val name = regName.text.toString()
            val id = regId.text.toString()
            val pw = regPass.text.toString()
            val pw_ck = regPassCheck.text.toString()


            if(id.isEmpty() || pw.isEmpty() || pw_ck.isEmpty() || name.isEmpty()){
                isExistBlank = true
            }
            else{
                isExistBlank = false
                if(pw == pw_ck){
                    isPWSame = true
                }
            }

            if(!isExistBlank && isPWSame){
                Toast.makeText(this, "회원가입 성공",Toast.LENGTH_SHORT).show()

                val sharedPreference = getSharedPreferences("file name",Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putString("id",id)
                editor.putString("pw",pw)
                editor.putString("name", name)
                editor.apply()

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
            }
        }
    }
}