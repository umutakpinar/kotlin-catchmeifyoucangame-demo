package com.umutakpinar.catchmeifyoucan

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.umutakpinar.catchmeifyoucan.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    private lateinit var binding : ActivityGameBinding
    private var score : Int = 0
    private var godMode = false
    private lateinit var sharedPreferences: SharedPreferences
    private var bestScore : Int = 0
    //Runnable
    private var runnable : Runnable = Runnable{ }
    private var handler : Handler = Handler(Looper.getMainLooper())
    private var time : Int = 16
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view : View = binding.root
        setContentView(view)

        //setSharedPreferences
        sharedPreferences = this@GameActivity.getSharedPreferences("com.umutakpinar.catchmeifyoucan",Context.MODE_PRIVATE)

        //checkbestScore and set
        checkBestScore()

        // Scoreekrnada gösterildi ve godMode atandı
        setScoreText()
        godMode = isGodModeEnabled()
        if(godMode){
            Toast.makeText(this@GameActivity,"God mode enabled!",Toast.LENGTH_SHORT).show()
        }

        //setDisable go text
        binding.textViewInfo.isEnabled = false

        //startCountDownfor bgin
        startCountDownForBeginingAndStartTheGame()
    }

    fun caught(view: View){
        increaseScoreOnePoint()
        setScoreText()
    }

    private fun increaseScoreOnePoint(){
        score++
    }

    private fun setScoreText(){
        val scoreText = "Score: $score"
        binding.textViewScore.text = scoreText
    }

    private fun isGodModeEnabled() : Boolean{
        val mainActIntent = intent
        return mainActIntent.getBooleanExtra("godMode",false)
    }

    private fun checkBestScore(){
        bestScore = sharedPreferences.getInt("bestScore",0)
    }

    private fun checkNewScoreIsBestAndSetIfTrue(){
        if( score > bestScore){
            sharedPreferences.edit().putInt("bestScore",score).apply()
            Toast.makeText(this@GameActivity,"Voila! You broke your best score!",Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTimerAndGame(){
        runnable = object : Runnable {
            override fun run() {
                if(time > 0){
                    time--
                    showTimeText()
                    handler.postDelayed(this,1000)
                }else {
                    stopTimer() //goyazısını sil ve kırmızı yap score'u kaydet ve ekranda göster!
                    binding.textViewInfo.setTextColor(Color.parseColor("#FF0000"))
                    binding.textViewInfo.text = "Stop!"
                    checkNewScoreIsBestAndSetIfTrue()
                    val endGameAlertDialog = AlertDialog.Builder(this@GameActivity)
                    endGameAlertDialog.setTitle("Time's up!")
                    endGameAlertDialog.setMessage("Your Score $score")
                    endGameAlertDialog.setPositiveButton("Restart!") {dialog, which ->
                        val intent = Intent(this@GameActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    endGameAlertDialog.setNegativeButton("Exit") {dialog,which ->
                        finish()
                    }
                    endGameAlertDialog.show()
                }
            }
        }
        handler.post(runnable)
    }

    private fun startCountDownForBeginingAndStartTheGame(){
        var countdown = 3
        object : CountDownTimer(3000,1000){
            override fun onTick(p0: Long) {
                binding.textViewTime.text = countdown.toString()
                countdown--
            }

            override fun onFinish() {
                startTimerAndGame()
                binding.textViewInfo.isEnabled = true
            }

        }
    }

    private fun stopTimer(){
        handler.removeCallbacks(runnable)
        time = 16
        showTimeText()
    }

    private fun showTimeText(){
        val timeText = "Time: $time"
        binding.textViewTime.text = timeText
    }
}