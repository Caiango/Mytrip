package com.example.mytrip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.fragment_help.*


class HelpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_help, container, false)
        val buthelp: Button = root.findViewById(R.id.buttonHelp)
        buthelp.setOnClickListener {
            getActivity()?.getSupportFragmentManager()?.beginTransaction()?.remove(this)?.commit()
        }
        return root
    }
}