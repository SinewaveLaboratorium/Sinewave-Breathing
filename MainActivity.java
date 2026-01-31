package com.sinewavelab.sinewavebreathing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;

public class MainActivity extends AppCompatActivity {

    private TextView countdownText;
    private Vibrator vibrator;
    private CountDownTimer countDownTimer;
    private boolean countDownRunning = false;
    public SharedPreferences breathing_settings;
    public SharedPreferences.Editor editor;
    private int timeLeftMilliseconds;
    private final int one_minute = 60000;
    private ToggleButton startStop;
    private SoundPool soundPool;
    private AudioManager audioManager;
    private final float initial_frequency = (float) 0.08;
    private float current_frequency;
    private static final int streamType = AudioManager.STREAM_MUSIC;
    private float volume, currentVolumeIndex, maxVolumeIndex, progress;
    private float flag = (float) 2.0;
    private int click, ending, change;
    private String time, time_formatted, soundguide;
    private boolean vibration = true;
    private boolean session_ending = false;
    private LottieAnimationView sinewave_animation;
    private  Animation fadeOut, fadeIn;
    private ImageView wait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        breathing_settings = getApplicationContext().getSharedPreferences("Breathing_frequency", MODE_PRIVATE);
        editor = breathing_settings.edit();
        current_frequency = breathing_settings.getFloat("frequency", (float) 0.08);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        time = prefs.getString("TIME", "5");
        soundguide = (prefs.getString("SFX", "4"));
        vibration = prefs.getBoolean("VIBRATION",true);

        wait = (ImageView) findViewById(R.id.wait);
        startStop = (ToggleButton) findViewById(R.id.breathe_toggle);

        countdownText = (TextView) findViewById(R.id.session_time);

        timeLeftMilliseconds = Integer.valueOf(time) * one_minute;
        int minutes = (timeLeftMilliseconds / 1000) / 60;
        int seconds = (timeLeftMilliseconds / 1000) % 60;
        String formatted_minutes = String.format("%02d", minutes);
        String formatted_seconds = String.format("%02d", seconds);
        time_formatted = "Session: " + formatted_minutes + ":" + formatted_seconds;
        countdownText.setText(time_formatted);

        sinewave_animation = (LottieAnimationView) findViewById(R.id.sinewave_animation);
        sinewave_animation.setSpeed(current_frequency / initial_frequency);
        sinewave_animation.pauseAnimation();
        sinewave_animation.addLottieOnCompositionLoadedListener(new LottieOnCompositionLoadedListener() {
            @Override
            public void onCompositionLoaded(LottieComposition composition) {
                wait.setVisibility(View.GONE);
                startStop.setEnabled(true);
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        initVolumeBar(); // Volume bar
        createSoundPoolNew();
        click = soundPool.load(this, R.raw.click_in, 1);
        ending = soundPool.load(this, R.raw.ending, 1);
        change = soundPool.load(this, R.raw.change, 1);

        if (timeLeftMilliseconds == 0){
            countdownText.setVisibility(View.GONE);
        } else {
            countdownText.setVisibility(View.VISIBLE);
        }

        TextView frequency_display = (TextView) findViewById(R.id.breathing_frequency);
        frequency_display.setText(current_frequency + " Hz");

        Button minusbutton = (Button) findViewById(R.id.minus);
        minusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float minus;

                if (current_frequency > 0.06){
                    minus = (float) 0.005;
                } else {
                    minus = (float) 0.001;
                }

                float if_frequency = (float) (Math.round((current_frequency - minus) * 1000d) / 1000d);

                if (if_frequency > 0.02) {
                    current_frequency = if_frequency;
                    frequency_display.setText(current_frequency + " Hz");
                    sinewave_animation.setSpeed(current_frequency / initial_frequency);
                    editor.putFloat("frequency", current_frequency);
                    editor.apply();
                }
            }
        });

        Button minusminusbutton = (Button) findViewById(R.id.minusminus);
        minusminusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float minusminus;

                minusminus = (float) 0.025;

                float if_frequency = (float) (Math.round((current_frequency - minusminus) * 1000d) / 1000d);

                if (if_frequency > 0.02) {
                    current_frequency = if_frequency;
                    frequency_display.setText(current_frequency + " Hz");
                    sinewave_animation.setSpeed(current_frequency / initial_frequency);
                    editor.putFloat("frequency", current_frequency);
                    editor.apply();
                }
            }
        });


        Button plusbutton = (Button) findViewById(R.id.plus);
        plusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float plus;
                if (current_frequency > 0.06){
                    plus = (float) 0.005;
                } else {
                    plus = (float) 0.001;
                }


                float if_frequency = (float) (Math.round((current_frequency + plus) * 1000d) / 1000d);

                if (if_frequency < 0.135){
                    current_frequency = if_frequency;
                    frequency_display.setText(current_frequency + " Hz");
                    sinewave_animation.setSpeed(current_frequency / initial_frequency);
                    editor.putFloat("frequency", current_frequency);
                    editor.apply();}
            }
        });


        Button plusplusbutton = (Button) findViewById(R.id.plusplus);
        plusplusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float plusplus;
                plusplus = (float) 0.025;

                float if_frequency = (float) (Math.round((current_frequency + plusplus) * 1000d) / 1000d);

                if (if_frequency < 0.135){
                    current_frequency = if_frequency;
                    frequency_display.setText(current_frequency + " Hz");
                    sinewave_animation.setSpeed(current_frequency / initial_frequency);
                    editor.putFloat("frequency", current_frequency);
                    editor.apply();}
            }
        });


        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = startStop.isChecked();
                if (on) {
                    startTimer();
                    session_ending = false;
                    sinewave_animation.playAnimation();
                } else {
                    if (Integer.valueOf(time) > 0){
                        timeLeftMilliseconds = Integer.valueOf(time) * one_minute; // restart session timer
                        countDownTimer.cancel();
                        int minutes = (timeLeftMilliseconds / 1000) / 60;
                        int seconds = (timeLeftMilliseconds / 1000) % 60;
                        String formatted_minutes = String.format("%02d", minutes);
                        String formatted_seconds = String.format("%02d", seconds);
                        countdownText.setText("Session: " + formatted_minutes + ":" + formatted_seconds);
                    }
                    soundPool.stop(click);
                    soundPool.stop(change);
//                    soundPool.play(ending, volume, volume, 0, 0, 1);
                    sinewave_animation.startAnimation(fadeOut);
                    sinewave_animation.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sinewave_animation.setProgress(0);
                            sinewave_animation.startAnimation(fadeIn);
                            sinewave_animation.pauseAnimation();
                        }
                    }, 999);

                }
            }
        });

        sinewave_animation.addAnimatorUpdateListener(
                animation -> {

                    progress = (float) (Math.round((sinewave_animation.getProgress()) * 100f) / 100f);

                    if ((progress == 0.05f && progress != flag && soundguide.compareTo("4") == 0 && !session_ending) ||
                            (progress == 0.15f && progress != flag && soundguide.compareTo("4") == 0 && !session_ending) ||
                            (progress == 0.35f && progress != flag && soundguide.compareTo("4") == 0 && !session_ending) ||
                            (progress == 0.45f && progress != flag && soundguide.compareTo("4") == 0 && !session_ending) ||
                            (progress == 0.55f && progress != flag && soundguide.compareTo("4") == 0 && !session_ending) ||
                            (progress == 0.65f && progress != flag && soundguide.compareTo("4") == 0 && !session_ending) ||
                            (progress == 0.85f && progress != flag && soundguide.compareTo("4") == 0 && !session_ending) ||
                            (progress == 0.95f && progress != flag && soundguide.compareTo("4") == 0 && !session_ending)){
                        flag = progress;
                        soundPool.play(click, volume, volume, 0, 0, 1);
                        if (vibration){
                            vibrate(50);}
                    }

                    if ((progress == 0.25f && progress != flag && (soundguide.compareTo("2") == 0 || soundguide.compareTo("4") == 0) && !session_ending)  ||
                            (progress == 0.75f && progress != flag && (soundguide.compareTo("2") == 0 || soundguide.compareTo("4") == 0)) && !session_ending){
                        flag = progress;
                        soundPool.play(change, volume, volume, 0, 0, 1);
                        if (vibration){
                            vibrate(100);
                        }
                    }

                });


        PreferenceManager.setDefaultValues(this, R.xml.preferences, false); // load default settings.
        PreferenceManager.getDefaultSharedPreferences(this); // Load user settings.
    }

    private void createSoundPoolNew() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else { // build Sound Pool
//            @SuppressWarnings("deprecation");
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
    }

    private void startTimer(){
        countDownRunning = true;
        countDownTimer = new CountDownTimer(timeLeftMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) ((millisUntilFinished / 1000) / 60);
                int seconds = (int) ((millisUntilFinished / 1000) % 60);
                String formatted_minutes = String.format("%02d", minutes);
                String formatted_seconds = String.format("%02d", seconds);
                countdownText.setText("Session: " + formatted_minutes + ":" + formatted_seconds);
            }

            @Override
            public void onFinish() {
                countdownText.setText("Session Done!");
                countDownRunning = false;
                session_ending = true;
                soundPool.stop(click);
                soundPool.stop(change);
                soundPool.play(ending, volume, volume, 0, 0, 1);
                if (vibration){vibrate(250);}
                flag = progress;
                startStop.setChecked(false);

                sinewave_animation.startAnimation(fadeOut);
                sinewave_animation.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sinewave_animation.setProgress(0);
                        sinewave_animation.startAnimation(fadeIn);
                        sinewave_animation.pauseAnimation();
                    }
                }, 999);

            }
        }.start();
    }

    private void vibrate(int ms){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(ms);
        }
    }

    private void initVolumeBar() {
        try {
            SeekBar volumeSeekBar = (SeekBar) findViewById(R.id.volumebar);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            currentVolumeIndex = (float) audioManager.getStreamVolume(streamType);
            maxVolumeIndex  = (float) audioManager.getStreamMaxVolume(streamType);
            volume = currentVolumeIndex / maxVolumeIndex;
            this.setVolumeControlStream(streamType);

            volumeSeekBar.setMax((int) maxVolumeIndex);
            volumeSeekBar.setProgress((int) currentVolumeIndex);
            volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    currentVolumeIndex = (float) audioManager.getStreamVolume(streamType);
                    volume = currentVolumeIndex / maxVolumeIndex;

                    switch( audioManager.getRingerMode() ){
                        case AudioManager.RINGER_MODE_SILENT:
                            Toast.makeText(MainActivity.this, "Phone is on silent mode", Toast.LENGTH_SHORT).show();
                        case AudioManager.RINGER_MODE_VIBRATE:
                            Toast.makeText(MainActivity.this, "Phone is on vibrate mode", Toast.LENGTH_SHORT).show();
                        default:
                            break;
                    }

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        } catch (Exception e) {

        }
    } // Volume bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (countDownRunning) {
            countDownTimer.cancel();
            countDownRunning = false;
        }
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}