package com.example.jetpackstudy.im

import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import java.io.File
import java.lang.Exception
import kotlin.jvm.Synchronized
import kotlin.Throws

/**
 * Created by xuas on 2015/5/11.
 */
class CoreDBManager {
    private var coredb: SQLiteDatabase? = null
    private val dblock = 0
    @Synchronized
    fun initCoreDB(coreDBPath: String, userid: String) {
        if (coredb == null) {
            val file = File(coreDBPath)
            if (!file.exists()) {
                file.mkdirs()
            }
            coredb =
                SQLiteDatabase.openOrCreateDatabase(coreDBPath + "coredb_" + userid + ".db", null)
        }
    }

    @Throws(SQLException::class)
    fun execSQL(sql: String?) {
        synchronized(dblock) {
            try {
                coredb!!.execSQL(sql)
            } catch (e: SQLException) {
                throw e
            }
        }
    }

    @Throws(SQLException::class)
    fun execSQL(sql: String?, bindArgs: Array<Any?>?) {
        synchronized(dblock) {
            try {
                coredb!!.execSQL(sql, bindArgs)
            } catch (e: SQLException) {
                throw e
            }
        }
    }

    fun rawQuery(sql: String?, selectionArgs: Array<String?>?): Cursor? {
        var cursor: Cursor? = null
        try {
            cursor = coredb!!.rawQuery(sql, selectionArgs)
            //如果table不存在，cursor有可能返回null，也有可能返回非null，但调用moveToNext或获取count时会抛出表不存在的错误
            //这里使用下面的方法判断cursor是否有效
            if (cursor.count <= 0) {
                cursor = null
            }
        } catch (e: Exception) {
            cursor = null
        }
        return cursor
    }

    fun close() {
        synchronized(dblock) {
            if (coredb != null) {
                coredb!!.close()
                coredb = null
            }
        }
    }
}