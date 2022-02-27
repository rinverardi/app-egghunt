package app.egghunt.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import app.egghunt.R

class HunterDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_hunter_details)

        // Initialize the properties.

        findViewById<TextView>(R.id.competitionDescription).text =
            intent.getStringExtra(EXTRA_COMPETITION_DESCRIPTION)

        findViewById<TextView>(R.id.competitionTag).text =
            intent.getStringExtra(EXTRA_COMPETITION_TAG)

        findViewById<TextView>(R.id.hunterDescription).text =
            intent.getStringExtra(EXTRA_HUNTER_DESCRIPTION)

        findViewById<TextView>(R.id.hunterTag).text =
            intent.getStringExtra(EXTRA_HUNTER_TAG)
    }

    companion object {
        const val EXTRA_COMPETITION_DESCRIPTION = "cd"
        const val EXTRA_COMPETITION_TAG = "ct"
        const val EXTRA_HUNTER_DESCRIPTION = "hd"
        const val EXTRA_HUNTER_TAG = "ht"
    }
}