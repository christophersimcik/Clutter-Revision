package com.example.clutterrevision;

import android.app.Activity;
import android.os.Environment;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import androidx.lifecycle.LiveData;


public class FileHelper extends LiveData {

    final static String TEMP_FILE_NAME = "temporary_audio_container";

    File audioFile = null;
    File temporaryFile = null;
    FileOutputStream tempOutputStream;

    public FileHelper(){

    }

    public void close() throws IOException {
        tempOutputStream.close();

    }

    public void writeWav() throws IOException {
        FileOutputStream outputStream = new FileOutputStream(audioFile.getAbsolutePath());
        writeString(outputStream, "RIFF"); // chunk id
        writeInt(outputStream, 36 + (int) temporaryFile.length()); // chunk size
        writeString(outputStream, "WAVE"); // format
        writeString(outputStream, "fmt "); // subchunk 1 id
        writeInt(outputStream, 16); // subchunk 1 size
        writeShort(outputStream, (short) 1); // audio format (1 = PCM)
        writeShort(outputStream, (short) 1); // number of channels
        writeInt(outputStream, 8000); // sample rate
        writeInt(outputStream,  (8000 * 2)); // byte rate
        writeShort(outputStream, (short) 2); // block align
        writeShort(outputStream, (short) 16); // bits per sample
        writeString(outputStream, "data"); // subchunk 2 id
        writeInt(outputStream, (int) temporaryFile.length()); // subchunk 2 size
        outputStream.write(retrieveAudio(temporaryFile));
        outputStream.flush();
        outputStream.close();
    }

    public void writeData(byte[] b){
        try {
            tempOutputStream.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createFile(Activity a){
        String name = timeStamp() + "_";
        String suffix = ".wav";
        String path = a.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath();
        File file = new File(path,name+suffix);
        return file;
    }

    private File createTemporaryFile(Activity a) {
        File tempAudio = a.getExternalFilesDir(null);
        File audioTemp = null;
        try {
            audioTemp = File.createTempFile(
                    TEMP_FILE_NAME,".pcm",tempAudio
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            tempOutputStream = new FileOutputStream(audioTemp.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return audioTemp;
    }

    private String timeStamp(){
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyMMddhhmmss");
        return date.toString(fmt);
    }

    public void makeTempFile(Activity a){
     temporaryFile = createTemporaryFile(a);
    }

    public void makeFile(Activity a){
        audioFile = createFile(a);
        setValue(audioFile);
    }

    private byte[] retrieveAudio(File f) throws IOException {
        byte bytes[] = new byte[(int)temporaryFile.length()];
        FileInputStream fileInputStream= new FileInputStream(f);
        fileInputStream.read(bytes);
        ByteBuffer  bb = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).put(bytes);
        return bb.array();
    }

    private void writeInt(final FileOutputStream output, final int value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }

    private void writeShort(final FileOutputStream  output, final short value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
    }

    private void writeString(final FileOutputStream  output, final String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
            output.write(value.charAt(i));
        }
    }




}
