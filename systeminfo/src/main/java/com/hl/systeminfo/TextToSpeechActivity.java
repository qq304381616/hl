package com.hl.systeminfo;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hl.base.BaseActivity;

import java.util.HashMap;
import java.util.Locale;

/**
 * 显示所有已安装应用列表
 */
public class TextToSpeechActivity extends BaseActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private EditText et_text;
    private Button btn_play;
    private Button btn_d1;
    private TextView tv_d;
    private Button btn_d2;
    private Button btn_s1;
    private TextView tv_s;
    private Button btn_s2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);

        tts = new TextToSpeech(this, this);

        et_text = (EditText) findViewById(R.id.et_text);
        btn_play = (Button) findViewById(R.id.btn_play);
        btn_d1 = (Button) findViewById(R.id.btn_d1);
        tv_d = (TextView) findViewById(R.id.tv_d);
        btn_d2 = (Button) findViewById(R.id.btn_d2);
        btn_s1 = (Button) findViewById(R.id.btn_s1);
        tv_s = (TextView) findViewById(R.id.tv_s);
        btn_s2 = (Button) findViewById(R.id.btn_s2);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = et_text.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    play(text, tv_d.getText().toString(), tv_s.getText().toString());
                }
            }
        });
        btn_d1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float f = jian(tv_d.getText().toString());
                if (f > 1 || f < 0) {
                    return;
                }
                tv_d.setText(String.valueOf(f));
            }
        });
        btn_d2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float f = jia(tv_d.getText().toString());
                if (f > 1 || f < 0) {
                    return;
                }
                tv_d.setText(String.valueOf(f));
            }
        });
        btn_s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float f = jian(tv_s.getText().toString());
                tv_s.setText(String.valueOf(f));
            }
        });
        btn_s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float f = jia(tv_s.getText().toString());
                tv_s.setText(String.valueOf(f));
            }
        });
    }

    public void play(String text, String type, String rate) {
        tts.setPitch(Float.valueOf(type));
        tts.setSpeechRate(Float.valueOf(rate));    // 设置语速
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
            }

            @Override
            public void onDone(String s) {
            }

            @Override
            public void onError(String s) {
            }
        });
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, text);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);  //播放语音
    }


    @Override
    public void onInit(int status) {
        //判断tts回调是否成功
        if (status == TextToSpeech.SUCCESS) {
            int result1 = tts.setLanguage(Locale.US);
            int result2 = tts.setLanguage(Locale.CHINESE);
            if (result1 == TextToSpeech.LANG_MISSING_DATA || result1 == TextToSpeech.LANG_NOT_SUPPORTED ||
                    result2 == TextToSpeech.LANG_MISSING_DATA || result2 == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "不支持", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private float jian(String text) {
        float f = Float.valueOf(text);
        return ((int) (f * 10) - 1) / 10.0f;
    }

    private float jia(String text) {
        float f = Float.valueOf(text);
        return ((int) (f * 10) + 1) / 10.0f;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
