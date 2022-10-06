package com.umutakpinar.catchmeifyoucan

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.umutakpinar.catchmeifyoucan.databinding.ActivityGameBinding
import com.umutakpinar.catchmeifyoucan.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var switch : Boolean? = null
    private var bestScore : Int = 0
    private var bestScoreGodMode : Int = 0
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view : View = binding.root
        setContentView(view)

        //Button color blue
        binding.btnStartGame.setBackgroundColor(Color.BLUE)

        //Switch cod null durumdan çıakrıldı şuanki değeri eşitlendi
        switchCondition()

        //set sharedPreferences
        sharedPreferences = this@MainActivity.getSharedPreferences("com.umutakpinar.catchmeifyoucan",Context.MODE_PRIVATE)

        //check best Score show it on screen
        checkBestScore()
        showBestScore(false)
    }

    public fun playButtonClicked(view: View){
        intent = Intent(this@MainActivity,GameActivity::class.java)
        intent.putExtra("godMode",switch)
        startActivity(intent)
        finish()
    }

    public fun switchChanged(view : View){
        switchCondition()
    }

    private fun switchCondition(){
        switch = binding.switchGodMode.isChecked
        if(switch == true){ //Burada godMode açık oludğu için bestSCore'u gizleyip godMode'un best Score'unu göstermelisin
            binding.btnStartGame.setBackgroundColor(Color.RED)
        }else{
            binding.btnStartGame.setBackgroundColor(Color.BLUE)
        }
        showBestScore(switch!!)
        Toast.makeText(this@MainActivity,"God mode: $switch",Toast.LENGTH_SHORT).show()
    }

    private fun checkBestScore(){
        bestScore = sharedPreferences.getInt("bestScore",0);
        bestScoreGodMode = sharedPreferences.getInt("bestScoreGodMode",0)
    }

    private fun showBestScore(godMode : Boolean){
        if(godMode){
            val scoreText = "Best (God): $bestScoreGodMode"
            binding.textViewBestScore.text = scoreText
        }else{
            val scoreText = "Best : $bestScore"
            binding.textViewBestScore.text = scoreText
        }
    }

    public fun resetScores(view : View){
        val areYouSureAlertDialog = AlertDialog.Builder(this@MainActivity)
        areYouSureAlertDialog.setTitle("Delete Scores")
        areYouSureAlertDialog.setMessage("Do you really want to delete your best scores?")
        areYouSureAlertDialog.setPositiveButton("Yes, delete"){dialog, which ->
            sharedPreferences.edit().clear().apply()
            recreate()
        }
        areYouSureAlertDialog.setNegativeButton("NO! Never"){dialog, which ->
            Toast.makeText(this@MainActivity,"Don't worry mate, we keep your score...",Toast.LENGTH_SHORT).show()
        }
        areYouSureAlertDialog.setCancelable(false);
        areYouSureAlertDialog.show()
    }

}