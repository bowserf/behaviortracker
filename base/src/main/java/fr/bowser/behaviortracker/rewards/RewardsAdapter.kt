package fr.bowser.behaviortracker.rewards

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.bowser.behaviortracker.inapp.InApp
import fr.bowser.behaviortracker.rewardsrow.RewardsRowView

class RewardsAdapter : RecyclerView.Adapter<RewardsAdapter.RewardsViewHolder>() {

    private val inAppList: MutableList<InApp> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardsViewHolder {
        val rewardsRowView = RewardsRowView(parent.context)

        val layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        rewardsRowView.layoutParams = layoutParams

        return RewardsViewHolder(rewardsRowView)
    }

    override fun getItemCount(): Int {
        return inAppList.size
    }

    override fun onBindViewHolder(holder: RewardsViewHolder, position: Int) {
        val inApp = inAppList[position]
        holder.view.setInApp(inApp)
    }

    fun setInAppList(inAppList: List<InApp>) {
        this.inAppList.clear()
        this.inAppList.addAll(inAppList)
        notifyDataSetChanged()
    }

    inner class RewardsViewHolder(val view: RewardsRowView) : RecyclerView.ViewHolder(view)
}
