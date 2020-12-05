package com.dsk.trendingrepos.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dsk.trendingrepos.R
import com.dsk.trendingrepos.data.model.RepoDetails
import kotlinx.android.synthetic.main.repo_detail_view.view.*


class TrendingRepoAdapter :
    RecyclerView.Adapter<TrendingRepoAdapter.TrendingRepoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingRepoHolder {
        return TrendingRepoHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.repo_detail_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TrendingRepoHolder, position: Int) {
        val currentList = differ.currentList[position]

        holder.itemView.apply {
            val color: Int = Color.parseColor(currentList.languageColor)
            (imageViewLanguageLogo.background as GradientDrawable).setColor(color)
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
}