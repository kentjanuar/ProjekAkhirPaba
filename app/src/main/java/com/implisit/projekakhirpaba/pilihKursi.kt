package com.implisit.projekakhirpaba

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class pilihKursi : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_kursi)



        val seats = listOf(
            findViewById<ImageView>(R.id.A1),
            findViewById<ImageView>(R.id.A2),
            findViewById<ImageView>(R.id.A3),
            findViewById<ImageView>(R.id.A4),
            findViewById<ImageView>(R.id.B1),
            findViewById<ImageView>(R.id.B2),
            findViewById<ImageView>(R.id.B3),
            findViewById<ImageView>(R.id.B4),
            findViewById<ImageView>(R.id.C1),
            findViewById<ImageView>(R.id.C2),
            findViewById<ImageView>(R.id.C3),
            findViewById<ImageView>(R.id.C4),
            findViewById<ImageView>(R.id.D1),
            findViewById<ImageView>(R.id.D2),
            findViewById<ImageView>(R.id.D3),
            findViewById<ImageView>(R.id.D4),
            findViewById<ImageView>(R.id.D5),
            findViewById<ImageView>(R.id.D6),
            findViewById<ImageView>(R.id.E1),
            findViewById<ImageView>(R.id.E2),
            findViewById<ImageView>(R.id.E3),
            findViewById<ImageView>(R.id.E4),
            findViewById<ImageView>(R.id.E5),
            findViewById<ImageView>(R.id.E6),
            findViewById<ImageView>(R.id.F1),
            findViewById<ImageView>(R.id.F2),
            findViewById<ImageView>(R.id.F3),
            findViewById<ImageView>(R.id.F4),
            findViewById<ImageView>(R.id.F5),
            findViewById<ImageView>(R.id.F6)
        )

        seats.forEach { seat ->
            seat.setOnClickListener {
                seat.setImageResource(R.drawable.chair_picked)
            }
        }
    }
}