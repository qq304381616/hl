package com.hl.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.TextUtils;
import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BitmapUtils {

    private static final String TAG = BitmapUtils.class.getSimpleName();

    public static String getBase64FromBitmap(Bitmap bitmap, ByteArrayOutputStream bos) {
        bitmap.compress(CompressFormat.JPEG, 100, bos);
        return Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
    }

    public static String getBase64FromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, bos);
        return Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
    }

    public static final int RAW_SIZE = 128;
    private static int sWidth = 128;
    private static int sHeight = 128;

    public static void reset() {
        sWidth = RAW_SIZE;
        sHeight = RAW_SIZE;
    }

    public static void setSize(int width, int height) {
        int size = width > height ? width : height;
        sWidth = size;
        sHeight = size;
    }

    public static Bitmap getBitmap(String path) {
        if (TextUtils.isEmpty(path))
            return null;

        byte[] buffer = FileUtils.getBuffer(path);

        return getBitmap(buffer);
    }

    public static Bitmap getBitmap(byte[] coverByte) {
        try {
            if (coverByte == null)
                return null;

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(coverByte, 0, coverByte.length, opts);
            opts.inSampleSize = BitmapUtils.computeSampleSize(opts, -1, sWidth * sHeight);

            opts.inJustDecodeBounds = false;
            return BitmapFactory.decodeByteArray(coverByte, 0, coverByte.length, opts);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;

        }
    }

    public static Bitmap getBitmap(InputStream is) {
        try {
            if (is == null)
                return null;

            int size = is.available();
            byte[] bs = new byte[size];
            is.read(bs, 0, bs.length);

            return getBitmap(bs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * 将Bitmap转换成String
     */
    public static String convertFileToString(File file) {
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(data);
    }

    /**
     * bitmap 转 base64 转 图片路径
     */
    public static void bitmap2Base642Path(Bitmap bitmap, String path) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, baos);
        String s = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        OutputStream os = new FileOutputStream(new File(path));
        os.write(s.getBytes());
        os.close();
    }

    /**
     * 图片路径 转 base64 转 bitmap
     */
    public static Bitmap base64Path2Bitmap(String path) throws IOException {
        FileInputStream fis = new FileInputStream(new File(path));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        byte[] decode = Base64.decode(buffer, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decode, 0, decode.length);
    }

    /**
     * String 换 Bitmap
     */
    public static Bitmap stringToBitmap(String string) {
        if (StringUtils.isEmpty(string)) return null;
        byte[] bitmapArray = Base64.decode(string, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
    }

    /**
     * Bitmap 转 base64
     */
    public static String bitmap2Base64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    /**
     * 图片路径 转 base64
     */
    public static String path2Bitmap(String path) {
        Bitmap b = BitmapFactory.decodeFile(path);
        if (b == null) return "";
        return bitmap2Base64(b);
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Bitmap scaleBitmap(Bitmap bitMap) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 设置想要的大小
        int newWidth = 320;
        int newHeight = 240;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postRotate(-90);
        return bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
    }

    public Bitmap revitionImageSize(InputStream temp, int size) throws IOException {
        // 取得图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化
        options.inJustDecodeBounds = true;
        // 通过创建图片的方式，取得options的内容（这里就是利用了java的地址传递来赋值）
        BitmapFactory.decodeStream(temp, null, options);
        // 关闭流
        temp.close();
        // 生成压缩的图片
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            // 这一步是根据要设置的大小，使宽和高都能满足
            if ((options.outWidth >> i <= size) && (options.outHeight >> i <= size)) {
                // 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
                // temp = this.getAssets().open(path);
                // 这个参数表示 新生成的图片为原始图片的几分之一。
                options.inSampleSize = (int) Math.pow(2.0D, i);
                // 这里之前设置为了true，所以要改为false，否则就创建不出图片
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(temp, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    /**
     * 压缩图片 根据质量大小
     */
    public static Bitmap compressImage(Bitmap image) {
        if (image == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        // 为了防止内存溢出的问题
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = false;
        opt.inSampleSize = 2;// 图片宽高都为原来的二分之一，即图片为原来的四分之一
        return BitmapFactory.decodeStream(isBm, null, opt);// 把ByteArrayInputStream数据生成图片
    }

    /**
     * 压缩图片 根据图片尺寸
     */
    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;// 只读边,不读内容
        BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 400f;
        float ww = 400f;
        int be = 1;
        if (w >= h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w <= h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;// 设置采样率
        Bitmap decodeFile = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(decodeFile);
    }

    /**
     * 默认以jpg格式保存图片到指定路径
     *
     * @param b    图片
     * @param path 保存路径
     * @return 是否保存成功
     */
    public static boolean saveBitmapToFile(Bitmap b, String path) throws IOException {
        if (b == null || StringUtils.isEmpty(path)) return false;

        File f = new File(path);
        if (f.exists()) if (!f.delete()) return false; // 如果文件存在 则删除
        if (!f.getParentFile().exists())
            if (!f.getParentFile().mkdirs()) return false; // 如果文件所在目录不存在，则创建。

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
        b.compress(CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        return true;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param b      需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap b, int degree) {
        if (degree == 0 || null == b) return b;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap returnBm = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
        b.recycle();
        return returnBm;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 根据URI获取文件的绝对路径
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * Bitmap 图 转 byte 数组
     *
     * @param b bitmap
     * @return 转换后的数组
     */
    public static byte[] bitmapToByteArray(Bitmap b) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }
}
