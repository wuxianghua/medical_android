package com.palmap.exhibition.iflytek;

import android.content.Context;
import android.text.TextUtils;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王天明 on 2016/9/6.
 */
public class IFlytekController {

    private static IFlytekController instance;
    private String appKey;
    private List<SpeechSynthesizer> speechSynthesizerList;

    private IFlytekController() {
        speechSynthesizerList = new ArrayList<>();
    }

    public static IFlytekController getInstance() {
        if (instance == null) {
            instance = new IFlytekController();
        }
        return instance;
    }

    public void initWithKey(Context context, String appKey) {
        if (TextUtils.isEmpty(appKey)) {
            throw new NullPointerException("appkey is not null!");
        }
        if (null == context) {
            throw new NullPointerException("context is not null!");
        }
        this.appKey = appKey;
        SpeechUtility.createUtility(context, "appid=" + appKey);
    }

    public SpeechSynthesizer obtainSpeechSynthesizer(Context context, InitListener initListener) {
        if (null == context) {
            throw new NullPointerException("context is not null!");
        }
        SpeechSynthesizer speechSynthesizer = SpeechSynthesizer.createSynthesizer(context, initListener);
        speechSynthesizerList.add(speechSynthesizer);
        return speechSynthesizer;
    }

    public void destroyAllSpeechSynthesizer() {
        if (speechSynthesizerList.size() > 0) {
            for (SpeechSynthesizer speechSynthesizer  : speechSynthesizerList) {
                speechSynthesizer.stopSpeaking();
                speechSynthesizer.destroy();
            }
        }
        speechSynthesizerList.clear();
    }

    public void startDefaultSpeaking(Context context,String msg, SynthesizerListener synthesizerListener){
        obtainSpeechSynthesizer(context,null).startSpeaking(msg,synthesizerListener);
    }
}