package app.egghunt.score

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import app.egghunt.R

class ScoreAdapter : ListAdapter<Any, ScoreViewHolder>(ScoreDiff) {
    override fun getItemViewType(position: Int): Int =
        if (getItem(position).javaClass == Rank::class.java) {
            R.layout.item_rank
        } else {
            R.layout.item_score
        }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val item = getItem(position)

        with(holder) {
            val context = itemView.context

            if (item is Rank) {
                holder.position!!.text = context.getString(R.string.header_rank, item.position)
            }

            if (item is Score) {
                holder.count!!.text = context.getString(R.string.egg_count, item.count)
                holder.description!!.text = item.hunterDescription
                holder.medal!!.text = item.medal
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return ScoreViewHolder(view)
    }
}