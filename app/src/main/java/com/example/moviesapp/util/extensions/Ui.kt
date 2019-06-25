package com.example.moviesapp.util.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.moviesapp.R
import com.example.moviesapp.db.entities.ShowType
import com.example.moviesapp.ui.details.DetailsActivity
import com.example.moviesapp.ui.details.DetailsFragment

fun AppCompatActivity.addFragment(
    tag: String,
    container: Int,
    ignoreBackStack: Boolean = false,
    newInstance: () -> Fragment
) {
    val fragment = if (ignoreBackStack) {
        newInstance()
    } else {
        supportFragmentManager.findFragmentByTag(tag) ?: newInstance()
    }
    supportFragmentManager
        .beginTransaction()
        .replace(container, fragment, tag)
        .commit()
}

fun AppCompatActivity.addFragmentBackStack(
    tag: String,
    container: Int,
    ignoreBackStack: Boolean = false,
    newInstance: () -> Fragment
) {
    val fragment = if (ignoreBackStack) {
        newInstance()
    } else {
        supportFragmentManager.findFragmentByTag(tag) ?: newInstance()
    }
    supportFragmentManager
        .beginTransaction()
        .replace(container, fragment, tag)
        .addToBackStack(tag)
        .commit()
}

fun Activity.hideKeyboard() {
    currentFocus?.run {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also {
            it.hideSoftInputFromWindow(windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
        }
    }
}

fun Fragment.showToast(message: String?, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(context, message, duration).show()

fun Fragment.showDetailsUi(twoPane: Boolean, movieId: Int, type: ShowType, image: String?) {
    if (type == ShowType.MOVIE || type == ShowType.TV) {
        activity?.let { fragAct ->
            if (twoPane) {
                (fragAct as AppCompatActivity)
                    .addFragment(
                        DetailsFragment::class.java.name,
                        R.id.detailsTabContainer,
                        true
                    ) {
                        DetailsFragment.newInstance(movieId, type, image, twoPane)
                    }
            } else {
                fragAct.startActivity(
                    DetailsActivity.intentFor(fragAct, movieId, type, image, twoPane)
                )
            }
        }
    } else {
        showToast(
            getString(
                R.string.operation_not_supported_for_media_type,
                type.toString()
            )
        )
    }
}