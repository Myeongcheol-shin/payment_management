package com.shino72.wallet.Adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.shino72.wallet.PaymentPlan
import com.shino72.wallet.R

class ManageAdapter(var context : Context): RecyclerView.Adapter<ManageAdapter.ViewHolder>() {

    var manageList = emptyList<PaymentPlan>()
    internal fun setPaymentList(manageList : List<PaymentPlan>){
        this.manageList = manageList
    }
    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView)
    {
        var img : ImageView
        var name : TextView
        var date : TextView
        var price : TextView
        var duedate : TextView
        var plan : TextView
        var memo : TextView
        var id : TextView
        var delete : AppCompatButton
        init{
            img = itemView.findViewById(R.id.image)
            name = itemView.findViewById(R.id.name)
            date = itemView.findViewById(R.id.duedate)
            price = itemView.findViewById(R.id.price)
            duedate = itemView.findViewById(R.id.duedate)
            plan = itemView.findViewById(R.id.plan)
            memo = itemView.findViewById(R.id.memo)
            id = itemView.findViewById(R.id.saveid)
            delete = itemView.findViewById(R.id.delete)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_management,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = manageList[position]
        holder.img.setImageResource(data.imgId)
        holder.name.text = data.name
        holder.date.text = data.date
        holder.price.text = data.price.toString() + "원"
        holder.duedate.text = "매월 " +data.date.toString() +"일 결제"
        holder.plan.text = data.plan
        holder.memo.text = data.memo
        holder.id.text = data.num.toString()
        holder.itemView.setOnClickListener {
            itemClickLisner.onClick(it, position)
        }
        holder.delete.setOnClickListener {
            itemClickLisner.onDelete(it,position,data.num)
        }
    }
    // 온클릭 이벤트
    interface OnItemClickListener{
        fun onClick(v: View, position: Int){
        }
        fun onDelete(v:View, position: Int, id: Int){
        }
        fun onRevise(v:View, position: Int){
        }
    }
    // 외부 클릭시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickLisner = onItemClickListener
    }
    private lateinit var itemClickLisner : OnItemClickListener
    override fun getItemCount() = manageList.size
}