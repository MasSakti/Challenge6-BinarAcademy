package com.mutawalli.challenge6.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mutawalli.challenge6.R
import com.mutawalli.challenge6.data.local.UserRepository
import com.mutawalli.challenge6.databinding.FragmentLoginBinding
import com.mutawalli.challenge6.datastore.UserDataStoreManager
import com.mutawalli.challenge6.resource.Status

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repos = UserRepository.getInstance(view.context)
        val pref = UserDataStoreManager(view.context)

        viewModel = ViewModelProvider(
            requireActivity(),
            LoginViewModelFactory(repos!!, pref)
        )[LoginViewModel::class.java]

        binding.btnLogin.setOnClickListener {

            viewModel.login(
                binding.etEmailLogin.text.toString(),
                binding.etPasswordLogin.text.toString()
            )

            viewModel.loginStatus.observe(viewLifecycleOwner) { user ->
                when (user.status) {
                    Status.SUCCESS -> {
                        if (user.data != null) {
                            viewModel.saveUserDataStore(true, user.data.id)
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        } else {
                            Snackbar.make(
                                binding.root,
                                "User Tidak Ditemukan",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), user.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }

        binding.tvCreate.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}