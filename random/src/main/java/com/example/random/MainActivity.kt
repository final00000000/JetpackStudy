package com.example.random

import android.net.nsd.NsdManager
import android.os.Bundle
import android.text.method.DigitsKeyListener
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private val minNum = 10
    private val maxNum = 99

    private fun initView() {

        // EditText 获取焦点弹出数字键盘 5.0以上不能切换英文
        val s = "0123456789QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm"
        findViewById<EditText>(R.id.et).keyListener = DigitsKeyListener.getInstance(s)

        val random = findViewById<Button>(R.id.Random)
        random.setOnClickListener {
            findViewById<TextView>(R.id.tv).text =
                "集合长度：${genRanDom().size} \n 数值：${genRanDom().sorted().joinToString()}"
        }
    }

    private fun genRanDom(): List<Int> {
        val mList = mutableListOf<Int>()
        for (i in 1..20) {
            mList.add(Random().nextInt(maxNum) % (maxNum - minNum + 1) + minNum)
        }

        return mList.filter { it == it }
    }

}