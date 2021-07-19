package me.vanjavk.isa_shows_app_vanjavk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentShowsBinding

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    val args: ShowsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.root.setOnClickListener {
//            findNavController().navigate(R.id.action_second_to_third)
//        }
//        binding.test.doOnTextChanged { text, start, before, count ->  }
//        Toast.makeText(context, "Poslani broj je ${args.myArg}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}