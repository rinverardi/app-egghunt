package app.egghunt.hint

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.egghunt.R

class HintViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var postedAt: TextView = view.findViewById(R.id.posted_at)
    var text: TextView = view.findViewById(R.id.text)
}