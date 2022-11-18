package com.shino72.wallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import org.json.JSONArray
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    var db : AppDatabase? = null
    lateinit var paymentList : JSONObject
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val intent = intent
        val registerName = intent.getStringExtra("name")
        val platformName : TextView = findViewById(R.id.platformName)

        // json 파일 불러오기
        val jsonString = assets.open("AppList.json").reader().readText()
        val jsonArray = JSONArray(jsonString)
        var koreanName = ""

        // 정보 찾기
        for (i in 0..jsonArray.length() - 1)
        {
            val item = jsonArray.getJSONObject(i)
            if(item.getString("name") == registerName){
                koreanName = item.getString("korean")
                paymentList = item
                break
            }
        }
        platformName.text = koreanName
        val imgid = resources.getIdentifier("appicon_"+registerName, "drawable",packageName)
        val registerImage : AppCompatImageView = findViewById(R.id.paymentImage)
        registerImage.setImageResource(imgid)

        var priceText : TextView = findViewById(R.id.priceText)
        var planName : TextView = findViewById(R.id.planName)
        var registerDate : EditText = findViewById(R.id.paymentDate)
        var cancleButton : Button = findViewById(R.id.cancle)
        var enrollButton : Button = findViewById(R.id.enroll)
        var memoText : EditText = findViewById(R.id.memo)
        enrollButton.setOnClickListener {
            var price = priceText.text.toString()
            var plan = planName.text.toString()
            var date = registerDate.text.toString()
            if(price == "") {
                alertInformation(false)
                return@setOnClickListener
            }
            if(plan == ""){
                alertInformation(false)
                return@setOnClickListener
            }
            if(date == "" || !(date.toInt() in 0..31)){
                alertInformation(false)
                return@setOnClickListener
            }
            alertInformation(true)
            db = AppDatabase.getInstance(this)
            val Data = Plan(0,registerName.toString(), koreanName, plan, price.toInt(),date.toInt(),memoText.text.toString())
            db?.planDao()?.insertDB(Data)

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
        cancleButton.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun alertInformation(status : Boolean)
    {
        if(!status)Toast.makeText(this, "데이터를 정확히 입력해주세요.",Toast.LENGTH_SHORT).show()
        else Toast.makeText(this,"입력 완료",Toast.LENGTH_SHORT).show()
    }
}