package com.dsk.trendingrepos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dsk.trendingrepos.R
import com.dsk.trendingrepos.data.model.RepoDetails

class TrendingRepoAdapter(private val trendingRepoList: List<RepoDetails>) :
    RecyclerView.Adapter<TrendingRepoAdapter.TrendingRepoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingRepoHolder {
        val itemViewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.repo_detail_view, parent, false)
        return TrendingRepoHolder(itemViewHolder)
    }

    override fun onBindViewHolder(holder: TrendingRepoHolder, position: Int) {
        val currentList = trendingRepoList[position]

        Glide.with(holder.itemView)
            .asBitmap()
            .load(currentList.avatar)
            .into(holder.imageViewAvatar)

        holder.imageViewLanguageLogo.setColorFilter(currentList.languageColor.toInt())
        holder.imageViewRepoStar.setImageResource(R.drawable.ic_star_24)

        holder.textViewAuthor.text = currentList.author
        holder.textViewLanguage.text = currentList.language
        holder.textViewDescription.text = currentList.description
        holder.textViewProjectName.text = currentList.name
        holder.textViewRepoCount.text = currentList.stars.toString()
    }

    override fun getItemCount(): Int {
       return trendingRepoList.size
    }

    class TrendingRepoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewAvatar: ImageView = itemView.findViewById(R.id.imageViewAvatar)
        val imageViewLanguageLogo: ImageView = itemView.findViewById(R.id.imageViewLanguageLogo)
        val imageViewRepoStar: ImageView = itemView.findViewById(R.id.imageViewRepoStar)

        val textViewAuthor: TextView = itemView.findViewById(R.id.textViewAuthor)
        val textViewLanguage: TextView = itemView.findViewById(R.id.textViewLanguage)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val textViewProjectName: TextView = itemView.findViewById(R.id.textViewProjectName)
        val textViewRepoCount: TextView = itemView.findViewById(R.id.textViewRepoCount)

    }
}