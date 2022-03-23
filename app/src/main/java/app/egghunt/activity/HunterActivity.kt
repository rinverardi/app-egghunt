package app.egghunt.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.egghunt.R

class HunterActivity : AppCompatActivity() {
    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        setContentView(R.layout.activity_hunter)
    }

    companion object {
        const val EXTRA_COMPETITION_DESCRIPTION = "competition_description"
        const val EXTRA_COMPETITION_TAG = "competition_tag"
        const val EXTRA_HUNTER_DESCRIPTION = "hunter_description"
        const val EXTRA_HUNTER_TAG = "hunter_tag"
    }
}
