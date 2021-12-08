package com.sokind.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sokind.data.local.user.UserDao
import com.sokind.data.local.user.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
//    abstract fun recentSearchDao(): RecentSearchDao
}