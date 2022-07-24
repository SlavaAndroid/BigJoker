package com.im30.ROE.g.ui.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.im30.ROE.g.R
import com.im30.ROE.g.databinding.ActivityJokerBinding


class JokerActivity: AppCompatActivity()  {

    private lateinit var binding: ActivityJokerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJokerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(JokerStartFragment.newInstance())
    }
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount < 2) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .apply {
                if (addToBackStack)
                    addToBackStack(null)
            }
            .commit()
    }
}