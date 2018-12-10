package com.hl.knowledge;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.hl.utils.L;

public class MyService extends Service {

    public MyService() {
    }

    /**
     * 调用 startService() 时启用的方法 。通过调用 stopSelf() 或 stopService() 来停止服务
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.e("onStartCommand");
//        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 当另一个组件想通过调用 bindService() 与服务绑定（例如执行 RPC）时，系统将调用此方法。
     * 在此方法的实现中，您必须通过返回 IBinder 提供一个接口，供客户端用来与服务进行通信。
     * 请务必实现此方法，但如果您并不希望允许绑定，则应返回 null。
     */
    @Override
    public IBinder onBind(Intent intent) {
        L.e( "onBind");
        // TODO: Return the communication channel to the service.
        return null;

    }

    /**
     * 首次创建服务时，系统将调用此方法来执行一次性设置程序（在调用 onStartCommand() 或 onBind() 之前）。
     * 如果服务已在运行，则不会调用此方法。
     */
    @Override
    public void onCreate() {
        super.onCreate();
        L.e( "onCreate");
    }

    /**
     * 当服务不再使用且将被销毁时，系统将调用此方法。服务应该实现此方法来清理所有资源，如线程、注册的侦听器、接收器等。 这是服务接收的最后一个调用。
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        L.e( "onDestroy");
    }
}
