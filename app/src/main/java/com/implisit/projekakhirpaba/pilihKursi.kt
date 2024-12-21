package com.implisit.projekakhirpaba

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class pilihKursi : AppCompatActivity() {
    private lateinit var judul: String
    private lateinit var rating: String
    private lateinit var gambar: String
    private lateinit var deskripsi: String
    private lateinit var tahun: String
    private lateinit var genre: String
    private lateinit var selectedDate: String
    private lateinit var selectedTime: String
    private lateinit var theaterName: String
    private lateinit var theaterAddress: String
    private  var done = false
    private  val selectedSeats = mutableListOf<String>()
    private lateinit var no_telpon: String
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_kursi)

        judul = intent.getStringExtra("judul") ?: ""
        rating = intent.getStringExtra("rating") ?: ""
        gambar = intent.getStringExtra("gambar") ?: ""
        deskripsi = intent.getStringExtra("deskripsi") ?: ""
        tahun = intent.getStringExtra("tahun") ?: ""
        genre = intent.getStringExtra("genre") ?: ""
        selectedDate = intent.getStringExtra("selectedDate") ?: ""
        selectedTime = intent.getStringExtra("selectedTime") ?: ""
        theaterName = intent.getStringExtra("theaterName") ?: ""
        theaterAddress = intent.getStringExtra("theaterAddress") ?: ""
        no_telpon = intent.getStringExtra("userPhone") ?: ""

        val seats = listOf(
            findViewById<ImageView>(R.id.A1).apply { tag = "A1" },
            findViewById<ImageView>(R.id.A2).apply { tag = "A2" },
            findViewById<ImageView>(R.id.A3).apply { tag = "A3" },
            findViewById<ImageView>(R.id.A4).apply { tag = "A4" },
            findViewById<ImageView>(R.id.B1).apply { tag = "B1" },
            findViewById<ImageView>(R.id.B2).apply { tag = "B2" },
            findViewById<ImageView>(R.id.B3).apply { tag = "B3" },
            findViewById<ImageView>(R.id.B4).apply { tag = "B4" },
            findViewById<ImageView>(R.id.C1).apply { tag = "C1" },
            findViewById<ImageView>(R.id.C2).apply { tag = "C2" },
            findViewById<ImageView>(R.id.C3).apply { tag = "C3" },
            findViewById<ImageView>(R.id.C4).apply { tag = "C4" },
            findViewById<ImageView>(R.id.D1).apply { tag = "D1" },
            findViewById<ImageView>(R.id.D2).apply { tag = "D2" },
            findViewById<ImageView>(R.id.D3).apply { tag = "D3" },
            findViewById<ImageView>(R.id.D4).apply { tag = "D4" },
            findViewById<ImageView>(R.id.D5).apply { tag = "D5" },
            findViewById<ImageView>(R.id.D6).apply { tag = "D6" },
            findViewById<ImageView>(R.id.E1).apply { tag = "E1" },
            findViewById<ImageView>(R.id.E2).apply { tag = "E2" },
            findViewById<ImageView>(R.id.E3).apply { tag = "E3" },
            findViewById<ImageView>(R.id.E4).apply { tag = "E4" },
            findViewById<ImageView>(R.id.E5).apply { tag = "E5" },
            findViewById<ImageView>(R.id.E6).apply { tag = "E6" },
            findViewById<ImageView>(R.id.F1).apply { tag = "F1" },
            findViewById<ImageView>(R.id.F2).apply { tag = "F2" },
            findViewById<ImageView>(R.id.F3).apply { tag = "F3" },
            findViewById<ImageView>(R.id.F4).apply { tag = "F4" },
            findViewById<ImageView>(R.id.F5).apply { tag = "F5" },
            findViewById<ImageView>(R.id.F6).apply { tag = "F6" }

        )
        checkSeatsAvailability(seats)

        seats.forEach { seat ->
            seat.setOnClickListener {
                db.collection("Users").document(no_telpon)
                    .collection("Tickets")
                    .whereEqualTo("tanggal_Tayang", selectedDate)
                    .whereEqualTo("selectedTime", selectedTime)
                    .whereEqualTo("seatNumber", seat.tag)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        if (snapshot.isEmpty) {
                            if (selectedSeats.contains(seat.tag)) {
                                selectedSeats.remove(seat.tag)
                                seat.setImageResource(R.drawable.chair)
                            } else {
                                selectedSeats.add(seat.tag as String)
                                seat.setImageResource(R.drawable.chair_picked)
                            }
                        } else {
                            Toast.makeText(this, "Kursi ini sudah dipesan!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal memeriksa status kursi: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        val pesanTiket = findViewById<Button>(R.id.pesenTiket)
        pesanTiket.setOnClickListener {

            val ticketData = hashMapOf(
                "movieTitle" to judul,
                "seatNumber" to selectedSeats,
                "tanggal_Tayang" to selectedDate,
                "rating" to rating,
                "gambar" to gambar,
                "deskripsi" to deskripsi,
                "tahun" to tahun,
                "genre" to genre,
                "selectedTime" to selectedTime,
                "theaterName" to theaterName,
                "theaterAddress" to theaterAddress,
                "no_telpon" to no_telpon,
                "status_Done" to done
            )

            val db = FirebaseFirestore.getInstance()
            db.collection("Users").document(no_telpon)
                .collection("Tickets")
                .document(judul)
                .set(ticketData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Tiket berhasil dipesan!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal memesan tiket: ${e.message}", Toast.LENGTH_SHORT).show()
                }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)


        }
    }

    private fun checkSeatsAvailability(seats: List<ImageView>) {
        db.collection("Users")
            .get()
            .addOnSuccessListener { userSnapshot ->
                for (userDoc in userSnapshot.documents) {
                    userDoc.reference.collection("Tickets")
                        .whereEqualTo("tanggal_Tayang", selectedDate)
                        .whereEqualTo("selectedTime", selectedTime)
                        .whereEqualTo("theaterName", theaterName)
                        .whereEqualTo("movieTitle", judul)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            for (document in snapshot.documents) {
                                val bookedSeats = document.get("seatNumber") as? List<String>
                                if (bookedSeats != null) {

                                    bookedSeats.forEach { bookedSeat ->
                                        seats.find { it.tag == bookedSeat }?.apply {
                                            setImageResource(R.drawable.chair_taken)
                                            isClickable = false
                                            alpha = 0.5f
                                        }
                                    }
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Gagal memeriksa ketersediaan kursi: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal memeriksa pengguna: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


}