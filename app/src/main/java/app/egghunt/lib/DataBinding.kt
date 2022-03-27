package app.egghunt.lib

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference

object DataBinding {
    fun bindEggs(competitionReference: DatabaseReference, recycler: RecyclerView): EggAdapter {

        // Assign the layout manager.

        recycler.layoutManager = LinearLayoutManager(recycler.context)

        // Assign the adapter.

        val query = competitionReference.child("egg")

        val adapter = EggAdapter(FirebaseRecyclerOptions.Builder<Egg>()
            .setQuery(query, Egg::class.java)
            .build())

        recycler.adapter = adapter

        adapter.startListening()

        return adapter
    }

    fun bindHints(competitionReference: DatabaseReference, recycler: RecyclerView): HintAdapter {

        // Assign the layout manager.

        recycler.layoutManager = LinearLayoutManager(recycler.context)

        // Assign the adapter.

        val query = competitionReference.child("hint").orderByChild("order")

        val adapter = HintAdapter(FirebaseRecyclerOptions.Builder<Hint>()
            .setQuery(query, Hint::class.java)
            .build())

        recycler.adapter = adapter

        adapter.startListening()

        return adapter
    }
}
