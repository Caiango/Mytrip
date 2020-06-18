package com.example.mytrip

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.activity_maps.*

/**
 * A simple [Fragment] subclass.
 */
class TransitionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_transition, container, false)
        val butyes: Button = root.findViewById(R.id.button_yes)
        val butno: Button = root.findViewById(R.id.button_no)
        butyes.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
        butno.setOnClickListener {
            getActivity()?.getSupportFragmentManager()?.beginTransaction()?.remove(this)?.commit()
        }
        return root


    }

}
