package com.example.jetpackstudy.im

import android.os.Handler
import com.example.jetpackstudy.im.AEvent.EventObj
import com.example.jetpackstudy.im.AEvent
import com.example.jetpackstudy.im.IEventListener
import android.os.Looper
import java.util.ArrayList

object AEvent {
    private val callBackList: MutableList<EventObj> = ArrayList()
    private var mHandler: Handler? = null
    fun setHandler(handler: Handler?) {
        mHandler = handler
    }

    @JvmStatic
    fun addListener(eventID: String, eventListener: IEventListener) {
        for (eventObj in callBackList) {
            if (eventObj.eventID == eventID && eventObj.eventListener!!.javaClass == eventListener.javaClass) {
                return
            }
        }
        val event = EventObj()
        event.eventListener = eventListener
        event.eventID = eventID
        callBackList.add(event)
    }

    @JvmStatic
    fun removeListener(eventID: String, eventListener: IEventListener) {
        var i: Int
        var event: EventObj
        i = 0
        while (i < callBackList.size) {
            event = callBackList[i]
            if (event.eventID == eventID && event.eventListener === eventListener) {
                callBackList.removeAt(i)
                return
                //i--;
            }
            i++
        }
    }

    @JvmStatic
    fun notifyListener(eventID: String, success: Boolean, `object`: Any?) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            // UI线程
            var i: Int
            var event: EventObj
            i = 0
            while (i < callBackList.size) {
                event = callBackList[i]
                if (event.eventID == eventID) {
                    event.eventListener!!.dispatchEvent(eventID, success, `object`)
                }
                i++
            }
        } else {
            // 非UI线程
            if (mHandler != null) {
                mHandler!!.post {
                    var i: Int
                    var event: EventObj
                    i = 0
                    while (i < callBackList.size) {
                        event = callBackList[i]
                        if (event.eventID == eventID) {
                            event.eventListener!!.dispatchEvent(eventID, success, `object`)
                        }
                        i++
                    }
                }
            } else {
                var i: Int
                var event: EventObj
                i = 0
                while (i < callBackList.size) {
                    event = callBackList[i]
                    if (event.eventID == eventID) {
                        event.eventListener!!.dispatchEvent(eventID, success, `object`)
                    }
                    i++
                }
            }
        }
    }

    //事件类型在这里定义
    const val AEVENT_LOGIN = "AEVENT_LOGIN"
    const val AEVENT_LOGOUT = "AEVENT_LOGOUT"
    const val AEVENT_RESET = "AEVENT_RESET"
    const val AEVENT_GROUP_GOT_LIST = "AEVENT_GROUP_GOT_LIST"
    const val AEVENT_GROUP_GOT_MEMBER_LIST = "AEVENT_GROUP_GOT_MEMBER_LIST"
    const val AEVENT_GOT_ONLINE_USER_LIST = "AEVENT_GOT_ONLINE_USER_LIST"
    const val AEVENT_VOIP_INIT_COMPLETE = "AEVENT_VOIP_INIT_COMPLETE"
    const val AEVENT_VOIP_REV_CALLING = "AEVENT_VOIP_REV_CALLING"
    const val AEVENT_VOIP_REV_CALLING_AUDIO = "AEVENT_VOIP_REV_CALLING_AUDIO"
    const val AEVENT_VOIP_REV_REFUSED = "AEVENT_VOIP_REV_REFUSED"
    const val AEVENT_VOIP_REV_HANGUP = "AEVENT_VOIP_REV_HANGUP"
    const val AEVENT_VOIP_REV_BUSY = "AEVENT_VOIP_REV_BUSY"
    const val AEVENT_VOIP_REV_MISS = "AEVENT_VOIP_REV_MISS"
    const val AEVENT_VOIP_REV_CONNECT = "AEVENT_VOIP_REV_CONNECT"
    const val AEVENT_VOIP_REV_ERROR = "AEVENT_VOIP_REV_ERROR"
    const val AEVENT_VOIP_REV_REALTIME_DATA = "AEVENT_VOIP_REV_REALTIME_DATA"
    const val AEVENT_VOIP_P2P_REV_CALLING = "AEVENT_VOIP_P2P_REV_CALLING"
    const val AEVENT_VOIP_P2P_REV_CALLING_AUDIO = "AEVENT_VOIP_P2P_REV_CALLING_AUDIO"
    const val AEVENT_VOIP_TRANS_STATE_CHANGED = "AEVENT_VOIP_TRANS_STATE_CHANGED"
    const val AEVENT_LIVE_ADD_UPLOADER = "AEVENT_LIVE_ADD_UPLOADER"
    const val AEVENT_LIVE_REMOVE_UPLOADER = "AEVENT_LIVE_REMOVE_UPLOADER"
    const val AEVENT_LIVE_ERROR = "AEVENT_LIVE_ERROR"
    const val AEVENT_LIVE_GET_ONLINE_NUMBER = "AEVENT_LIVE_GET_ONLINE_NUMBER"
    const val AEVENT_LIVE_SELF_KICKED = "AEVENT_LIVE_SELF_KICKED"
    const val AEVENT_LIVE_SELF_BANNED = "AEVENT_LIVE_SELF_BANNED"
    const val AEVENT_LIVE_REV_MSG = "AEVENT_LIVE_REV_MSG"
    const val AEVENT_LIVE_REV_PRIVATE_MSG = "AEVENT_LIVE_REV_PRIVATE_MSG"
    const val AEVENT_LIVE_APPLY_LINK = "AEVENT_LIVE_APPLY_LINK"
    const val AEVENT_LIVE_APPLY_LINK_RESULT = "AEVENT_LIVE_APPLY_LINK_RESULT"
    const val AEVENT_LIVE_INVITE_LINK = "AEVENT_LIVE_INVITE_LINK"
    const val AEVENT_LIVE_INVITE_LINK_RESULT = "AEVENT_LIVE_INVITE_LINK_RESULT"
    const val AEVENT_LIVE_SELF_COMMANDED_TO_STOP = "AEVENT_LIVE_SELF_COMMANDED_TO_STOP"
    const val AEVENT_LIVE_REV_REALTIME_DATA = "AEVENT_LIVE_REV_REALTIME_DATA"
    const val AEVENT_LIVE_PUSH_STREAM_ERROR = "AEVENT_LIVE_PUSH_STREAM_ERROR"
    const val AEVENT_SUPER_ROOM_ADD_UPLOADER = "AEVENT_SUPER_ROOM_ADD_UPLOADER"
    const val AEVENT_SUPER_ROOM_REMOVE_UPLOADER = "AEVENT_SUPER_ROOM_REMOVE_UPLOADER"
    const val AEVENT_SUPER_ROOM_ERROR = "AEVENT_SUPER_ROOM_ERROR"
    const val AEVENT_SUPER_ROOM_GET_ONLINE_NUMBER = "AEVENT_SUPER_ROOM_GET_ONLINE_NUMBER"
    const val AEVENT_SUPER_ROOM_SELF_KICKED = "AEVENT_SUPER_ROOM_SELF_KICKED"
    const val AEVENT_SUPER_ROOM_SELF_BANNED = "AEVENT_SUPER_ROOM_SELF_BANNED"
    const val AEVENT_SUPER_ROOM_REV_MSG = "AEVENT_SUPER_ROOM_REV_MSG"
    const val AEVENT_SUPER_ROOM_REV_PRIVATE_MSG = "AEVENT_SUPER_ROOM_REV_PRIVATE_MSG"
    const val AEVENT_SUPER_ROOM_APPLY_LINK = "AEVENT_SUPER_ROOM_APPLY_LINK"
    const val AEVENT_SUPER_ROOM_SELF_COMMANDED_TO_STOP = "AEVENT_SUPER_ROOM_SELF_COMMANDED_TO_STOP"
    const val AEVENT_SUPER_ROOM_REV_REALTIME_DATA = "AEVENT_SUPER_ROOM_REV_REALTIME_DATA"
    const val AEVENT_SUPER_ROOM_PUSH_STREAM_ERROR = "AEVENT_SUPER_ROOM_PUSH_STREAM_ERROR"
    const val AEVENT_MEETING_ADD_UPLOADER = "AEVENT_MEETING_ADD_UPLOADER"
    const val AEVENT_MEETING_REMOVE_UPLOADER = "AEVENT_MEETING_REMOVE_UPLOADER"
    const val AEVENT_MEETING_ERROR = "AEVENT_MEETING_ERROR"
    const val AEVENT_MEETING_GET_ONLINE_NUMBER = "AEVENT_MEETING_GET_ONLINE_NUMBER"
    const val AEVENT_MEETING_SELF_KICKED = "AEVENT_MEETING_SELF_KICKED"
    const val AEVENT_MEETING_SELF_BANNED = "AEVENT_MEETING_SELF_BANNED"
    const val AEVENT_MEETING_REV_MSG = "AEVENT_MEETING_REV_MSG"
    const val AEVENT_MEETING_REV_PRIVATE_MSG = "AEVENT_MEETING_REV_PRIVATE_MSG"
    const val AEVENT_MEETING_REV_REALTIME_DATA = "AEVENT_MEETING_REV_REALTIME_DATA"
    const val AEVENT_MEETING_PUSH_STREAM_ERROR = "AEVENT_MEETING_PUSH_STREAM_ERROR"
    const val AEVENT_ECHO_FIN = "AEVENT_ECHO_FIN"
    const val AEVENT_CHATROOM_ERROR = "AEVENT_CHATROOM_ERROR"
    const val AEVENT_CHATROOM_STOP_OK = "AEVENT_CHATROOM_STOP_OK"
    const val AEVENT_CHATROOM_DELETE_OK = "AEVENT_CHATROOM_DELETE_OK"
    const val AEVENT_CHATROOM_SELF_BANNED = "AEVENT_CHATROOM_SELF_BANNED"
    const val AEVENT_CHATROOM_SELF_KICKED = "AEVENT_CHATROOM_SELF_KICKED"
    const val AEVENT_CHATROOM_REV_MSG = "AEVENT_CHATROOM_REV_MSG"
    const val AEVENT_CHATROOM_REV_PRIVATE_MSG = "AEVENT_CHATROOM_REV_PRIVATE_MSG"
    const val AEVENT_CHATROOM_GET_ONLINE_NUMBER = "AEVENT_CHATROOM_GET_ONLINE_NUMBER"
    const val AEVENT_C2C_REV_MSG = "AEVENT_C2C_REV_MSG"
    const val AEVENT_GROUP_REV_MSG = "AEVENT_GROUP_REV_MSG"
    const val AEVENT_REV_SYSTEM_MSG = "AEVENT_REV_SYSTEM_MSG"
    const val AEVENT_CONN_DEATH = "AEVENT_CONN_DEATH"
    const val AEVENT_USER_KICKED = "AEVENT_USER_KICKED"
    const val AEVENT_USER_ONLINE = "AEVENT_USER_ONLINE"
    const val AEVENT_USER_OFFLINE = "AEVENT_USER_OFFLINE"
    const val AEVENT_RTSP_FORWARD = "AEVENT_RTSP_FORWARD"
    const val AEVENT_RTSP_STOP = "AEVENT_RTSP_STOP"
    const val AEVENT_RTSP_RESUME = "AEVENT_RTSP_RESUME"
    const val AEVENT_RTSP_DELETE = "AEVENT_RTSP_DELETE"
    const val AEVENT_GOT_LIST = "AEVENT_GOT_LIST"

    private class EventObj {
        var eventListener: IEventListener? = null
        var eventID: String? = null
    }
}