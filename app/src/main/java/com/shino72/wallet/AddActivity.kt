package com.shino72.wallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shino72.wallet.Adapter.AddAdapter
import org.json.JSONArray

class AddActivity : AppCompatActivity() {
    private lateinit var paymentAdapter: AddAdapter
    private var paymentList = mutableListOf<Ott>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        // json 파일에서 데이터 가져오기
        val jsonString = assets.open("AppList.json").reader().readText()
        val jsonArray = JSONArray(jsonString)

        // 리사이클러뷰 설정
        val recyclerView : RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 3)
        paymentAdapter = AddAdapter(applicationContext)
        recyclerView.adapter = paymentAdapter
        paymentAdapter.setItemClickListener(object :AddAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val intent = Intent(applicationContext, RegisterActivity::class.java)
                intent.putExtra("name",paymentList[position].name)
                startActivity(intent)
            }
        })

        // 리사이클러뷰 이미지 넣기
        for(i in 0..jsonArray.length() - 1)
        {
            val item = jsonArray.getJSONObject(i)
            val imgName = item.getString("name")
            val imgid = resources.getIdentifier("appicon_"+imgName, "drawable",packageName)
            paymentList.add(Ott(imgName, imgid))
        }
        paymentAdapter.setPaymentList(paymentList)
    }
}