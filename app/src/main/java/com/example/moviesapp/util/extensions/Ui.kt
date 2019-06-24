package com.example.moviesapp.util.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

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
        .add(container, fragment, tag)
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