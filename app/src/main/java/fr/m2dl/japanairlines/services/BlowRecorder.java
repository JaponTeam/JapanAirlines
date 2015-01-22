package fr.m2dl.japanairlines.services;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by msoum on 22/01/15.
 */
public class BlowRecorder {
    private MediaRecorder mRecorder = new MediaRecorder();
    private boolean isStarted = false;

    private void startRecording() {
        if(!isStarted) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");

            try {
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e("###", " ### prepare() failed");
            }

            mRecorder.start();
            isStarted = true;
        }

    }

    public void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    public void recordBlow() {
        Log.d("", "Recording !");

        startRecording();

        boolean recording = true;
        while (recording) {
            if (mRecorder.getMaxAmplitude() > 27000) {
                Log.d("", "## Blowing value : " + mRecorder.getMaxAmplitude());
                stopRecording();
                recording = false;
            }
        }

        Log.d("", "## Blowing done !");
    }
}
