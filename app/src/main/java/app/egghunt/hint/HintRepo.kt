package app.egghunt.hint

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.egghunt.lib.Keys
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference

object HintRepo {
    fun bind(competition: DatabaseReference, recycler: RecyclerView): HintAdapter {

        // Assign the layout manager.

        recycler.layoutManager = LinearLayoutManager(recycler.context)

        // Assign the adapter.

        val query = competition.child(Keys.HINT).orderByChild(Keys.ORDER)

        val adapter = HintAdapter(
            FirebaseRecyclerOptions.Builder<Hint>()
                .setQuery(query, Hint::class.java)
                .build()
        )

        recycler.adapter = adapter

        adapter.startListening()

        return adapter
    }
}
