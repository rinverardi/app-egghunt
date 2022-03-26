package app.egghunt.organizer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import app.egghunt.R

internal class OrganizerPageAdapter(private val activity: OrganizerActivity) :
    RecyclerView.Adapter<OrganizerPageHolder>() {

    override fun getItemCount(): Int = 3

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> R.layout.tab_eggs
        1 -> R.layout.tab_scores
        2 -> R.layout.tab_hints
        else -> throw NotImplementedError()
    }

    private fun initializeButtonFind(tab: View) {
        val button: Button? = tab.findViewById(R.id.button_find)

        button?.visibility = View.GONE
    }

    private fun initializeButtonHide(tab: View) {
        val button: Button? = tab.findViewById(R.id.button_hide)

        button?.setOnClickListener {
            activity.scan()
        }
    }

    override fun onBindViewHolder(holder: OrganizerPageHolder, position: Int) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizerPageHolder {
        val tab: View = LayoutInflater.from(activity).inflate(viewType, parent, false)

        initializeButtonFind(tab)
        initializeButtonHide(tab)

        return OrganizerPageHolder(tab)
    }
}