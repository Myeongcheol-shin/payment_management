package com.shino72.wallet

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
public final data class Plan(
    @PrimaryKey(autoGenerate = true) val uid : Int = 0,
    val platform : String,
    val korean : String,
    val name : String,
    val price : Int,
    val duedate : Int,
    val memo : String?
)