package com.shino72.wallet

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.shino72.wallet.Adapter.AddAdapter
import com.shino72.wallet.Adapter.DuedateAdapter
import com.shino72.wallet.Adapter.PaymentListAdapter
import com.shino72.wallet.Alarm.AlarmReceiver
import com.shino72.wallet.Preference.App
import java.util.*

class MainActivity : AppCompatActivity() {
    var db : AppDatabase? = null
    var dataList = mutableListOf<Plan>()
    private lateinit var paymentAdapter: PaymentListAdapter
    private lateinit var duedateAdapter: DuedateAdapter
    private var paymentList = mutableListOf<Ott>()
    private var duedateList = mutableListOf<Duedate>()
    var backpressTime : Long = 0
    var prefs = App.prefs
    private lateinit var AlarmButton : AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // db 초기화
        db = AppDatabase.getInstance(this)
        val beforeData = db!!.planDao().getAll()
        if(beforeData.isNotEmpty()) dataList.addAll(beforeData)

        // 관리 버튼
        val manageButton = findViewById<AppCompatButton>(R.id.manageButton)
        manageButton.setOnClickListener {
            manageClickEvent()
        }
        // 알람버튼
        AlarmButton = findViewById(R.id.AlarmButton)
        when(prefs.EditStatus){
            true -> {AlarmButton.setBackgroundResource(R.drawable.icon_alarm_on)}
            false -> {AlarmButton.setBackgroundResource(R.drawable.icon_alarm_off)}
        }

        AlarmButton.setOnClickListener {
            setAlarm()
        }

        // 리사이클러 뷰
        val paymentListView : RecyclerView = findViewById(R.id.recurringView)
        paymentListView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        paymentAdapter = PaymentListAdapter(applicationContext)
        paymentListView.adapter = paymentAdapter

        val duedateView : RecyclerView = findViewById(R.id.duedateView)
        duedateView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        duedateAdapter = DuedateAdapter(applicationContext)
        duedateView.adapter = duedateAdapter

        var sumPrice = 0
        var remainPrice = 0

        val priceText : TextView = findViewById(R.id.priceText)
        val sortedList = dataList.sortedBy { it.duedate }
        var Date = Date()
        Date.setDate()
        var lastday = Date.getLastDate()
        for(i in sortedList){
            sumPrice += i.price
            var imgid = resources.getIdentifier("appicon_"+i.platform,"drawable",packageName)
            paymentList.add(Ott(i.name,imgid))
            if(Date.day == i.duedate){
                remainPrice += i.price
                duedateList.add(Duedate(i.korean,imgid,i.price, "오늘",i.name))
            }
            else if(Date.day <= i.duedate){
                remainPrice += i.price
                if (lastday >= i.duedate) duedateList.add(Duedate(i.korean,imgid,i.price,(i.duedate - Date.day).toString(), i.name))
                else duedateList.add(Duedate(i.korean,imgid,i.price,(lastday - Date.day).toString(), i.name))
            }
        }

        priceText.text = sumPrice.toString() + "원"
        val pricedueText : TextView = findViewById(R.id.pricedueText)
        pricedueText.text = remainPrice.toString() + "원"

        paymentAdapter.setPaymentList(paymentList)
        duedateAdapter.setDueDateList(duedateList)
    }
    fun setAlarm()
    {

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        var pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        prefs = App.prefs
        when(prefs.EditStatus) {
            true -> {
                AlarmButton.setBackgroundResource(R.drawable.icon_alarm_off)
                prefs.EditStatus = false
                // 알람 끄기
                alarmManager.cancel(pendingIntent)
                Toast.makeText(applicationContext,"알람 OFF", Toast.LENGTH_SHORT).show()
            }
            false -> {
                AlarmButton.setBackgroundResource(R.drawable.icon_alarm_on)
                prefs.EditStatus = true

                // 알람 설정

                val calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, 12)
                    set(Calendar.MINUTE, 0)
                }
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
                Toast.makeText(applicationContext,"알람 ON", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onBackPressed() {
        if(System.currentTimeMillis() - backpressTime >= 2000){
            backpressTime = System.currentTimeMillis()
            Snackbar.make(findViewById(R.id.mainLayout), "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Snackbar.LENGTH_LONG).show()
        }
        else{
            // 앱 종료
            ActivityCompat.finishAffinity(this)
            System.exit(0)
        }
    }
    fun manageClickEvent()
    {
        val intent = Intent(this, ManageActivity::class.java)
        startActivity(intent)
    }
}