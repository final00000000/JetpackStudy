package com.example.jetpackstudy

import androidx.room.*


/**
 * @Author : zhang
 * @Create Time : 2022/5/30
 * @Class Describe :  项目描述
 * @Project Name : JetpackStudy
 */
@Dao
interface StudentDao {

    @Insert
    fun insertStudent(studentEntity: StudentEntity)

    @Delete
    fun deleteStudent(studentEntity: StudentEntity)

    @Update
    fun updateStudent(studentEntity: StudentEntity)


    @get:Query("SELECT * FROM ${Constant.STUDENT_NAME}")
    val getAllStudent: MutableList<StudentEntity>

    @Query("SELECT * FROM ${Constant.STUDENT_NAME} WHERE name = :name")
    fun getStudentByName(name: String): List<StudentEntity?>?


}