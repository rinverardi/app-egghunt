package app.egghunt.hunter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.egghunt.R

internal class HunterTabAdapter(private val activity: HunterActivity) :
    RecyclerView.Adapter<HunterTabViewHolder>() {

    override fun getItemCount(): Int = 3

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> R.layout.tab_eggs
        1 -> R.layout.tab_scores
        2 -> R.layout.tab_hints
        else -> throw NotImplementedError()
    }

    override fun onBindViewHolder(viewHolder: HunterTabViewHolder, position: Int) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HunterTabViewHolder {
        val tab: View = LayoutInflater.from(activity).inflate(viewType, parent, false)

        activity.rebind(tab)
        activity.reinitialize(tab)

        return HunterTabViewHolder(tab)
    }
}