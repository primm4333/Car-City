// MainActivity.kt

package com.example.carcity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity(), Task {
    lateinit var rootLayout : LinearLayout
    lateinit var startBtn : Button
    lateinit var mgameView : View
    lateinit var score : TextView
    lateinit var Hscore : TextView
    private var highScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startBtn = findViewById(R.id.startBtn)
        rootLayout = findViewById(R.id.rootLayout)
        score = findViewById(R.id.Score)
        Hscore = findViewById(R.id.Hscore)

        highScore = getHighScore()
        Hscore.text = "High Score: $highScore"

        mgameView = View(this, this)

        startBtn.setOnClickListener(){
            mgameView.setBackgroundResource(R.drawable.road)
            rootLayout.addView(mgameView)
            startBtn.visibility = android.view.View.GONE
            score.visibility = android.view.View.GONE
            Hscore.visibility = android.view.View.GONE
        }
    }

    override fun closeGame(mScore: Int, highScore: Int) {
        score.text = "Score : $mScore"

        if (mScore > getHighScore()) {
            saveHighScore(mScore)
            this.highScore = mScore
        }


        Hscore.text = "High Score : ${getHighScore()}"

        rootLayout.removeView(mgameView)
        startBtn.visibility = android.view.View.VISIBLE
        score.visibility = android.view.View.VISIBLE
        Hscore.visibility = android.view.View.VISIBLE

        mgameView = View(this, this)
    }

    private fun getHighScore(): Int {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        return sharedPreferences.getInt("highScore", 0)
    }

    private fun saveHighScore(score: Int) {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("highScore", score)
        editor.apply()
    }
}
