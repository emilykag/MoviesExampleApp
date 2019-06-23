package com.example.moviesapp.ui.search

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration

import com.example.moviesapp.R
import com.example.moviesapp.api.SearchResult
import com.example.moviesapp.api.Status
import com.example.moviesapp.di.Injectable
import com.example.moviesapp.ui.details.DetailsFragment
import com.example.moviesapp.util.extensions.addFragmentBackStack
import com.example.moviesapp.util.extensions.hideKeyboard
import com.example.moviesapp.util.extensions.showToast
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var searchViewModel: SearchViewModel

    private var adapter: SearchResultsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SearchViewModel::class.java)

        editTextSearchMovie.setOnEditorActionListener { _: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                activity?.hideKeyboard()
                doSearch()
                true
            } else {
                false
            }
        }

        context?.let {
            if (isAdded) {
                adapter = SearchResultsAdapter(
                    it,
                    object : OnSearchResultClickListener {
                        override fun onClick(searchResult: SearchResult?) {
                            searchResult?.let { item ->

                                if (item.type == SearchResult.Type.MOVIE ||
                                    item.type == SearchResult.Type.TV
                                ) {
                                    activity?.let { fragAct ->
                                        (fragAct as AppCompatActivity)
                                            .addFragmentBackStack(
                                                DetailsFragment::class.java.name,
                                                R.id.container
                                            ) {
                                                DetailsFragment.newInstance(item)
                                            }
                                    }
                                } else {
                                    showToast(
                                        getString(
                                            R.string.operation_not_supported_for_media_type,
                                            item.type.toString()
                                        )
                                    )
                                }
                            }

                        }
                    })
                recyclerViewSearchResults.addItemDecoration(
                    DividerItemDecoration(
                        it,
                        DividerItemDecoration.VERTICAL
                    )
                )
                recyclerViewSearchResults.adapter = adapter
            }
        }

        // search with the letter a in order to show some results at first launch
        editTextSearchMovie.setText("a")
        doSearch()
    }

    private fun doSearch() {
        searchViewModel.search(editTextSearchMovie.text.toString())
            .observe(this, Observer {
                when (it) {
                    Status.LOADING -> {
                        progressBarMore.isVisible = true
                    }
                    else -> {
                        progressBarMore.isVisible = false
                    }
                }
            })
        searchViewModel.getSearchResultLiveData()
            .observe(this, Observer {
                adapter?.submitList(it)
            })
    }

    companion object {

        fun newInstance(): SearchFragment =
            SearchFragment()
    }
}
