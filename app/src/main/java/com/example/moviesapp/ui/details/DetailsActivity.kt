package com.example.moviesapp.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.moviesapp.R
import com.example.moviesapp.db.entities.ShowType
import com.example.moviesapp.util.extensions.addFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class DetailsActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        addFragment(DetailsFragment::class.java.name, R.id.detailsContainer) {
            DetailsFragment.newInstance(
                intent.getIntExtra(SHOW_ID_ARG, 0),
                intent.getSerializableExtra(SHOW_TYPE_ARG) as ShowType,
                intent.getStringExtra(SHOW_THUMB_IMG_ARG)
            )
        }
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    companion object {

        fun intentFor(
            context: Context,
            showId: Int,
            type: ShowType,
            showImg: String?,
            twoPane: Boolean = false
        ): Intent =
            Intent(context, DetailsActivity::class.java).apply {
                putExtra(SHOW_ID_ARG, showId)
                putExtra(SHOW_TYPE_ARG, type)
                putExtra(SHOW_THUMB_IMG_ARG, showImg)
                putExtra(TWO_PANE_ARG, twoPane)
            }
    }
}
