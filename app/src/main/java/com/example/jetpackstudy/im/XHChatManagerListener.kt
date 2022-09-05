package com.example.jetpackstudy.im

import com.example.jetpackstudy.im.AEvent.notifyListener
import com.example.jetpackstudy.im.MLOC.addHistory
import com.example.jetpackstudy.im.MLOC.saveMessage
import com.starrtc.starrtcsdk.apiInterface.IXHChatManagerListener
import com.starrtc.starrtcsdk.core.im.message.XHIMMessage
import java.text.SimpleDateFormat
import java.util.*

class XHChatManagerListener : IXHChatManagerListener {
    override fun onReceivedMessage(message: XHIMMessage) {
        val historyBean = HistoryBean()
        historyBean.type = CoreDB.HISTORY_TYPE_C2C
        historyBean.lastTime = SimpleDateFormat("MM-dd HH:mm").format(Date())
        historyBean.lastMsg = message.contentData
        historyBean.conversationId = message.fromId
        historyBean.newMsgCount = 1
        addHistory(historyBean, false)
        val messageBean = MessageBean()
        messageBean.conversationId = message.fromId
        messageBean.time = SimpleDateFormat("MM-dd HH:mm").format(Date())
        messageBean.msg = message.contentData
        messageBean.fromId = message.fromId
        saveMessage(messageBean)
        notifyListener(AEvent.AEVENT_C2C_REV_MSG, true, message)
    }

    override fun onReceivedSystemMessage(message: XHIMMessage) {
        val historyBean = HistoryBean()
        historyBean.type = CoreDB.HISTORY_TYPE_C2C
        historyBean.lastTime = SimpleDateFormat("MM-dd HH:mm").format(Date())
        historyBean.lastMsg = message.contentData
        historyBean.conversationId = message.fromId
        historyBean.newMsgCount = 1
        addHistory(historyBean, false)
        val messageBean = MessageBean()
        messageBean.conversationId = message.fromId
        messageBean.time = SimpleDateFormat("MM-dd HH:mm").format(Date())
        messageBean.msg = message.contentData
        messageBean.fromId = message.fromId
        saveMessage(messageBean)
        notifyListener(AEvent.AEVENT_REV_SYSTEM_MSG, true, message)
    }
}