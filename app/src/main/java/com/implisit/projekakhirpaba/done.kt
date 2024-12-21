package com.implisit.projekakhirpaba

import adapterTiket
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [done.newInstance] factory method to
 * create an instance of this fragment.
 */
class done : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_done, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterTiket = adapterTiket(arTiket)

        val _rvTiket = view.findViewById<RecyclerView>(R.id.rvDone)
        _rvTiket.adapter = adapterTiket
        _rvTiket.layoutManager = LinearLayoutManager(context)

        val _jumlah = view.findViewById<TextView>(R.id.tiketDone)

        val sharedPreference: SharedPreferences = requireActivity().getSharedPreferences("UserSession", 0)
        val userPhone = sharedPreference.getString("userPhone", null)

        if (userPhone != null) {
            readTickets(userPhone, _jumlah)

        } else {
            Toast.makeText(context, "User phone number not found in SharedPreferences", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readTickets(userPhone: String, jumlah: TextView) {
        db.collection("Users").document(userPhone)
            .collection("Tickets")
            .get()
            .addOnSuccessListener { result ->
                arTiket.clear()
                for (document in result) {
                    val seatNumbers = document.get("seatNumber") as? List<String> ?: emptyList()
                    val seatNumbersString = seatNumbers.joinToString(", ")

                    // Ambil status ticket
                    val statusDone = document.getBoolean("status_Done") ?: false

                    // Tambahkan kondisi untuk hanya menampilkan tiket dengan status true
                    if (statusDone) {
                        val ticket = tiket(
                            judul = document.getString("movieTitle") ?: "",
                            tanggal = document.getString("tanggal_Tayang") ?: "",
                            jam = document.getString("selectedTime") ?: "",
                            tempat = document.getString("theaterName") ?: "",
                            seat = seatNumbersString,
                            location = document.getString("theaterAddress") ?: "",
                            genre = document.getString("genre") ?: "",
                            statusDone = statusDone,
                            ticketID = document.getString("ticketID") ?: ""
                        )
                        arTiket.add(ticket)
                    }
                }
                jumlah.text = arTiket.size.toString()
                adapterTiket.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Gagal mengambil data tiket: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("done", "Error fetching tickets", e)
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            done().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private lateinit var adapterTiket: adapterTiket
    private var arTiket: MutableList<tiket> = mutableListOf()
}