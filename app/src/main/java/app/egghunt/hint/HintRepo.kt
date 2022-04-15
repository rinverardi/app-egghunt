package app.egghunt.hint

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.egghunt.lib.Keys
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query

object HintRepo {
    fun bind(competition: DatabaseReference, recycler: RecyclerView): HintAdapter {
        val adapter = HintAdapter(
            FirebaseRecyclerOptions.Builder<Hint>()
                .setQuery(makeQuery(competition), Hint::class.java)
                .build()
        )

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(recycler.context)

        adapter.startListening()

        return adapter
    }

    private fun makeQuery(competition: DatabaseReference): Query =
        competition.child(Keys.HINT).orderByChild(Keys.ORDER)
}
