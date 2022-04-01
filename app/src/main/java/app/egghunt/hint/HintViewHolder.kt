package app.egghunt.hint

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.egghunt.R

class HintViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var text: TextView = view.findViewById(R.id.text)
    var timePosted: TextView = view.findViewById(R.id.time_posted)
}