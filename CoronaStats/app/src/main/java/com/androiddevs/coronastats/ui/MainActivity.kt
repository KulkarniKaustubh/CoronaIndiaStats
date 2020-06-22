package com.androiddevs.coronastats.ui

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.androiddevs.coronastats.R
import com.androiddevs.coronastats.ui.fragments.IndiaFragment
import com.androiddevs.coronastats.ui.fragments.StateFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Communicator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.setupWithNavController(MainOuterFrag.findNavController())
    }

    override fun passData(position: Int) {
        val bundle = Bundle()
        bundle.putInt("state position", position)
        Log.d("MAIN ACT", "position is : " + position)
        val transaction = supportFragmentManager.beginTransaction()
        val stateFrag = StateFragment()
        stateFrag.arguments = bundle

        transaction.replace(R.id.MainOuterFrag, stateFrag)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }
}

