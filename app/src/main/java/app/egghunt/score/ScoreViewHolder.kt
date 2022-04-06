package app.egghunt.score

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.egghunt.R

class ScoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val count: TextView? = view.findViewById(R.id.count)
    val description: TextView? = view.findViewById(R.id.description)
    val medal: TextView? = view.findViewById(R.id.medal)
    val position: TextView? = view.findViewById(R.id.position)
}