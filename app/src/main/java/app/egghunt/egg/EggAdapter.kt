package app.egghunt.egg

import android.content.Context
import android.text.TextUtils
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.egghunt.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class EggAdapter(options: FirebaseRecyclerOptions<Egg>) :
    FirebaseRecyclerAdapter<Egg, EggViewHolder>(options) {

    override fun onBindViewHolder(
        viewHolder: EggViewHolder,
        position: Int,
        egg: Egg
    ) {
        val context = viewHolder.itemView.context

        if (TextUtils.isEmpty(egg.description))
            viewHolder.description.text = context.getString(R.string.type_egg)
        else
            viewHolder.description.text = egg.description

        if (egg.timeFound == null) {
            viewHolder.timeFound.visibility = View.GONE
        } else {
            viewHolder.timeFound.text = sayFound(context, egg)
            viewHolder.timeFound.visibility = View.VISIBLE
        }

        if (egg.timeFound != null || egg.timeHidden == null) {
            viewHolder.timeHidden.visibility = View.GONE
        } else {
            viewHolder.timeHidden.text = sayHidden(context, egg)
            viewHolder.timeHidden.visibility = View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EggViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_egg, parent, false)

        return EggViewHolder(view)
    }

    private fun sayFound(context: Context, egg: Egg): String {
        val time = DateUtils.formatDateTime(context, egg.timeFound!!, DateUtils.FORMAT_SHOW_TIME)

        return if (egg.hunterDescription == null) {
            context.getString(R.string.time_found, time)
        } else {
            context.getString(R.string.time_found_by, time, egg.hunterDescription)
        }
    }

    private fun sayHidden(context: Context, egg: Egg): String = context.getString(
        R.string.time_hidden,
        DateUtils.formatDateTime(context, egg.timeHidden!!, DateUtils.FORMAT_SHOW_TIME)
    )
}