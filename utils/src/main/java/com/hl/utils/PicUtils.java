package com.hl.utils;

import android.app.Activity;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/7/13.
 */
public class PicUtils {

    private List<LocalMedia> selectList = new ArrayList<>();

    private boolean mode = true; // 相册or单独拍照
    private int chooseMode = PictureMimeType.ofImage(); // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
    private int themeId = R.style.picture_default_style;
    private int maxSelectNum = 9;
    private boolean sOrm = true;
    private boolean previewImg = true;
    private boolean previewVideo = true;
    private boolean previewAudio = true;
    private boolean isCamera = true;
    private boolean crop = false;
    private boolean compress = false;
    private int compressMode = PictureConfig.SYSTEM_COMPRESS_MODE;
    private int aspect_ratio_x, aspect_ratio_y;
    private boolean hide = false;
    private boolean isGif = false;
    private boolean styleCrop = false;
    private boolean cropCircular = false;
    private boolean showCropFrame = true;
    private boolean showCropGrid = true;
    private boolean voice = false;

    public void startSelect(Activity activity, String count, String cropMode) {
        int counta = Integer.valueOf(count);
        if (counta > 0 && counta <= 9) {
            maxSelectNum = counta;
            if (counta == 1) {
                sOrm = false;
            }
        }
        if (1 == Integer.valueOf(cropMode)) {
            crop = true;
            aspect_ratio_x = 1;
            aspect_ratio_y = 1;
        }
        if (mode) {
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(activity)
                    .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(sOrm ? PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选
                    .previewImage(previewImg)// 是否可预览图片
                    .previewVideo(previewVideo)// 是否可预览视频
                    .enablePreviewAudio(previewAudio) // 是否可播放音频
                    .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                    .isCamera(isCamera)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                    .enableCrop(crop)// 是否裁剪
                    .compress(compress)// 是否压缩
                    .compressMode(compressMode)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(hide ? false : true)// 是否显示uCrop工具栏，默认不显示
                    .isGif(isGif)// 是否显示gif图片
                    .freeStyleCropEnabled(styleCrop)// 裁剪框是否可拖拽
                    .circleDimmedLayer(cropCircular)// 是否圆形裁剪
                    .showCropFrame(showCropFrame)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(showCropGrid)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(voice)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                    //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                    //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled() // 裁剪是否可旋转图片
                    //.scaleEnabled()// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()//显示多少秒以内的视频or音频也可适用
                    //.recordVideoSecond()//录制视频秒数 默认60s
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        } else {
            // 单独拍照
            PictureSelector.create(activity)
                    .openCamera(chooseMode)// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                    .theme(themeId)// 主题样式设置 具体参考 values/styles
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .selectionMode(sOrm ?
                            PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选
                    .previewImage(previewImg)// 是否可预览图片
                    .previewVideo(previewVideo)// 是否可预览视频
                    .enablePreviewAudio(previewAudio) // 是否可播放音频
                    .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                    .isCamera(isCamera)// 是否显示拍照按钮
                    .enableCrop(crop)// 是否裁剪
                    .compress(compress)// 是否压缩
                    .compressMode(compressMode)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(hide ? false : true)// 是否显示uCrop工具栏，默认不显示
                    .isGif(isGif)// 是否显示gif图片
                    .freeStyleCropEnabled(styleCrop)// 裁剪框是否可拖拽
                    .circleDimmedLayer(cropCircular)// 是否圆形裁剪
                    .showCropFrame(showCropFrame)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(showCropGrid)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(voice)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
                    .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.cropCompressQuality(90)// 裁剪压缩质量 默认为100
                    //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                    //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled() // 裁剪是否可旋转图片
                    //.scaleEnabled()// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()////显示多少秒以内的视频or音频也可适用
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }
    }
}
