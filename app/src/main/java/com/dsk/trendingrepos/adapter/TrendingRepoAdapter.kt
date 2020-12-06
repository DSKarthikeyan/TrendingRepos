package com.dsk.trendingrepos.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dsk.trendingrepos.R
import com.dsk.trendingrepos.data.model.RepoDetails
import kotlinx.android.synthetic.main.repo_detail_view.view.*
import java.util.*
import kotlin.collections.ArrayList


class TrendingRepoAdapter :
    RecyclerView.Adapter<TrendingRepoAdapter.TrendingRepoHolder>(), Filterable {

    var trendingRepoFilterList = ArrayList<RepoDetails>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingRepoHolder {
        return TrendingRepoHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.repo_detail_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TrendingRepoHolder, position: Int) {
        val currentList = differ.currentList[position]

        holder.itemView.apply {
            val mDrawable = ContextCompat.getDrawable(context, R.drawable.solid_circle)!!.mutate()
            mDrawable.colorFilter = PorterDuffColorFilter(Color.parseColor(currentList.languageColor), PorterDuff.Mode.SRC_IN)
            imageViewLanguageLogo.setImageDrawable(mDrawable)
            imageViewRepoStar.setImageResource(R.drawable.ic_star_24)
            Glide.with(holder.itemView)
                .asBitmap()
                .override(50, 50)
                .load(currentList.avatar)
                .into(imageViewAvatar)

            textViewAuthor.text = currentList.author
            textViewLanguage.text = currentList.language
            textViewDescription.text = currentList.description
            textViewProjectName.text = currentList.name
            textViewRepoCount.text = currentList.stars.toString()
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class TrendingRepoHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<RepoDetails>() {
        override fun areItemsTheSame(oldItem: RepoDetails, newItem: RepoDetails): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: RepoDetails, newItem: RepoDetails): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                trendingRepoFilterList = if (charSearch.isEmpty()) {
                    trendingRepoFilterList
                } else {
                    val resultList = ArrayList<RepoDetails>()
                    for (row in differ.currentList) {
                        if (row.name.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = trendingRepoFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                if (results.values != null) {
                    trendingRepoFilterList = results.values!! as ArrayList<RepoDetails>
                    if (trendingRepoFilterList.isNotEmpty() && trendingRepoFilterList.size > 0) {
                        differ.submitList(trendingRepoFilterList)
                    }
                }else{

                }
            }
        }
    }

}