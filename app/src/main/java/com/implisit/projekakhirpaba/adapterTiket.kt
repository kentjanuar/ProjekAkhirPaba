import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.implisit.projekakhirpaba.R
import com.implisit.projekakhirpaba.tiket

class adapterTiket(private val ticketList: List<tiket>) : RecyclerView.Adapter<adapterTiket.TicketViewHolder>() {

    class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val judul: TextView = itemView.findViewById(R.id.judulTiketFilm)
        val tanggal: TextView = itemView.findViewById(R.id.tanggalTiket)
        val jam: TextView = itemView.findViewById(R.id.jamTiket)
        val tempat: TextView = itemView.findViewById(R.id.tempatBioskop)
        val seat: TextView = itemView.findViewById(R.id.seat)
        val location: TextView = itemView.findViewById(R.id.location)
        val genre: TextView = itemView.findViewById(R.id.genre)
        val qrCode: ImageView = itemView.findViewById(R.id.qrCode)
        val done: Button = itemView.findViewById(R.id.btnStatus)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.tiket, parent, false)
        return TicketViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val tiketFilm = ticketList[position]
        holder.judul.text = tiketFilm.judul
        holder.tanggal.text = tiketFilm.tanggal
        holder.jam.text = tiketFilm.jam
        holder.tempat.text = tiketFilm.tempat
        holder.seat.text = tiketFilm.seat
        holder.location.text = tiketFilm.location
        holder.genre.text = tiketFilm.genre
        val qrCodeBitmap = generateQRCode(tiketFilm)
        holder.qrCode.setImageBitmap(qrCodeBitmap)

        if (tiketFilm.statusDone) {
            holder.done.visibility = View.GONE
            holder.done.isClickable = false
        } else {
            holder.done.visibility = View.VISIBLE
            holder.done.isClickable = true
        }


        holder.done.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val sharedPreferences = holder.itemView.context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val userPhone = sharedPreferences.getString("userPhone", "")

            if (userPhone != null) {
                db.collection("Users").document(userPhone)
                    .collection("Tickets").document(tiketFilm.ticketID)
                    .update("status_Done", true)
                    .addOnSuccessListener {
                        Toast.makeText(holder.itemView.context, "Status updated to done!", Toast.LENGTH_SHORT).show()
                        holder.done.visibility = View.GONE
                        holder.done.isClickable = false

                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(holder.itemView.context, "Failed to update status: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }

        }

    }


    private fun generateQRCode(ticket: tiket): Bitmap {
        val size = 512
        val qrCodeWriter = QRCodeWriter()
        val text = formatTicketDetails(ticket)
        return try {
            val bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size)
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
            for (x in 0 until size) {
                for (y in 0 until size) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bitmap
        } catch (e: WriterException) {
            Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        }
    }

    private fun formatTicketDetails(ticket: tiket): String {
        return """
        Title: ${ticket.judul}
        Genre: ${ticket.genre}
        Date: ${ticket.tanggal}
        Time: ${ticket.jam}
        Theater: ${ticket.tempat}
        Seat: ${ticket.seat}
        Location: ${ticket.location}
    """.trimIndent()
    }

    override fun getItemCount() = ticketList.size
}