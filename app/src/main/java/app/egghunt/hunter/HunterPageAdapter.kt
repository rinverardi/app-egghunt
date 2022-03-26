package app.egghunt.hunter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import app.egghunt.R

internal class HunterPageAdapter(private val activity: HunterActivity) :
    RecyclerView.Adapter<HunterPageHolder>() {

    override fun getItemCount(): Int = 2

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> R.layout.tab_eggs
        1 -> R.layout.tab_scores
        else -> throw NotImplementedError()
    }

    private fun initializeButtonFind(tab: View) {
        val button: Button? = tab.findViewById(R.id.button_find)

        button?.setOnClickListener {
            activity.scan()
        }
    }

    private fun initializeButtonHide(tab: View) {
        val button: Button? = tab.findViewById(R.id.button_hide)

        button?.visibility = View.GONE
    }

    override fun onBindViewHolder(holder: HunterPageHolder, position: Int) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HunterPageHolder {
        val tab: View = LayoutInflater.from(activity).inflate(viewType, parent, false)

        initializeButtonFind(tab)
        initializeButtonHide(tab)

        return HunterPageHolder(tab)
    }
}