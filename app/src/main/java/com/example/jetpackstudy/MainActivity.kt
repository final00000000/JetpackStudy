package com.example.jetpackstudy

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jetpackstudy.im.C2CActivity
import com.example.jetpackstudy.im.KeepLiveService
import com.example.jetpackstudy.im.MLOC
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var studentDao: StudentDao? = null
//    private val studentDao = DemoApplication().getDao().getStudentDao()

    private var sAdapter = StudentAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MLOC.userId = MLOC.loadSharedData(applicationContext,"userId");
        val intent = Intent(this, KeepLiveService::class.java)
        startService(intent)

        studentDao = StudentDataBase.getInstance(this@MainActivity).getStudentDao()

        studentRv.adapter = sAdapter

        insert.setOnClickListener {
            if (nameET.text!!.isNotEmpty() && ageET.text!!.isNotEmpty()) {
                val s1 = StudentEntity(0, nameET.text.toString(), ageET.text.toString().toInt())
                studentDao!!.insertStudent(s1)
                sAdapter.setStudents(studentDao!!.getAllStudent)
                sAdapter.notifyDataSetChanged()
                nameET.setText("")
                ageET.setText("")
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "请确保填写完整", Toast.LENGTH_SHORT).show()
            }
        }
        query.setOnClickListener {

            MLOC.saveC2CUserId(this@MainActivity, ageET.text.toString())
            val intent = Intent(this@MainActivity, C2CActivity::class.java)
            intent.putExtra("targetId", ageET.text.toString())
            startActivity(intent)
//            sAdapter.setStudents(studentDao!!.getAllStudent)
//            sAdapter.notifyDataSetChanged()
//            Log.e("TAG", "onCreate: ${studentDao!!.getAllStudent}")
        }

        delete.setOnClickListener {
            Toast.makeText(this, "待完善", Toast.LENGTH_SHORT).show()
        }
        update.setOnClickListener {
            Toast.makeText(this, "待完善", Toast.LENGTH_SHORT).show()
        }
    }
}