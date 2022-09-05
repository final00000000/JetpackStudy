package com.example.jetpackstudy.im

import android.app.Service
import com.example.jetpackstudy.im.AEvent.addListener
import com.example.jetpackstudy.im.AEvent.removeListener
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import com.starrtc.starrtcsdk.api.XHClient
import com.starrtc.starrtcsdk.api.XHCustomConfig
import com.starrtc.starrtcsdk.apiInterface.IXHResultCallback
import java.util.*

/**
 * Created by zhangjt on 2017/8/6.
 */
class KeepLiveService : Service(), IEventListener {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeListener()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        initSDK()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initSDK() {
        MLOC.init(this)
        initFree()
    }

    private var isLogin = false

    //开放版SDK初始化
    private fun initFree() {
        MLOC.d("KeepLiveService", "initFree")
        isLogin = XHClient.getInstance().isOnline
        if (!isLogin) {
            if (MLOC.userId == "") {
                MLOC.userId = "" + (Random().nextInt(900000) + 100000)
                MLOC.saveUserId(MLOC.userId)
            }
            addListener()
            val customConfig = XHCustomConfig.getInstance(this)
            customConfig.chatroomServerUrl = MLOC.CHATROOM_SERVER_URL
            customConfig.liveSrcServerUrl = MLOC.LIVE_SRC_SERVER_URL
            customConfig.liveVdnServerUrl = MLOC.LIVE_VDN_SERVER_URL
            customConfig.setImServerUrl(MLOC.IM_SERVER_URL)
            customConfig.voipServerUrl = MLOC.VOIP_SERVER_URL
            //            customConfig.setLogEnable(false); //关闭SDK调试日志
//            customConfig.setDefConfigOpenGLESEnable(false);
//            customConfig.setDefConfigCameraId(1);//设置默认摄像头方向  0后置  1前置
//            customConfig.setDefConfigVideoSize(XHConstants.XHCropTypeEnum.STAR_VIDEO_CONFIG_360BW_640BH_180SW_320SH);
//            customConfig.setLogDirPath(Environment.getExternalStorageDirectory().getPath()+"/starrtcLog");
//            customConfig.setDefConfigCamera2Enable(false);
//            StarCamera.setFrameBufferEnable(false);
            customConfig.initSDKForFree(MLOC.userId, { errMsg, data ->
                MLOC.e("KeepLiveService", "error:$errMsg")
                MLOC.showMsg(this@KeepLiveService, errMsg)
            }, Handler())
            XHClient.getInstance().chatManager.addListener(XHChatManagerListener())
            //            XHClient.getInstance().getGroupManager().addListener(new XHGroupManagerListener());
//            XHClient.getInstance().getVoipManager().addListener(new XHVoipManagerListener());
//            XHClient.getInstance().getVoipP2PManager().addListener(new XHVoipP2PManagerListener());
//            XHClient.getInstance().getLoginManager().addListener(new XHLoginManagerListener());
//            XHVideoSourceManager.getInstance().setVideoSourceCallback(new DemoVideoSourceCallback());
            XHClient.getInstance().loginManager.loginFree(object : IXHResultCallback {
                override fun success(data: Any) {
                    MLOC.d("KeepLiveService", "loginSuccess")
                    isLogin = true
                }

                override fun failed(errMsg: String) {
                    MLOC.d("KeepLiveService", "loginFailed $errMsg")
                    MLOC.showMsg(this@KeepLiveService, errMsg)
                }
            })
        }
    }

    override fun dispatchEvent(aEventID: String?, success: Boolean, eventObj: Any?) {
        when (aEventID) {
            AEvent.AEVENT_VOIP_REV_CALLING -> {}
            AEvent.AEVENT_VOIP_REV_CALLING_AUDIO -> {}
            AEvent.AEVENT_VOIP_P2P_REV_CALLING -> if (MLOC.canPickupVoip) {
//                    Intent intent = new Intent(this, VoipP2PRingingActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
//                    intent.putExtra("targetId",eventObj.toString());
//                    startActivity(intent);
            }
            AEvent.AEVENT_VOIP_P2P_REV_CALLING_AUDIO -> if (MLOC.canPickupVoip) {
//                    Intent intent = new Intent(this, VoipP2PRingingActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
//                    intent.putExtra("targetId",eventObj.toString());
//                    startActivity(intent);
            }
            AEvent.AEVENT_C2C_REV_MSG, AEvent.AEVENT_REV_SYSTEM_MSG -> MLOC.hasNewC2CMsg = true
            AEvent.AEVENT_GROUP_REV_MSG -> MLOC.hasNewGroupMsg = true
            AEvent.AEVENT_LOGOUT -> {
                removeListener()
                this.stopSelf()
            }
            AEvent.AEVENT_USER_KICKED, AEvent.AEVENT_CONN_DEATH -> {
                MLOC.d("KeepLiveService", "AEVENT_USER_KICKED OR AEVENT_CONN_DEATH")
                XHClient.getInstance().loginManager.loginFree(object : IXHResultCallback {
                    override fun success(data: Any) {
                        MLOC.d("KeepLiveService", "loginSuccess")
                        isLogin = true
                    }

                    override fun failed(errMsg: String) {
                        MLOC.d("KeepLiveService", "loginFailed $errMsg")
                        MLOC.showMsg(this@KeepLiveService, errMsg)
                    }
                })
            }
        }
    }

    private fun addListener() {
        addListener(AEvent.AEVENT_LOGOUT, this)
        addListener(AEvent.AEVENT_VOIP_REV_CALLING, this)
        addListener(AEvent.AEVENT_VOIP_REV_CALLING_AUDIO, this)
        addListener(AEvent.AEVENT_VOIP_P2P_REV_CALLING, this)
        addListener(AEvent.AEVENT_C2C_REV_MSG, this)
        addListener(AEvent.AEVENT_REV_SYSTEM_MSG, this)
        addListener(AEvent.AEVENT_GROUP_REV_MSG, this)
        addListener(AEvent.AEVENT_USER_KICKED, this)
        addListener(AEvent.AEVENT_CONN_DEATH, this)
    }

    private fun removeListener() {
        removeListener(AEvent.AEVENT_LOGOUT, this)
        removeListener(AEvent.AEVENT_VOIP_REV_CALLING, this)
        removeListener(AEvent.AEVENT_VOIP_REV_CALLING_AUDIO, this)
        removeListener(AEvent.AEVENT_VOIP_P2P_REV_CALLING, this)
        removeListener(AEvent.AEVENT_C2C_REV_MSG, this)
        removeListener(AEvent.AEVENT_REV_SYSTEM_MSG, this)
        removeListener(AEvent.AEVENT_GROUP_REV_MSG, this)
        removeListener(AEvent.AEVENT_USER_KICKED, this)
        removeListener(AEvent.AEVENT_CONN_DEATH, this)
    }
}