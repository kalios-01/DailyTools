package com.kalios.roomdbdemo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Query("Select * from userTable order by id ASC")
    suspend fun getAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg users: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM userTable")
    suspend fun deleteAll()

    @Update
    suspend fun update(user: User)


}