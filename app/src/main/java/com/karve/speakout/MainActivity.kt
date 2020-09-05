package com.karve.speakout

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var opText : EditText
      lateinit  var mTTS : TextToSpeech
   lateinit var mEditText : EditText
    lateinit var  mSeekBarPitch : SeekBar
    lateinit var mSeekBarSpeech : SeekBar
    lateinit var mButtonSpeech : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mButtonSpeech = findViewById(R.id.buSpeak)
        opText = findViewById(R.id.txt_op)

    mTTS = TextToSpeech(this,TextToSpeech.OnInitListener() {
        var status = 0
        if (status==TextToSpeech.SUCCESS){
            var result = mTTS.setLanguage(Locale.ENGLISH)
            if (result==TextToSpeech.LANG_MISSING_DATA
                || result==TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","Language not supported")
            }else{
                mButtonSpeech.isEnabled = true
            }
        }else{
            Log.e("TTS", "Initialisation failed")
        }
    })

        mEditText = findViewById(R.id.txt_op)
       // mEditText=findViewById(R.id.et1)
        mSeekBarPitch = findViewById(R.id.seekBarPitch)
        mSeekBarSpeech = findViewById(R.id.seekBar2)
    mButtonSpeech.setOnClickListener {
        speak()

    }
    }
    fun speak(){
        var text = mEditText.text.toString()
        var pitch = (mSeekBarPitch.progress  / 50) .toFloat()
    if (pitch<0.1) {
        pitch = 0.1f
    }
        var speed = (mSeekBarSpeech.progress  / 50).toFloat()
        if (speed<0.1) {
            speed = 0.1f
        }
        mTTS.setPitch(pitch)
        mTTS.setSpeechRate(speed)

        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null)
    }

    @Override
    override fun onDestroy() {
        if (mTTS!=null){
            mTTS.stop()
            mTTS.shutdown()
        }
        super.onDestroy()
    }

    fun btnSpeech(view: View) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
            "HY SPEAK SOMETHING")
        try {
            startActivityForResult(intent, 1)

        }catch (e:ActivityNotFoundException){
            Toast.makeText(this,e.message, Toast.LENGTH_SHORT).show()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
         if (resultCode == Activity.RESULT_OK && null != data){
            var result : ArrayList<String> =data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            opText.setText(result.get(0))
        /*when(requestCode){
           resultCode->
                }*/

        }
    }
}