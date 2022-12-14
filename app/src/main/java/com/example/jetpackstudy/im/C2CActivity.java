package com.example.jetpackstudy.im;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jetpackstudy.R;
import com.starrtc.starrtcsdk.api.XHClient;
import com.starrtc.starrtcsdk.apiInterface.IXHResultCallback;
import com.starrtc.starrtcsdk.core.im.message.XHIMMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class C2CActivity extends Activity implements IEventListener, AdapterView.OnItemLongClickListener {

    private EditText vEditText;
    private TextView vTargetId;
    private ListView vMsgList;
    private View vSendBtn;

    private String mTargetId;
    private List<MessageBean> mDatas;
    private MyChatroomListAdapter mAdapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c2c);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        findViewById(R.id.title_left_btn).setVisibility(View.VISIBLE);
        findViewById(R.id.title_left_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addListener();
        vEditText = (EditText) findViewById(R.id.id_input);
        mDatas = new ArrayList<>();

        mTargetId = getIntent().getStringExtra("targetId");
        ((TextView)findViewById(R.id.title_text)).setText(mTargetId);
        mAdapter = new MyChatroomListAdapter();
        vMsgList = (ListView) findViewById(R.id.msg_list);
        vMsgList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        vMsgList.setOnItemLongClickListener(this);
        mAdapter = new MyChatroomListAdapter();
        vMsgList.setAdapter(mAdapter);


        vSendBtn = findViewById(R.id.send_btn);
        vSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = vEditText.getText().toString();
                if(!TextUtils.isEmpty(txt)){
                    sendMsg(txt);
                    vEditText.setText("");
                }
            }
        });

    }

    private void sendMsg(String msg){
        XHIMMessage message = XHClient.getInstance().getChatManager().sendMessage(msg, mTargetId, new IXHResultCallback() {
            @Override
            public void success(Object data) {
                MLOC.d("IM_C2C  ??????","???????????????"+data);
            }

            @Override
            public void failed(String errMsg) {
                MLOC.d("IM_C2C  ??????","???????????????"+errMsg);
            }
        });

        HistoryBean historyBean = new HistoryBean();
        historyBean.setType(CoreDB.HISTORY_TYPE_C2C);
        historyBean.setLastTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
        historyBean.setLastMsg(message.contentData);
        historyBean.setConversationId(message.targetId);
        historyBean.setNewMsgCount(1);
        MLOC.addHistory(historyBean,true);

        MessageBean messageBean = new MessageBean();
        messageBean.setConversationId(message.targetId);
        messageBean.setTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
        messageBean.setMsg(message.contentData);
        messageBean.setFromId(message.fromId);
        MLOC.saveMessage(messageBean);

        ColorUtils.getColor(this,message.fromId);
        mDatas.add(messageBean);
        mAdapter.notifyDataSetChanged();
    }



    private void addListener(){
        AEvent.addListener(AEvent.AEVENT_C2C_REV_MSG,this);
        AEvent.addListener(AEvent.AEVENT_REV_SYSTEM_MSG,this);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        addListener();
    }

    @Override
    public void onResume(){
        super.onResume();
        mDatas.clear();
        List<MessageBean> list =  MLOC.getMessageList(mTargetId);
        if(list!=null&&list.size()>0){
            mDatas.addAll(list);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop(){
        AEvent.removeListener(AEvent.AEVENT_C2C_REV_MSG,this);
        AEvent.removeListener(AEvent.AEVENT_REV_SYSTEM_MSG,this);
        super.onStop();
    }



    @Override
    public void dispatchEvent(String aEventID, boolean success, final Object eventObj) {
        MLOC.d("IM_C2C",aEventID+"||"+eventObj);
        switch (aEventID){
            case AEvent.AEVENT_C2C_REV_MSG:
            case AEvent.AEVENT_REV_SYSTEM_MSG:
                final XHIMMessage revMsg = (XHIMMessage) eventObj;
                if(revMsg.fromId.equals(mTargetId)){
                    HistoryBean historyBean = new HistoryBean();
                    historyBean.setType(CoreDB.HISTORY_TYPE_C2C);
                    historyBean.setLastTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
                    historyBean.setLastMsg(revMsg.contentData);
                    historyBean.setConversationId(revMsg.fromId);
                    historyBean.setNewMsgCount(1);
                    MLOC.addHistory(historyBean,true);

                    MessageBean messageBean = new MessageBean();
                    messageBean.setConversationId(revMsg.fromId);
                    messageBean.setTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
                    messageBean.setMsg(revMsg.contentData);
                    messageBean.setFromId(revMsg.fromId);
                    mDatas.add(messageBean);
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // ??????????????????????????????????????????
        cm.setText(mDatas.get(position).getMsg());
        Toast.makeText(this,"???????????????",Toast.LENGTH_LONG).show();
        return false;
    }

    public class MyChatroomListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        public MyChatroomListAdapter(){
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            if(mDatas ==null) return 0;
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            if(mDatas ==null)
                return null;
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            if(mDatas ==null)
                return 0;
            return position;
        }

        @Override
        public int getViewTypeCount(){
            return 2;
        }

        @Override
        public int getItemViewType(int position){
            int id = -1;
            if(mDatas.get(position).getFromId()==null){
                id = 0;
            }else{
                id =mDatas.get(position).getFromId().equals(MLOC.userId)?0:1;
            }
            return id;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            int currLayoutType = getItemViewType(position);
            if(currLayoutType == 0){ //???????????????
                final ViewHolder itemSelfHolder;
                if(convertView == null){
                    itemSelfHolder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.item_chat_msg_list_right,null);
                    itemSelfHolder.vUserId = (TextView) convertView.findViewById(R.id.item_user_id);
                    itemSelfHolder.vMsg = (TextView) convertView.findViewById(R.id.item_msg);
                    itemSelfHolder.vHeadBg = convertView.findViewById(R.id.head_bg);
                    itemSelfHolder.vHeadCover = (CircularCoverView) convertView.findViewById(R.id.head_cover);
                    itemSelfHolder.vHeadImage = (ImageView) convertView.findViewById(R.id.head_img);
                    convertView.setTag(itemSelfHolder);
                }else{
                    itemSelfHolder = (ViewHolder)convertView.getTag();
                }
                itemSelfHolder.vUserId.setText(mDatas.get(position).getFromId());
                itemSelfHolder.vMsg.setText(mDatas.get(position).getMsg());
                itemSelfHolder.vHeadBg.setBackgroundColor(ColorUtils.getColor(C2CActivity.this,mDatas.get(position).getFromId()));
                itemSelfHolder.vHeadCover.setCoverColor(Color.parseColor("#f6f6f6"));
                int cint = DensityUtils.Companion.dip2px(C2CActivity.this,20);
                itemSelfHolder.vHeadCover.setRadians(cint, cint, cint, cint,0);
                itemSelfHolder.vHeadImage.setImageResource(R.drawable.head_icon_1);
            }else if(currLayoutType == 1){//???????????????
                final ViewHolder itemOtherHolder;
                if(convertView == null){
                    itemOtherHolder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.item_chat_msg_list_left,null);
                    itemOtherHolder.vUserId = (TextView) convertView.findViewById(R.id.item_user_id);
                    itemOtherHolder.vMsg = (TextView) convertView.findViewById(R.id.item_msg);
                    itemOtherHolder.vHeadBg = convertView.findViewById(R.id.head_bg);
                    itemOtherHolder.vHeadCover = (CircularCoverView) convertView.findViewById(R.id.head_cover);
                    itemOtherHolder.vHeadImage = (ImageView) convertView.findViewById(R.id.head_img);
                    convertView.setTag(itemOtherHolder);
                }else{
                    itemOtherHolder = (ViewHolder)convertView.getTag();
                }
                itemOtherHolder.vUserId.setText(mDatas.get(position).getFromId());
                itemOtherHolder.vMsg.setText(mDatas.get(position).getMsg());
                itemOtherHolder.vHeadBg.setBackgroundColor(ColorUtils.getColor(C2CActivity.this,mDatas.get(position).getFromId()));
                itemOtherHolder.vHeadCover.setCoverColor(Color.parseColor("#f6f6f6"));
                int cint = DensityUtils.Companion.dip2px(C2CActivity.this,20);
                itemOtherHolder.vHeadCover.setRadians(cint, cint, cint, cint,0);
                itemOtherHolder.vHeadImage.setImageResource(R.drawable.head_icon_2);
            }
            return convertView;
        }
    }

    public class ViewHolder{
        public TextView vUserId;
        public TextView vMsg;
        public View vHeadBg;
        public CircularCoverView vHeadCover;
        public ImageView vHeadImage;
    }



}
