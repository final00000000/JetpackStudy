package com.example.jetpackstudy

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * @Author : zhang
 * @Create Time : 2022/5/30
 * @Class Describe :  项目描述
 * @Project Name : JetpackStudy
 */
@Database(entities = [StudentEntity::class], version = 1, exportSchema = false)
abstract class StudentDataBase : RoomDatabase() {

    companion object {
        private var mInstance: StudentDataBase? = null

        @Synchronized
        fun getInstance(context: Context): StudentDataBase {
            if (mInstance == null) {
                mInstance = Room.databaseBuilder(
                    context.applicationContext,
                    StudentDataBase::class.java,
                    Constant.STUDENT_NAME
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return mInstance!!
        }

    }

    abstract fun getStudentDao(): StudentDao
}