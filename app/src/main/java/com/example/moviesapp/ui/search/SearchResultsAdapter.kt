package com.example.moviesapp.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.api.SearchResult
import kotlinx.android.synthetic.main.search_result_item.view.*

class SearchResultsAdapter(
    private val context: Context,
    private val listener: OnSearchResultClickListener
) : PagedListAdapter<SearchResult, SearchResultsAdapter.SearchResultsVH>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.search_result_item, parent, false)

        return SearchResultsVH(view)
    }

    override fun onBindViewHolder(holder: SearchResultsVH, position: Int) {
        val item = getItem(position)

        Glide
            .with(context)
            .load(item?.getThumbnailUrl())
            .placeholder(R.drawable.ic_not_available)
            .into(holder.imageViewSearchItemThumb)

        holder.textViewSearchItemTitle.text = item?.title
        holder.textViewSearchItemDate.text = item?.getYear()
        holder.textViewSearchItemType.setText(item?.type?.res!!)
        holder.textViewSearchItemRating.text = item?.getDisplayRating()
    }

    inner class SearchResultsVH(view: View) : RecyclerView.ViewHolder(view) {

        val imageViewSearchItemThumb: ImageView = view.imageViewSearchItemThumb
        val textViewSearchItemTitle: TextView = view.textViewSearchItemTitle
        val textViewSearchItemDate: TextView = view.textViewSearchItemDate
        val textViewSearchItemType: TextView = view.textViewSearchItemType
        val textViewSearchItemRating: TextView = view.textViewSearchItemRating

        init {
            view.setOnClickListener {
                listener.onClickSearchResult(getItem(layoutPosition))
            }
        }
    }

    companion object {
        var DIFF_CALLBACK: DiffUtil.ItemCallback<SearchResult> = object : DiffUtil.ItemCallback<SearchResult>() {
            override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
                return oldItem == newItem
            }
        }
    }
}

interface OnSearchResultClickListener {
    fun onClickSearchResult(searchResult: SearchResult?)
}