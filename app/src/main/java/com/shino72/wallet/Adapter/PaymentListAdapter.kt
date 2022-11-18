package com.shino72.wallet.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.shino72.wallet.Ott
import com.shino72.wallet.R

public class PaymentListAdapter(var context : Context): RecyclerView.Adapter<PaymentListAdapter.ViewHolder>() {

    var recurringList = emptyList<Ott>()
    internal fun setPaymentList(paymentList : List<Ott>){
        this.recurringList = paymentList
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        var img : ImageView
        init{
            img = itemView.findViewById(R.id.image)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_paymentlist,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = recurringList[position]
        holder.img.setImageResource(data.imgId)
//        holder.itemView.setOnClickListener {
        //       itemClickLisner.onClick(it, position)
        // }
    }
    // 온클릭 이벤트
    interface OnItemClickListener{
        fun onClick(v: View, position: Int)
    }
    // 외부 클릭시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        // this.itemClickLisner = onItemClickListener
    }
    private lateinit var itemClickLisner : OnItemClickListener
    override fun getItemCount() = recurringList.size
}