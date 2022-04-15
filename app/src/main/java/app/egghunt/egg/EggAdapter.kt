package app.egghunt.egg

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import app.egghunt.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class EggAdapter(private val listener: EggListener?, options: FirebaseRecyclerOptions<Egg>) :
    FirebaseRecyclerAdapter<Egg, EggViewHolder>(options) {

    override fun onBindViewHolder(
        viewHolder: EggViewHolder,
        position: Int,
        egg: Egg
    ) = with(viewHolder) {
        val context = itemView.context

        description.text = if (egg.description.isNullOrEmpty()) {
            context.getString(R.string.egg)
        } else {
            egg.description
        }

        itemView.background = if (egg.timeFound == null) {
            AppCompatResources.getDrawable(context, R.color.white)
        } else {
            AppCompatResources.getDrawable(context, R.color.green)
        }

        if (listener != null) {
            itemView.setOnClickListener { listener.onClick(egg) }
        }

        if (egg.timeFound == null) {
            timeFound.visibility = View.GONE
        } else {
            timeFound.text = sayFound(context, egg)
            timeFound.visibility = View.VISIBLE
        }

        if (egg.timeFound != null || egg.timeHidden == null) {
            timeHidden.visibility = View.GONE
        } else {
            timeHidden.text = sayHidden(context, egg)
            timeHidden.visibility = View.VISIBLE
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