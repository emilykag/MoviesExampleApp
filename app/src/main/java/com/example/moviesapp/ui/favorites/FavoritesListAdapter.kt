package com.example.moviesapp.ui.favorites

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
import com.example.moviesapp.db.entities.Favorite
import kotlinx.android.synthetic.main.favorite_item.view.*

class FavoritesListAdapter(
    private val context: Context,
    private val listener: OnFavoritesClickListener
) : PagedListAdapter<Favorite, FavoritesListAdapter.FavoritesVH>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.favorite_item, parent, false)

        return FavoritesVH(view)
    }

    override fun onBindViewHolder(holder: FavoritesVH, position: Int) {
        val item = getItem(position)

        Glide
            .with(context)
            .load(item?.getFavImageUrl())
            .placeholder(R.drawable.ic_not_available)
            .into(holder.imageViewFavorite)

        holder.textViewFavoriteTitle.text = item?.title
        holder.textViewFavoriteYear.text = item?.releaseDate
    }

    inner class FavoritesVH(view: View) : RecyclerView.ViewHolder(view) {

        val imageViewFavorite: ImageView = view.imageViewFavorite
        val textViewFavoriteTitle: TextView = view.textViewFavoriteTitle
        val textViewFavoriteYear: TextView = view.textViewFavoriteYear

        init {
            view.setOnClickListener {
                listener.onClickFavorite(getItem(layoutPosition))
            }
        }
    }

    companion object {
        var DIFF_CALLBACK: DiffUtil.ItemCallback<Favorite> = object : DiffUtil.ItemCallback<Favorite>() {
            override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem.movieId == newItem.movieId
            }

            override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem == newItem
            }
        }
    }
}

interface OnFavoritesClickListener {
    fun onClickFavorite(favorite: Favorite?)
}