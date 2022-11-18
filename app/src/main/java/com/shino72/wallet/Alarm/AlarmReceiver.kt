package com.shino72.wallet.Alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.shino72.wallet.AppDatabase
import com.shino72.wallet.Date
import com.shino72.wallet.MainActivity
import com.shino72.wallet.Plan
import com.shino72.wallet.Preference.App
import com.shino72.wallet.R
import java.util.*
lateinit var notificationManager: NotificationManager
var db : AppDatabase? = null
const val CHANNETL_ID = "SHINO72"
val get_requestcode = 0
class AlarmReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals("android.intent.action.BOOT_COMPLETED")){
            var prefs = App.prefs
            if(prefs.EditStatus){
                val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
                var pendingIntent = PendingIntent.getBroadcast(
                    context, 0, intent, PendingIntent.FLAG_IMMUTABLE
                )
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
                Toast.makeText(context,"알람재등록",Toast.LENGTH_SHORT).show()
            }
        }
        notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
        creatNotificationChannel()
        deliverNotification(context)
    }
}
// 체널 등록
fun creatNotificationChannel()
{
    val notificationChannel = NotificationChannel(CHANNETL_ID,"정기결제 알림",NotificationManager.IMPORTANCE_HIGH)
    notificationChannel.enableLights(false) // 불빛
    notificationChannel.enableVibration(false) // 진동
    notificationManager.createNotificationChannel(notificationChannel)

}
// notification 설정
fun deliverNotification(context:Context)
{
    var name = mutableListOf<String>()
    var price = 0
    var PlanList = mutableListOf<Plan>()
    db = AppDatabase.getInstance(context)
    val beforeData = db!!.planDao().getAll()
    if(beforeData.isNotEmpty()) PlanList.addAll(beforeData)
    for(data in PlanList)
    {
        var dt = Date()
        var maxDate = dt.getLastDate()
        var nowDate = dt.day
        var pmDate = Math.min(maxDate, data.duedate)
        if(pmDate - 1 == nowDate) {
            name.add(data.korean)
            price += data.price
        }
    }

    val contentIntent = Intent(context, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        context,
        get_requestcode,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val builder = NotificationCompat.Builder(context, CHANNETL_ID)
        .setSmallIcon(R.mipmap.appicon)
        .setContentTitle("내일 %d원 결제 예정".format(price))
        .setContentText("결제 목록 : %s".format(name.joinToString(",")))
        .setContentIntent(contentPendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setChannelId(CHANNETL_ID)
        .setDefaults(NotificationCompat.DEFAULT_ALL)
    notificationManager.notify(101, builder.build())
}