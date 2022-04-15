package app.egghunt.egg

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.egghunt.lib.Keys
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query

object EggRepo {
    fun bind(competition: DatabaseReference, recycler: RecyclerView): EggAdapter {
        val adapter = EggAdapter(
            FirebaseRecyclerOptions.Builder<Egg>()
                .setQuery(makeQuery(competition), Egg::class.java)
                .build()
        )

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(recycler.context)

        adapter.startListening()

        return adapter
    }

    private fun makeQuery(competition: DatabaseReference): Query =
        competition.child(Keys.EGG).orderByChild(Keys.DESCRIPTION)
}
