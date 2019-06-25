package com.example.moviesapp.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import com.example.moviesapp.R
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviesapp.db.entities.Favorite
import com.example.moviesapp.di.Injectable
import com.example.moviesapp.util.extensions.showDetailsUi
import kotlinx.android.synthetic.main.fragment_favorites_list.*
import javax.inject.Inject

class FavoritesListFragment : Fragment(), Injectable, OnFavoritesClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: FavoritesListViewModel

    private var twoPane: Boolean = false

    private var adapter: FavoritesListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(FavoritesListViewModel::class.java)

        if (detailsTabContainer != null) {
            twoPane = true
        }

        setUpRecyclerView()
        loadFavorites()
    }

    override fun onClickFavorite(favorite: Favorite?) {
        favorite?.let { item ->
            showDetailsUi(twoPane, item.movieId, item.type, item.image)
        }
    }

    private fun loadFavorites() {
        viewModel.loadFavorites().observe(viewLifecycleOwner, Observer {
            adapter?.submitList(it)
            textViewNoFavorites.isVisible = it.isNullOrEmpty()
        })
    }

    private fun setUpRecyclerView() {
        context?.let {
            if (isAdded) {
                adapter = FavoritesListAdapter(it, this)
                val gridColumns = resources.getInteger(R.integer.favorites_grid_columns)
                recyclerViewFavorites.layoutManager = GridLayoutManager(it, gridColumns)
                recyclerViewFavorites.adapter = adapter
            }
        }
    }

    companion object {

        fun newInstance(): FavoritesListFragment =
            FavoritesListFragment()
    }
}
