package com.example.rafaelmarra.pertubis.model.disturb

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface RoomDaoDisturb {

    @Query("SELECT * from disturbs")
    fun getAll(): LiveData<List<Disturb>>

    @Query("SELECT * from disturbs WHERE id = :id")
    fun getFromId(id: Long): LiveData<Disturb>

    @Update
    fun update(disturb: Disturb)

    @Insert(onConflict = REPLACE)
    fun insert(disturb: Disturb)

    @Delete
    fun delete(disturb: Disturb)
}