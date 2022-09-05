package com.example.jetpackstudy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Author : zhang
 * @Create Time : 2022/5/30
 * @Class Describe :  项目描述
 * @Project Name : JetpackStudy
 */
@Entity(tableName = Constant.STUDENT_NAME)
data class StudentEntity (

    @PrimaryKey(autoGenerate = true) // id主键自增
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER) // 指定库表的名称和类型
    var id: Int = 0, // id

    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    var name: String = "", // 姓名

    @ColumnInfo(name = "age", typeAffinity = ColumnInfo.INTEGER)
    var age: Int = 0 // 年纪



)
