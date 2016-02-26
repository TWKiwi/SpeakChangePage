package com.example.kiwi.speakchangepage;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecognitionListener {

    int mPosition = 1;   //當前頁數
    boolean mIsListening;   //當前是否開啟麥克風

    ArrayList<Integer> mViewGroup = new ArrayList<>();

    View mView;
    LayoutInflater mFactory;
    SpeechRecognizer mRecognizer;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("程式狀態", "開始執行");

        initView();

        addPage(R.layout.first_view);
        addPage(R.layout.second_view);
        addPage(R.layout.third_view);

        mFactory = LayoutInflater.from(this);
        mView = mFactory.inflate(mViewGroup.get(mPosition), null);
        setContentView(mView);


    }

    public void initView() {

        if(mRecognizer == null) {
            mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);    //創建一個收音器
            intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
            intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        }

        if (!mIsListening)
        {
            mIsListening = true;
            mRecognizer.setRecognitionListener(this);
            mRecognizer.startListening(intent);
        }
    }

    /**
     * 加入頁面
     * @param page
     */
    public void addPage(int page) {
        mViewGroup.add(page);
    }

    /**
     * 取得切頁指令後進行動作
     * @param echo 切頁指令
     */
    public void changePage(String echo) {
        Log.d("程式狀態", "進入changePage 指令為：" + echo);
        try {
            switch (echo) {
                case "上一頁":
                    mView = mFactory.inflate(mViewGroup.get(--mPosition), null);
                    setContentView(mView);
                    break;
                case "last page":
                    mView = mFactory.inflate(mViewGroup.get(--mPosition), null);
                    setContentView(mView);
                    break;
                case "下一頁":
                    mView = mFactory.inflate(mViewGroup.get(++mPosition), null);
                    setContentView(mView);
                    break;
                case "next page":
                    mView = mFactory.inflate(mViewGroup.get(++mPosition), null);
                    setContentView(mView);
                    break;

                default:
                    Toast.makeText(this, echo + "？ 請再說一次", Toast.LENGTH_SHORT).show();

            }
        }catch (IndexOutOfBoundsException e){   //假如超出頁數則自動返回第一頁
            mPosition = 0;
            mView = mFactory.inflate(mViewGroup.get(mPosition), null);
            setContentView(mView);
        }
    }


    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {
        Log.d("發生錯誤", "錯誤代碼：" + error);
        if ((error == SpeechRecognizer.ERROR_NO_MATCH)
                || (error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT)){
            mIsListening = false;
            mRecognizer.stopListening();
            mRecognizer.cancel();
            mRecognizer.destroy();
            initView();
        }
    }

    @Override
    public void onResults(Bundle results) {
        Log.d("程式狀態", "回傳語音");
        String echo = "";
        ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        echo += data.get(0);

        changePage(echo);        // 回傳指令
        mIsListening = false;
        initView();
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        mIsListening = false;
    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}
