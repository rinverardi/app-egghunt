package app.egghunt.lib

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
        viewHolder.description.text = egg.description
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EggViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_egg, parent, false)

        return EggViewHolder(view)
    }
}