package com.example.jetpackstudy

import android.app.Application
import android.content.Context
import androidx.room.Room

/**
 * @Author : zhang
 * @Create Time : 2022/5/31
 * @Class Describe :  项目描述
 * @Project Name : JetpackStudy
 */
class DemoApplication : Application() {
    private lateinit var mContext: Context

    var studentDao: StudentDataBase? = null


    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext

//        initRoom()
    }

    fun getContext(): Context {
        return mContext
    }

    fun getDao(): StudentDataBase {
        return studentDao!!
    }

    private fun initRoom() {
        studentDao = Room.databaseBuilder(
            mContext,
            StudentDataBase::class.java,
            Constant.STUDENT_NAME
        )
            .allowMainThreadQueries()
            .build()
    }
}