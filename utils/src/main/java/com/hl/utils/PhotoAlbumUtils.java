package com.hl.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.media.ExifInterface;

import java.io.IOException;

/**
 * 从相册内获取照片转化工具类
 */
public class PhotoAlbumUtils {

    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) {
            return getRealPathFromUriAboveApi19(context, uri);
        } else {
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) {
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * 获取图片信息 大小/旋转角度等。
     */
    public static String getPhotoInfo(String path) throws IOException {
        ExifInterface exifInterface = new ExifInterface(path);

        String orientation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
        String dateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
        String make = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
        String model = exifInterface.getAttribute(ExifInterface.TAG_MODEL);
        String flash = exifInterface.getAttribute(ExifInterface.TAG_FLASH);
        String imageLength = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
        String imageWidth = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
        String latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        String longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        String latitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
        String longitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
        String exposureTime = exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
        String aperture = exifInterface.getAttribute(ExifInterface.TAG_APERTURE_VALUE);
        String isoSpeedRatings = exifInterface.getAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS);
        String dateTimeDigitized = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED);
        String subSecTime = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME);
        String subSecTimeOrig = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME_ORIGINAL);
        String subSecTimeDig = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME_DIGITIZED);
        String altitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE);
        String altitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF);
        String gpsTimeStamp = exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
        String gpsDateStamp = exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
        String whiteBalance = exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
        String focalLength = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
        String processingMethod = exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);

        String info = "";
        info += "## orientation=" + orientation;
        info += "## dateTime=" + dateTime;
        info += "## make=" + make;
        info += "## model=" + model;
        info += "## flash=" + flash;
        info += "## imageLength=" + imageLength;
        info += "## imageWidth=" + imageWidth;
        info += "## latitude=" + latitude;
        info += "## longitude=" + longitude;
        info += "## latitudeRef=" + latitudeRef;
        info += "## longitudeRef=" + longitudeRef;
        info += "## exposureTime=" + exposureTime;
        info += "## aperture=" + aperture;
        info += "## isoSpeedRatings=" + isoSpeedRatings;
        info += "## dateTimeDigitized=" + dateTimeDigitized;
        info += "## subSecTime=" + subSecTime;
        info += "## subSecTimeOrig=" + subSecTimeOrig;
        info += "## subSecTimeDig=" + subSecTimeDig;
        info += "## altitude=" + altitude;
        info += "## altitudeRef=" + altitudeRef;
        info += "## gpsTimeStamp=" + gpsTimeStamp;
        info += "## gpsDateStamp=" + gpsDateStamp;
        info += "## whiteBalance=" + whiteBalance;
        info += "## focalLength=" + focalLength;
        info += "## processingMethod=" + processingMethod;
        return info;
    }
}
