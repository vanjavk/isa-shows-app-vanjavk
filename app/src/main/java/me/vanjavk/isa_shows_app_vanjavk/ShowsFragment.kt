package me.vanjavk.isa_shows_app_vanjavk

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentShowsBinding
import me.vanjavk.isa_shows_app_vanjavk.model.Show
import me.vanjavk.isa_shows_app_vanjavk.viewmodel.ShowsViewModel

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null

    private val binding get() = _binding!!

    private var showsAdapter: ShowsAdapter? = null

    private val showsViewModel: ShowsViewModel by navGraphViewModels(R.id.mainGraph)

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

        showsViewModel.initShows()

        initShowsRecycler()

        showsViewModel.getShowsLiveData().observe(requireActivity(), { shows ->
            updateItems(shows)
        })

        initLogoutButton()
    }

    private fun updateItems(shows: List<Show>) {
        showsAdapter?.setItems(shows)
    }

    private fun initLogoutButton() {
        binding.logoutButton.setOnClickListener {
            val sharedPref =
                activity?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener
            with(sharedPref.edit()) {
                putBoolean(
                    getString(me.vanjavk.isa_shows_app_vanjavk.R.string.logged_in_key),
                    false
                )
                apply()
            }
            ShowsFragmentDirections.actionLogout()
                .let { findNavController().navigate(it) }
        }
    }

    private fun initShowsRecycler() {
        showsAdapter = ShowsAdapter(emptyList()) { item ->
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@ShowsAdapter
            val email = sharedPref.getString(getString(R.string.user_email_key), "Default_user").orEmpty()

            ShowsFragmentDirections.actionShowToDetails(
                email, item.ID
            )
                .let { findNavController().navigate(it) }
        }

        binding.showsRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.showsRecyclerView.adapter = showsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}