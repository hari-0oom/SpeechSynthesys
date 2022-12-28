package com.hari_0oom.speechsynthasys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;

import com.hari_0oom.speechsynthasys.databinding.ActivityMainBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private TextToSpeech mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());


        mTTS= new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                Toast.makeText(MainActivity.this, "chal raha hai" , Toast.LENGTH_SHORT).show();
                if(i==TextToSpeech.SUCCESS){
                    int result=mTTS.setLanguage(Locale.US);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(MainActivity.this, "lang not supported", Toast.LENGTH_SHORT).show();
                    }else{
                        binding.btnspeak.setEnabled(true);
                    }
                }else{
                    Toast.makeText(MainActivity.this, "init unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.btnspeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });

        binding.btnmic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listen();
            }
        });

    }

    private void speak() {

        String text= binding.et.getText().toString();
        mTTS.setPitch((float)(binding.seekBarPitch.getProgress()/50));
        mTTS.setSpeechRate((float)binding.seekBarSpeed.getProgress()/50);
        if(text==null || "".equals(text)){
            mTTS.speak("get lost", TextToSpeech.QUEUE_FLUSH, null);
        }else {
            mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }

    }

    private void listen() {
        //intent to show speech to text dialogue
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra("android.speech.extra.LANGUAGE", Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Something Om ...");
        try {
            //success case

            startActivityForResult(intent, 1);

        }catch(Exception e){
            //in case of failure ue display the error through the toast
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    //recieve voice input...

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode==RESULT_OK && data!=null){
            //get text input from voice input
            ArrayList<String> result= data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            binding.et.setText(result.get(0));
        }

    }

    @Override
    protected void onDestroy() {

        if(mTTS!=null){
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }
}