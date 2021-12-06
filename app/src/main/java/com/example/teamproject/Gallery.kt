package com.example.teamproject
import android.app.Activity
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

private val UP_GALLERY = 0
private val DOWN_GALLERY = 1
class Gallery : AppCompatActivity() {
    lateinit var up : ImageButton
    lateinit var down : ImageButton
    lateinit var apply : Button
    lateinit var retn : Button

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        setTitle("갤러리에서 배경화면 선택하기")
        up = findViewById<ImageButton>(R.id.gain)
        down = findViewById<ImageButton>(R.id.loss)
        apply = findViewById<Button>(R.id.decide)
        retn = findViewById<Button>(R.id.back)

        up.setOnClickListener { openGallery(UP_GALLERY) }
        down.setOnClickListener { openGallery(DOWN_GALLERY) }
    }

    private fun openGallery(tmp : Int) {
        val wallpaperManager = WallpaperManager.getInstance(baseContext)
        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, tmp)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            if(requestCode == UP_GALLERY){
                var currentImageUrl : Uri? = data?.data
                try{
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
                    up.setImageBitmap(bitmap)

                } catch(e:Exception){
                    e.printStackTrace()
                }
            }
            if(requestCode == DOWN_GALLERY){
                var currentImageUrl : Uri? = data?.data
                try{
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
                    down.setImageBitmap(bitmap)
                } catch(e:Exception){
                    e.printStackTrace()
                }
            }
        } else{
            Log.d("ActivityResult", "something wrong")
        }
    }
}