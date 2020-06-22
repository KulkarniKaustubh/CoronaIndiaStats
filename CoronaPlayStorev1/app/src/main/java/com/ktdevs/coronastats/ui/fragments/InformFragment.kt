package com.ktdevs.coronastats.ui.fragments

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ktdevs.coronastats.R

class InformFragment : Fragment(R.layout.inform_fragment){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var githubLink : TextView = view.findViewById(R.id.github_link)
        githubLink.movementMethod = LinkMovementMethod.getInstance()
    }

}