package com.hl;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.hl.base.BaseActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信
 */
public class MessageActivity extends BaseActivity {

    private TextView tv_send;
    private TextView tv_message;
    private MessageContentObserver messageContentObserver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messageContentObserver = new MessageContentObserver(MessageActivity.this, handler);
        getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, messageContentObserver);

        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_message = (TextView) findViewById(R.id.tv_message);

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(messageContentObserver);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

            } else if (msg.what == 2) {
                tv_message.setText(tv_message.getText().toString() + msg.obj + "\n");
            }
        }
    };

    class MessageContentObserver extends ContentObserver {

        private Context mContext;
        private Handler mHandler;
        private String code;

        public MessageContentObserver(Context context, Handler handler) {
            super(handler);
            mContext = context;
            mHandler = handler;
        }

        /**
         * 回调函数, 当监听的Uri发生改变时，会回调该方法
         * 需要注意的是当收到短信的时候会回调两次
         * 收到短信一般来说都是执行了两次onchange方法.第一次一般都是raw的这个.
         * 虽然收到了短信.但是短信并没有写入到收件箱里
         * <p>
         * _id | 短消息序号 如100 thread_id | 对话的序号 如100
         * address | 发件人地址，手机号.如+8613811810000 person　| 发件人，返回一个数字就是联系人列表里的序号，陌生人为null
         * date | 日期 long型。如1256539465022 protocol | 协议 0 SMS_RPOTO, 1 MMS_PROTO
         * read | 是否阅读 0未读， 1已读
         * status | 状态 -1接收，0 complete, 64 pending, 128 failed
         * type | 类型 1是接收到的，2是已发出
         * body | 短消息内容
         * service_center | 短信服务中心号码编号。
         */
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            if (uri.toString().equals("content://sms/raw")) {
                return;
            }
            //  content://sms/inbox 收件箱
            //  content://sms/sent 已发送
            //  content://sms/draft 草稿
            //  content://sms/outbox 发件箱 (正在发送的信息)
            //  content://sms/failed 发送失败
            //  content://sms/queued 待发送列表
            Uri inboxUri = Uri.parse("content://sms/inbox");
            Cursor c = mContext.getContentResolver().query(inboxUri, null, null, null, "date desc");  // 按时间顺序排序短信数据库
            if (c != null) {
                if (c.moveToFirst()) {

                    //   _id | 短消息序号 如100 thread_id | 对话的序号 如100
                    //   address | 发件人地址，手机号.如+8613811810000 person　| 发件人，返回一个数字就是联系人列表里的序号，陌生人为null
                    //   date | 日期 long型。如1256539465022 protocol | 协议 0 SMS_RPOTO, 1 MMS_PROTO
                    //   read | 是否阅读 0未读， 1已读
                    //   status | 状态 -1接收，0 complete, 64 pending, 128 failed
                    //   type | 类型 1是接收到的，2是已发出
                    //   body | 短消息内容
                    //   service_center | 短信服务中心号码编号。
                    String address = c.getString(c.getColumnIndex("address"));//发送方号码
                    String body = c.getString(c.getColumnIndex("body")); // 短信内容

                    Message msg = Message.obtain();
                    msg.what = 2;
                    msg.obj = address + "\n" + body;
                    mHandler.sendMessage(msg);

                    if (address.equals("10086")) {
                        Pattern pattern = Pattern.compile("(\\d{6})");//正则表达式匹配验证码
                        Matcher matcher = pattern.matcher(body);
                        if (matcher.find()) {
                            code = matcher.group(0);
                            msg = Message.obtain();
                            msg.what = 1;
                            msg.obj = code;
                            mHandler.sendMessage(msg);
                        }
                    }
                }
                c.close();
            }
        }
    }
}
