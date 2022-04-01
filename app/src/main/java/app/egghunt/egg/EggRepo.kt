package app.egghunt.egg

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.egghunt.lib.Keys
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference

object EggRepo {
    fun bind(competition: DatabaseReference, recycler: RecyclerView): EggAdapter {

        // Assign the layout manager.

        recycler.layoutManager = LinearLayoutManager(recycler.context)

        // Assign the adapter.

        val query = competition.child(Keys.EGG).orderByChild(Keys.DESCRIPTION)

        val adapter = EggAdapter(
            FirebaseRecyclerOptions.Builder<Egg>()
                .setQuery(query, Egg::class.java)
                .build()
        )

        recycler.adapter = adapter

        adapter.startListening()

        return adapter
    }
}
