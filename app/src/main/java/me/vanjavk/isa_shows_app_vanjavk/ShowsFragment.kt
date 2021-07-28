package me.vanjavk.isa_shows_app_vanjavk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentShowsBinding

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null

    private val binding get() = _binding!!

    private val args: ShowsFragmentArgs by navArgs()

    private var showsAdapter: ShowsAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initShowsRecycler()

        initShowHideButton()

        initLogoutButton()
    }

    private fun initLogoutButton() {
        binding.logoutButton.setOnClickListener {
            ShowsFragmentDirections.actionLogout()
                .let { findNavController().navigate(it) }
        }
    }

    private fun initShowHideButton() {
        binding.showHideButton.setOnClickListener {
            binding.showsRecyclerView.apply {
                isVisible = !isVisible
            }
        }
    }

    private fun initShowsRecycler() {
        showsAdapter = ShowsAdapter(emptyList()) { item ->
            ShowsFragmentDirections.actionShowToDetails(args.email, item.ID)
                .let { findNavController().navigate(it) }
        }

        binding.showsRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.showsRecyclerView.adapter = showsAdapter

        showsAdapter?.setItems(shows)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}