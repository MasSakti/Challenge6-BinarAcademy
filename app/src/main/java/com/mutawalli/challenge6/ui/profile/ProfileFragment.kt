package com.mutawalli.challenge6.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mutawalli.challenge6.R
import com.mutawalli.challenge6.data.local.UserEntity
import com.mutawalli.challenge6.databinding.FragmentProfileBinding
import com.mutawalli.challenge6.datastore.UserDataStoreManager
import com.mutawalli.challenge6.resource.Status
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = UserDataStoreManager(view.context)
        val factory = ProfileViewModelProvider.getInstance(view.context, pref)
        viewModel = ViewModelProvider(requireActivity(), factory)[ProfileViewModel::class.java]

        val userData = UserEntity()

        viewModel.getIdUser().observe(viewLifecycleOwner) {
            viewModel.userData(it)
        }

        viewModel.userData.observe(viewLifecycleOwner) { user ->
            when (user.status) {
                Status.SUCCESS -> {
                    if (user.data != null) {

                        userData.id = user.data.id

                        userData.password = user.data.password

                        userData.fullname = user.data.fullname
                        binding.etNameProfile.setText(user.data.fullname)

                        userData.username = user.data.username
                        binding.etUsernameProfile.setText(user.data.username)

                        userData.ttl = user.data.ttl
                        binding.etBirthProfile.setText(user.data.ttl)

                        userData.address = user.data.address
                        binding.etAddresProfile.setText(user.data.address)

                        userData.email = user.data.email
                        binding.etEmailProfile.setText(user.data.email)

                        userData.image = user.data.image
                        val uriImage = Uri.parse(user.data.image)
                        binding.ivProfile.setImageURI(uriImage)
                        Glide.with(binding.root).load(user.data.image)
                            .circleCrop()
                            .into(binding.ivProfile)
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

        binding.ivBacktoList2.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)

        }

        binding.btnLogoutProfile.setOnClickListener {
            val dialog = AlertDialog.Builder(view.context)
            dialog.setTitle("Logout")
            dialog.setMessage("Apakah Anda Yakin Ingin Logout ?")
            dialog.setPositiveButton("Yakin") { _, _ ->
                Snackbar.make(binding.root, "User Berhasil Logout", Snackbar.LENGTH_LONG)
                    .show()
                viewModel.clearDataUser()
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }

            dialog.setNegativeButton("Batal") { listener, _ ->
                listener.dismiss()
            }

            dialog.show()
        }
        binding.btnUpdateProfile.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToUpdateProfileFragment(
                    userData
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}