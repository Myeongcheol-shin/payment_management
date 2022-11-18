package com.shino72.wallet

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
public interface planDao{
    // 전부 가져오기
    @Query("SELECT * FROM 'Plan'")
    fun getAll() : List<Plan>
    @Query("DELETE FROM 'Plan'")
    fun deleteAll()
    @Query("DELETE FROM 'Plan' WHERE uid = :userId")
    fun deleteByUserId(userId: Int)

    // 삽입 하기
    @Insert
    fun insertDB(plan: Plan)

    // 삭제 하기
    @Delete
    fun delete(plan:Plan)
}