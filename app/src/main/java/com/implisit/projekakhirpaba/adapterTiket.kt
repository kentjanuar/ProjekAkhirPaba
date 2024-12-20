import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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

    }

    override fun getItemCount() = ticketList.size
}