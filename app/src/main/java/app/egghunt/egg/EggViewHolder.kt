package app.egghunt.egg

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.egghunt.R

class EggViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var description: TextView = view.findViewById(R.id.description)
    var timeFound: TextView = view.findViewById(R.id.time_found)
    var timeHidden: TextView = view.findViewById(R.id.time_hidden)
}