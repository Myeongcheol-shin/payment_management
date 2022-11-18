package com.shino72.wallet

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shino72.wallet.Adapter.ManageAdapter

class ManageActivity : AppCompatActivity() {
    var db : AppDatabase? = null
    var planList = mutableListOf<Plan>()

    private lateinit var manageAdapter: ManageAdapter
    private var manageList = mutableListOf<PaymentPlan>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)

        val addButton : AppCompatButton = findViewById(R.id.addButton)
        addButton.setOnClickListener{
            clickedAdd()
        }
        var dialog = AlertDialog.Builder(this)
        // db 초기화
        db = AppDatabase.getInstance(this)
        // 이전 데이터 가져오기
        val beforeData = db!!.planDao().getAll()
        if(beforeData.isNotEmpty()){
            planList.addAll(beforeData)
        }

        // 리사이클러뷰 만들기 (정기결제)
        val manageView : RecyclerView = findViewById(R.id.recyclerView)
        manageView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        manageAdapter = ManageAdapter(applicationContext)
        manageView.adapter = manageAdapter
        for(i in planList){
            var imgid = resources.getIdentifier("appicon_"+i.platform,"drawable",packageName)
            manageList.add(PaymentPlan(i.korean, imgid, i.price, i.duedate.toString(), i.name, i.uid, i.memo!!))
        }

        manageAdapter.setItemClickListener(object :ManageAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                // 클릭 시 이벤트 아직 없음
            }
            // 삭제 버튼
            override fun onDelete(v: View, position: Int, id : Int) {
                db!!.planDao().deleteByUserId(id)
                manageList.removeAt(position)
                manageAdapter.notifyDataSetChanged()

                val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                var pendingIntent = PendingIntent.getBroadcast(
                    applicationContext, position, intent, PendingIntent.FLAG_IMMUTABLE
                )
                Toast.makeText(applicationContext, "%d 삭제 완료".format(position), Toast.LENGTH_SHORT).show()
                alarmManager.cancel(pendingIntent)

            }
        })
        manageAdapter.setPaymentList(manageList)

    }
    override fun onBackPressed() {
        val intent = Intent(this,MainActivity::class.java)
        finish()
        startActivity(intent)
    }
    fun clickedAdd()
    {
        val intent = Intent(this, AddActivity::class.java)
        startActivity(intent)
    }

}