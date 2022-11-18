package com.shino72.wallet.Adapter
import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shino72.wallet.Duedate
import com.shino72.wallet.R

class DuedateAdapter(var context : Context): RecyclerView.Adapter<DuedateAdapter.ViewHolder>() {

    var duedateList = emptyList<Duedate>()
    internal fun setDueDateList(duedateList : List<Duedate>){
        this.duedateList = duedateList
    }
    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView)
    {
        var img : ImageView
        var name : TextView
        var date : TextView
        var price : TextView
        init{
            img = itemView.findViewById(R.id.image)
            name = itemView.findViewById(R.id.name)
            date = itemView.findViewById(R.id.date)
            price = itemView.findViewById(R.id.price)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_duedate,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = duedateList[position]
        holder.img.setImageResource(data.imgId)
        holder.name.text = data.name + " [" + data.plan + "]"
        holder.price.text = data.price.toString() + "원"
        if(data.date == "오늘") holder.date.text = data.date
        else holder.date.text = data.date + "일후"

        //       holder.itemView.setOnClickListener {
        //          itemClickLisner.onClick(it, position)
        //    }
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
    override fun getItemCount() = duedateList.size
}
