package app.egghunt.hint

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.egghunt.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class HintAdapter(options: FirebaseRecyclerOptions<Hint>) :
    FirebaseRecyclerAdapter<Hint, HintViewHolder>(options) {

    private var recycler: RecyclerView? = null

    override fun onAttachedToRecyclerView(recycler: RecyclerView) {
        super.onAttachedToRecyclerView(recycler)

        this.recycler = recycler
    }

    override fun onBindViewHolder(
        viewHolder: HintViewHolder,
        position: Int,
        hint: Hint
    ) = with (viewHolder) {
        val context = itemView.context

        text.text = hint.text
        timePosted.text = sayPosted(context, hint)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HintViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_hint, parent, false)

        return HintViewHolder(view)
    }

    override fun onDataChanged() {
        super.onDataChanged()

        recycler?.scrollToPosition(0)
    }

    override fun onDetachedFromRecyclerView(recycler: RecyclerView) {
        this.recycler = null

        super.onDetachedFromRecyclerView(recycler)
    }

    private fun sayPosted(context: Context, hint: Hint): String {
        val time = if (hint.timePosted == null) {
            ""
        } else {
            DateUtils.formatDateTime(context, hint.timePosted, DateUtils.FORMAT_SHOW_TIME)
        }

        return context.getString(R.string.time_posted, time)
    }
}