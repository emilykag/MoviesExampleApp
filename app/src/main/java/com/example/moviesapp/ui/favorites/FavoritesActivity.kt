package com.example.moviesapp.ui.favorites

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.moviesapp.R
import com.example.moviesapp.util.extensions.addFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class FavoritesActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        setTitle(R.string.favorites)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        addFragment(FavoritesListFragment::class.java.name, R.id.favoritesContainer) {
            FavoritesListFragment.newInstance()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    companion object {

        fun intentFor(context: Context): Intent =
            Intent(context, FavoritesActivity::class.java)
    }
}
