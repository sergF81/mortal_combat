package com.example.mortal_combat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mortal_combat.databinding.FragmentEndBinding


class EndFragment : Fragment() {
    private lateinit var binding: FragmentEndBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEndBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val argsTextToEnd: EndFragmentArgs  by navArgs()

        binding.textViewMessageEnd.text = argsTextToEnd.textToEnd
        binding.buttonRestart.setOnClickListener {
            val action =
                EndFragmentDirections.actionEndFragmentToFirstFragment()
            findNavController().navigate(action)
        }
    }

}