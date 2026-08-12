package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserEntity::class,
        StudentProfileEntity::class,
        UniversityEntity::class,
        DocumentEntity::class,
        ApplicationEntity::class,
        BookingEntity::class,
        SeminarEntity::class,
        ExpertEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class IshcDatabase : RoomDatabase() {

    abstract fun ishcDao(): IshcDao

    companion object {
        @Volatile
        private var INSTANCE: IshcDatabase? = null

        fun getDatabase(context: Context): IshcDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IshcDatabase::class.java,
                    "ishc_help_center.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
