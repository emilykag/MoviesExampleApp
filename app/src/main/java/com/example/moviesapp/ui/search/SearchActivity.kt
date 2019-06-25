package com.example.moviesapp.ui.search

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.moviesapp.R
import com.example.moviesapp.util.Utils
import com.example.moviesapp.util.extensions.addFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        checkInternetConnection()

        addFragment(SearchFragment::class.java.name, R.id.searchContainer) {
            SearchFragment.newInstance()
        }
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    private fun checkInternetConnection() {
        if (!Utils.isNetworkAvailable(this@SearchActivity)) {
            AlertDialog.Builder(this@SearchActivity)
                .setTitle(R.string.check_network_msg)
                .setPositiveButton(R.string.dismiss, null)
                .show()
        }
    }
}
