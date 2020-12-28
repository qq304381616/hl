package com.hl.systeminfo;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.hl.base.BaseActivity;
import com.hl.base.adapter.BaseRecyclerLine2Adapter;
import com.hl.base.entity.BaseDataEntity;
import com.hl.utils.ConvertUtils;
import com.hl.utils.FileUtils;
import com.hl.utils.SDCardUtils;
import com.hl.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 存储
 */
public class PathActivity extends BaseActivity {

    private static final String PATH = "abs";

    /**
     * 获取公共目录 URI，
     *
     * @param type   Images Audio Video Files Downloads
     * @param volume 指定存储卷。Files 必须指定。
     * @return
     */
    public static Uri getUri(String type, String volume) {
        if (type.equals("Images")) {
            if (volume.equals("internal")) {
                return MediaStore.Images.Media.INTERNAL_CONTENT_URI;
            } else if (volume.equals("external")) {
                return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else {
                return MediaStore.Images.Media.getContentUri(volume);
            }
        } else if (type.equals("Audio")) {
            if (volume.equals("internal")) {
                return MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
            } else if (volume.equals("external")) {
                return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            } else {
                return MediaStore.Audio.Media.getContentUri(volume);
            }
        } else if (type.equals("Video")) {
            if (volume.equals("internal")) {
                return MediaStore.Video.Media.INTERNAL_CONTENT_URI;
            } else if (volume.equals("external")) {
                return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else {
                return MediaStore.Video.Media.getContentUri(volume);
            }
        } else if (type.equals("File")) {
            return MediaStore.Files.getContentUri(volume);
        } else if (type.equals("Downloads")) {
//            if (volume.equals("internal")) {
//                return MediaStore.Downloads.INTERNAL_CONTENT_URI;
//            }else if (volume.equals("external")) {
//                return MediaStore.Downloads.EXTERNAL_CONTENT_URI;
//            }else {
//                return MediaStore.Downloads.getContentUri(volume);
//            }
        }
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_activity_path);
        initToolbar(true);

        RecyclerView rv_path = findViewById(R.id.rv_path);

        rv_path.setLayoutManager(new LinearLayoutManager(this));
        rv_path.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        BaseRecyclerLine2Adapter adapter = new BaseRecyclerLine2Adapter(this);
        rv_path.setAdapter(adapter);

        List<BaseDataEntity> list = new ArrayList<>();
        String sdCardInfo = SDCardUtils.getSDCardInfo();
        if (!TextUtils.isEmpty(sdCardInfo)) {
            try {
                JSONObject object = new JSONObject(sdCardInfo);
                list.add(new BaseDataEntity("总空间", ConvertUtils.byte2FitMemorySize(object.getLong("TotalBytes"))));
                list.add(new BaseDataEntity("可用剩余空间", ConvertUtils.byte2FitMemorySize(object.getLong("AvailableBytes"))));
                list.add(new BaseDataEntity("剩余总空间", ConvertUtils.byte2FitMemorySize(object.getLong("FreeBytes"))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        list.add(new BaseDataEntity("以下文件夹清除缓存时被清理", ""));
        list.add(new BaseDataEntity("getCacheDir", getCacheDir().getAbsolutePath()));
        File[] externalCacheDirs = getExternalCacheDirs();
        for (File externalCacheDir : externalCacheDirs) {
            list.add(new BaseDataEntity("getExternalCacheDirs", externalCacheDir.getAbsolutePath()));
        }
        File[] externalFilesDirs = getExternalFilesDirs(PATH);
        for (File externalFilesDir : externalFilesDirs) {
            list.add(new BaseDataEntity("getExternalFilesDirs", externalFilesDir.getAbsolutePath()));
        }
        list.add(new BaseDataEntity("getFilesDir", getFilesDir().getAbsolutePath()));
        list.add(new BaseDataEntity("getDir", getDir(PATH, Context.MODE_PRIVATE).getAbsolutePath()));
        list.add(new BaseDataEntity("以下文件夹程序卸载清除数据存时被清理", ""));
        list.add(new BaseDataEntity("getExternalCacheDir", getExternalCacheDir() == null ? "" : getExternalCacheDir().getAbsolutePath()));
        File externalFilesDir = getExternalFilesDir(PATH);
        list.add(new BaseDataEntity("getExternalFilesDir", externalFilesDir == null ? "" : externalFilesDir.getAbsolutePath()));
        list.add(new BaseDataEntity("其他", ""));
        list.add(new BaseDataEntity("getObbDir", getObbDir().getAbsolutePath()));
        File[] obbDirs = getObbDirs();
        for (File obbDir : obbDirs) {
            list.add(new BaseDataEntity("getObbDirs", obbDir.getAbsolutePath()));
        }
        list.add(new BaseDataEntity("getCodeCacheDir", getCodeCacheDir().getAbsolutePath()));

        // android L 新增加的私有目录
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            list.add(new BaseDataEntity("21 L 以上支持。", ""));
            File[] externalMediaDirs = getExternalMediaDirs();
            for (File externalMediaDir : externalMediaDirs) {
                list.add(new BaseDataEntity("getExternalMediaDirs", externalMediaDir.getAbsolutePath()));
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.add(new BaseDataEntity("24 N 以上支持。", ""));
            list.add(new BaseDataEntity("getDataDir", getDataDir().getAbsolutePath()));
        }

        list.add(new BaseDataEntity("公共目录 29 Q 以下使用", ""));
        list.add(new BaseDataEntity("Environment.getExternalStorageDirectory", Environment.getExternalStorageDirectory().getAbsolutePath()));
        list.add(new BaseDataEntity("Environment.getExternalStoragePublicDirectory",
                Environment.getExternalStoragePublicDirectory("").getAbsolutePath()));
        list.add(new BaseDataEntity("Environment.getExternalStoragePublicDirectory",
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath()));
        list.add(new BaseDataEntity("Environment.getExternalStoragePublicDirectory",
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath()));
        list.add(new BaseDataEntity("Environment.getExternalStoragePublicDirectory",
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()));
        list.add(new BaseDataEntity("Environment.getExternalStoragePublicDirectory",
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()));

        list.add(new BaseDataEntity("公共目录 29 Q 以上访问", ""));
        getVolume(this);

//        11 分区存储
//        公开目录
//
//        媒体文件
//        Audio  Video  Image
//        获取本应用创建文件。不需要申请权限。
//        申请“READ_EXTERNAL_STORAGE”权限后，可访问所有应用的数据
//
//        文件
//        File  Downloads
//        Download: 代码只能访问本应用文件。卸载后文件包名关联取消。所以重新安装也不能访问
//        File: 能访问所有媒体文件。
//        可以使用SAF选择所有应用文件。

        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取存储盘 名称
     * getExternalVolumeNames() 是 Q 及以上版本调用。
     * <p>
     * 样式: content://media/external_primary/images/media
     * 固定写法: MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
     */
    @TargetApi(29)
    public String getVolume(Context context) {
        // 显示公共所有存储设备， 及对应URI
//        for (String volumeName : MediaStore.getExternalVolumeNames(DemoActivity.this)) {
//            System.out.println(volumeName);
//            Uri imageUri = MediaStore.Images.Media.getContentUri(volumeName);
//            System.out.println(imageUri);
//            Uri fileUri = MediaStore.Files.getContentUri(volumeName);
//            System.out.println(fileUri);
//        }
        return "";
    }

    /**
     * 版本 10 及以上
     * 获取公共目录下相册文件
     */
    @TargetApi(29)
    private void getCollection() {
        String volume = getVolume(this);
        Uri contentUri = MediaStore.Images.Media.getContentUri(volume);

        ContentResolver resolver = this.getContentResolver();

        // 查询条件
//                String selection = MediaStore.Images.Media.TITLE + "=?";
//                String[] args = new String[] {"Image"};
        String selection = null;
        String[] args = new String[]{};

//        String[] projection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.RELATIVE_PATH};
//        Cursor cursor = resolver.query(contentUri, projection, selection, args, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            int idIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
//            int displayNameIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
//            int relativePathIndex = cursor.getColumnIndex(MediaStore.Images.Media.RELATIVE_PATH);
//            do {
//                int id = cursor.getInt(idIndex);
//                String displayName = cursor.getString(displayNameIndex);
//                String relativePath = cursor.getString(relativePathIndex);
//                Uri imageUri = ContentUris.withAppendedId(contentUri, cursor.getLong(0));
//                System.out.println(id + " | " + displayName + " | " + relativePath);
//                System.out.println(imageUri);
//            } while (cursor.moveToNext());
//            cursor.close();
//        }
    }

    /**
     * 版本 10 及以上
     * 插入图片到系统相册
     *
     * @param disPlayName 图片名称
     */
    @TargetApi(29)
    private void insertImageToCollection(Context context, String disPlayName) throws IOException {
        FileUtils.checkExistsAndCopy(context, new File(context.getCacheDir(), disPlayName)); // 先将 assets 文件复制到 存储设备上

        ContentValues contentValues = new ContentValues();
//        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, disPlayName);
//        contentValues.put(MediaStore.Images.Media.IS_PENDING, 1); // 这个字段 新版本才有
//        contentValues.put(MediaStore.Images.Media.DESCRIPTION, disPlayName);
//        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
//        contentValues.put(MediaStore.Images.Media.TITLE, disPlayName);
//        // 可以指定二级目录。默认 /Pictures
//        // DCIM/Camera/ Or /Pictures
//        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Camera");

        String volume = getVolume(this);
        Uri imageCollection = MediaStore.Images.Media.getContentUri(volume);

        ContentResolver contentResolver = context.getContentResolver();
        Uri imageUri = contentResolver.insert(imageCollection, contentValues);

        if (imageUri == null) return;

        File f = new File(context.getCacheDir(), disPlayName);
        InputStream is = new FileInputStream(f);
        OutputStream os = contentResolver.openOutputStream(imageUri, "w");
        FileUtils.copyFile(is, os);

        contentValues.clear();

//        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0);
        contentResolver.update(imageUri, contentValues, null, null);
    }

    /**
     * 低版本 10以下，放到公共文件后的广播更新 事件。
     */
    private void insertFile(File f) {
        File publicFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/xty/" + f.getName());
        FileUtils.copyFile(f.getAbsolutePath(), publicFile.getAbsolutePath());
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(publicFile)));
        ToastUtils.showShortToast(getApplicationContext(), "文件已保存到：" + publicFile.getAbsolutePath());
    }
}
