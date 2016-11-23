package orange.com.audiorecord;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

public class LongClick extends AppCompatActivity {
    //语音保存的路径
    private String fileName = null;
    //语音操作对象，MediaPlayer为播放器，MediaRecorder为录音
    private MediaPlayer mPlayer = null;
    private MediaRecorder mRecorder = null;

    //开始录音
    private Button startRecord;
    //播放录音
    private Button startPlay;

    private int isLongClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_click);

        //绑定录音按钮响应事件
        startRecord = (Button) findViewById(R.id.startRecord);
        startRecord.setOnLongClickListener(new StartRecordListener());
        startRecord.setOnClickListener(new StopRecordListener());

        //绑定播放按钮响应事件
        startPlay = (Button) findViewById(R.id.startPlay);
        startPlay.setOnClickListener(new StartPlayListener());

        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileName = fileName+"/myRecord.3gp";

    }

    //长按录音，松开之后自动执行短按操作
    class StartRecordListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            isLongClick = 1;
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(fileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                Log.d("AudioTag", "prepare failed");
            }
            mRecorder.start();
            return false;
        }
    }

    //短按停止录音，直接单击无效
    class StopRecordListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (isLongClick == 1) {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
                isLongClick = 0;
            }
        }
    }

    class StartPlayListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(fileName);
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                Log.d("AudioTag", "播放失败");
            }
        }
    }
}
