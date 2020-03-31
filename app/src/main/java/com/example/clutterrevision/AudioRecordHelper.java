package com.example.clutterrevision;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;

public class AudioRecordHelper extends LiveData {

    public static final int RECORDER_SAMPLERATE = 8000;
    public static final int SAMPLE_SIZE = 320;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int RECORDER_BUFFERSIZE_BYTES = 640;
    private final short[] blank = new short[640];
    private boolean isRecording = false;

    int max = 0;
    int min = 0;

    Handler handler;
    FileHelper fileHelper;
    List<Integer> values = new ArrayList<>();
    AudioRecord audioRecord;

    public AudioRecordHelper(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
        setValue(blank);
    }

    public AudioRecord initRecorder() {
        AudioRecord ar;
        ar = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING,
                RECORDER_BUFFERSIZE_BYTES
        );
        return ar;
    }

    public void record() {

        if (!isRecording) {
            if (audioRecord == null) {
                audioRecord = initRecorder();
            }
            isRecording = true;
            audioRecord.startRecording();
        }

        // run audio thread
        new Thread(new Runnable() {
            @Override
            public void run() {

                handler = new Handler(Looper.getMainLooper());
                Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);

                short[] audioData = new short[RECORDER_BUFFERSIZE_BYTES];

                while (isRecording) {
                    audioRecord.read(audioData, 0, RECORDER_BUFFERSIZE_BYTES);
                    final short[] s = Arrays.copyOf(audioData, RECORDER_BUFFERSIZE_BYTES);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setValue(s);
                        }
                    });

                    short[] t = Arrays.copyOf(audioData,RECORDER_BUFFERSIZE_BYTES);

                    fileHelper.writeData(convertShortToByte(audioData));
                    // get min and max values from array after reading
                    giveMxMn(t);
                }
                if (audioRecord.getState() == AudioRecord.RECORDSTATE_RECORDING) {
                    audioRecord.stop();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        setValue(blank);
                    }
                });
            }
        }).start();

    }

    public void stop() {
        isRecording = false;
    }


    public void releaseAudioRecord() {
        isRecording = false;
        if(audioRecord != null) {
            audioRecord.release();
            audioRecord = null;
        }
        stop();
    }

    private byte[] convertShortToByte(short[] sData) {
        int shortArraySize = sData.length;
        byte[] bytes = new byte[shortArraySize * 2];
        for (int i = 0; i < shortArraySize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;
    }

    private short[] giveMxMn(short[] array) {
        new Pair(max, min);
        Arrays.sort(array);
        max = array[array.length - 1];
        min = array[0];
        values.add(min);
        values.add(max);
        return array;
    }
}
