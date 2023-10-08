package com.example.mortal_combat

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mortal_combat.databinding.FragmentEndBinding
import com.example.mortal_combat.databinding.FragmentStartBinding


class StartFragment : Fragment() {
    private lateinit var binding: FragmentStartBinding
private lateinit var melody: MediaPlayer
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        melody = MediaPlayer.create(context, R.raw.melody)
        melody.start()
        binding.buttonStart.setOnClickListener {
            melody.stop()
            val action =
                StartFragmentDirections.actionStartFragmentToFirstFragment()
            findNavController().navigate(action)
        }
    }

}