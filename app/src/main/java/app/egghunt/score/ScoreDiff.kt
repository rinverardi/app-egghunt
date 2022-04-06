package app.egghunt.score

import androidx.recyclerview.widget.DiffUtil

object ScoreDiff : DiffUtil.ItemCallback<Any>() {
    override fun areContentsTheSame(old: Any, new: Any): Boolean =
        if (old is Rank && new is Rank) {
            true
        } else if (old is Score && new is Score) {
            old.count == new.count &&
                    old.hunterDescription == new.hunterDescription &&
                    old.hunterTag == new.hunterTag &&
                    old.medal == new.medal
        } else {
            false
        }

    override fun areItemsTheSame(old: Any, new: Any): Boolean =
        if (old is Rank && new is Rank) {
            old.position == new.position
        } else if (old is Score && new is Score) {
            old.hunterTag == new.hunterTag
        } else {
            false
        }
}