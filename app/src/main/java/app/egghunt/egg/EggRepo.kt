package app.egghunt.egg

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.egghunt.lib.Keys
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query

object EggRepo {
    fun bind(
        competition: DatabaseReference,
        listener: EggListener?,
        recycler: RecyclerView
    ): EggAdapter {
        val adapter = EggAdapter(
            listener,
            FirebaseRecyclerOptions.Builder<Egg>()
                .setQuery(makeQuery(competition), makeParser())
                .build()
        )

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(recycler.context)

        adapter.startListening()

        return adapter
    }

    private fun makeParser(): SnapshotParser<Egg> = SnapshotParser<Egg> {
        it.getValue(Egg::class.java)!!.apply { tag = it.key }
    }

    private fun makeQuery(competition: DatabaseReference): Query =
        competition.child(Keys.EGG).orderByChild(Keys.DESCRIPTION)
}
