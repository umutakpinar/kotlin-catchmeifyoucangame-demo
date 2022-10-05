package com.umutakpinar.catchmeifyoucan

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import com.umutakpinar.catchmeifyoucan.databinding.ActivityGameBinding
import com.umutakpinar.catchmeifyoucan.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var switch : Boolean? = null
    private var bestScore : Int = 0
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view : View = binding.root
        setContentView(view)

        //Switch cod null durumdan çıakrıldı şuanki değeri eşitlendi
        switchCondition()

        //set sharedPreferences
        sharedPreferences = this@MainActivity.getSharedPreferences("com.umutakpinar.catchmeifyoucan",Context.MODE_PRIVATE)

        //check best Score show it on screen
        checkBestScore()
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
        Toast.makeText(this@MainActivity,"God mode: $switch",Toast.LENGTH_SHORT).show()
    }

    private fun checkBestScore(){
        bestScore = sharedPreferences.getInt("bestScore",0);
    }

    private fun showBestScore(){
        binding.textViewBestScore.text = bestScore.toString();
    }
}