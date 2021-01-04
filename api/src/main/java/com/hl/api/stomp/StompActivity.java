package com.hl.api.stomp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.hl.api.R;
import com.hl.base.BaseActivity;
import com.hl.base.adapter.BaseRecyclerAdapter;
import com.hl.base.entity.BaseDataEntity;
import com.hl.utils.L;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

/**
 * 使用库 com.github.NaikSoftware:StompProtocolAndroid 连接 WebSocket
 * 支持连接、断开、订阅消息、发送消息。
 */
public class StompActivity extends BaseActivity {

    private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    boolean connecting = false;
    private String channel = "286";
    private String userId = "201359445";
    private String text = "{\"data\":\"{\\\"type\\\":[\\\"stat\\\"],\\\"stat\\\":{}}\",\"from\":\"201359445\",\"id\":102,\"type\":\"roomMsg\"}";
    private BaseRecyclerAdapter mAdapter;
    private List<BaseDataEntity> mDataSet = new ArrayList<>();
    private StompClient mStompClient;
    private Disposable mRestPingDisposable;
    private RecyclerView mRecyclerView;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_activity_stomp);

        mRecyclerView = findViewById(R.id.recycler_view);
        mAdapter = new BaseRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(mDataSet);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        Map<String, String> map = new HashMap<>();
        map.put("username", "yuhailong1");
        map.put("password", "111111");
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "https://live.xiaotunyun.com/liveclassroomTX/ws_app", map);

        resetSubscriptions();
    }

    public void disconnectStomp(View view) {
        mStompClient.disconnect();
    }

    public synchronized void connectStomp(View view) {
        if (connecting) return;
        if (mStompClient.isConnected()) return;
        connecting = true;

        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);

        resetSubscriptions();

        Disposable dispLifecycle = mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LifecycleEvent>() {
                    @Override
                    public void accept(LifecycleEvent lifecycleEvent) {
                        switch (lifecycleEvent.getType()) {
                            case OPENED:
                                toast("Stomp connection opened");
                                break;
                            case ERROR:
                                L.e("Stomp connection error", lifecycleEvent.getException());
                                toast("Stomp connection error");
                                break;
                            case CLOSED:
                                toast("Stomp connection closed");
                                resetSubscriptions();
                                break;
                            case FAILED_SERVER_HEARTBEAT:
                                toast("Stomp failed server heartbeat");
                                break;
                        }
                        connecting = false;
                    }
                });

        compositeDisposable.add(dispLifecycle);

        // 订阅出现概率收不到消息。可能跟订阅顺序有关
        topic("/user/" + userId + "/channel/" + channel);
        topic("/channel/" + channel);

        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader("usertype", "rtc"));
        headers.add(new StompHeader("userid", "201359445"));
        mStompClient.connect(headers);
    }

    /**
     * 发布订阅
     *
     * @param path 订阅地址
     */
    private void topic(String path) {
        Disposable disposable = mStompClient.topic(path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StompMessage>() {
                    @Override
                    public void accept(StompMessage stompMessage) {
                        addItem(String.format("Stomp received msg: %s, from path %s", stompMessage.getPayload(), stompMessage.findHeader("destination")));
                        L.e(String.format("Stomp received msg: %s, from path %s", stompMessage.getPayload(), stompMessage.findHeader("destination")));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        L.e("连接错误", throwable);
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void sendEchoViaStomp(View v) {
        if (!mStompClient.isConnected()) return;
        compositeDisposable.add(mStompClient.send("/app/" + channel + "/msg", text)
                .compose(applySchedulers())
                .subscribe(new Action() {
                               @Override
                               public void run() {
                                   L.e("STOMP echo send successfully");
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) {
                                   L.e("Error send STOMP echo", throwable);
                                   toast(throwable.getMessage());
                               }
                           }
                ));
    }

    public void sendEchoViaRest(View v) {
        mRestPingDisposable = RestClient.getInstance().getExampleRepository()
                .sendRestEcho(text)
                .compose(applySchedulers())
                .subscribe(new Action() {
                    @Override
                    public void run() {
                        L.e("REST echo send successfully");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        L.e("Error send REST echo", throwable);
                        StompActivity.this.toast(throwable.getMessage());
                    }
                });
    }

    private void addItem(String msg) {
        mDataSet.add(new BaseDataEntity(1, msg + " - " + mTimeFormat.format(new Date())));
        mAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(mDataSet.size() - 1);
    }

    private void toast(String text) {
        L.e(text);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected CompletableTransformer applySchedulers() {
        return new CompletableTransformer() {
            @Override
            public CompletableSource apply(Completable upstream) {
                return upstream
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onDestroy() {
        mStompClient.disconnect();

        if (mRestPingDisposable != null) mRestPingDisposable.dispose();
        if (compositeDisposable != null) compositeDisposable.dispose();
        super.onDestroy();
    }
}
