package com.example.moviesapp.ui.search

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration

import com.example.moviesapp.R
import com.example.moviesapp.api.SearchResult
import com.example.moviesapp.api.Status
import com.example.moviesapp.di.Injectable
import com.example.moviesapp.ui.favorites.FavoritesActivity
import com.example.moviesapp.util.extensions.*
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

private const val SEARCH_QUERY_ARG = "SEARCH_QUERY"

class SearchFragment : Fragment(), Injectable, OnSearchResultClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SearchViewModel

    private var twoPane: Boolean = false

    private var adapter: SearchResultsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SearchViewModel::class.java)

        if (detailsTabContainer != null) {
            twoPane = true
        }

        editTextSearchMovie.setOnEditorActionListener { _: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                activity?.hideKeyboard()
                doSearch(editTextSearchMovie.text.toString())
                true
            } else {
                false
            }
        }

        imageButtonViewWatchlist.setOnClickListener {
            context?.let { ctx ->
                startActivity(
                    FavoritesActivity.intentFor(ctx)
                )
            }
        }

        setUpRecyclerView()

        // search with the letter a in order to show some results at first launch
        editTextSearchMovie.setText("a")
        val searchQuery = savedInstanceState?.getString(SEARCH_QUERY_ARG) ?: editTextSearchMovie.text.toString()
        doSearch(searchQuery)
    }

    override fun onClickSearchResult(searchResult: SearchResult?) {
        searchResult?.let { item ->
            showDetailsUi(twoPane, item.id, item.type, item.image)
        }
    }

    private fun doSearch(searchValue: String) {
        viewModel.search(searchValue)
    }

    private fun setUpRecyclerView() {
        context?.let {
            if (isAdded) {
                adapter = SearchResultsAdapter(it, this)
                recyclerViewSearchResults.addItemDecoration(
                    DividerItemDecoration(
                        it,
                        DividerItemDecoration.VERTICAL
                    )
                )
                recyclerViewSearchResults.adapter = adapter
            }
        }

        viewModel.getStatus()
            .observe(viewLifecycleOwner, Observer {
                when (it) {
                    Status.LOADING -> {
                        progressBarMore.isVisible = true
                    }
                    else -> {
                        progressBarMore.isVisible = false
                    }
                }
            })
        viewModel.getSearchResultLiveData()
            .observe(viewLifecycleOwner, Observer {
                adapter?.submitList(it)
            })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY_ARG, viewModel.getSearchQuery())
    }

    companion object {

        fun newInstance(): SearchFragment =
            SearchFragment()
    }
}
