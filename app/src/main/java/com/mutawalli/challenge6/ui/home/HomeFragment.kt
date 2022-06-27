package com.mutawalli.challenge6.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mutawalli.challenge6.MyApplication
import com.mutawalli.challenge6.R
import com.mutawalli.challenge6.databinding.FragmentHomeBinding
import com.mutawalli.challenge6.datastore.UserDataStoreManager
import com.mutawalli.challenge6.resource.Status
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            HomeViewModelFactory.getInstance(
                view.context,
                (activity?.application as MyApplication).repository,
                UserDataStoreManager(view.context)
            )
        )[HomeViewModel::class.java]

        viewModel.getIdUser().observe(viewLifecycleOwner) {
            viewModel.userData(it)
        }

        val adapter = HomeAdapter()
        binding.rvMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvMovies.adapter = adapter

        viewModel.userData.observe(viewLifecycleOwner) { user ->
            when (user.status) {
                Status.SUCCESS -> {
                    if (user.data != null) {
                        binding.tvWelcomeHome.text = getString(R.string.usernamePlaceholder, user?.data.username)
                    } else {
                        Snackbar.make(
                            binding.root,
                            "User Tidak Ditemukan",
                            Snackbar.LENGTH_LONG
                        )
                            .show()
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), user.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        viewModel.popular.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            Log.d("HomeFragment", it.toString())
        }

        viewModel.errorStatus.observe(viewLifecycleOwner) { text ->
            text?.let {
                Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show()
                viewModel.onSnackbarShown()
            }
        }

        binding.ivProfileHome.setOnClickListener() {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}