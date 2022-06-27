package com.mutawalli.challenge6.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.mutawalli.challenge6.MyApplication
import com.mutawalli.challenge6.R
import com.mutawalli.challenge6.databinding.FragmentDetailBinding
import com.mutawalli.challenge6.utils.urlImage
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()

    private val viewModel by viewModels<DetailViewModel>{
        DetailViewModelFactory((activity?.application as MyApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDetail(args.movieId)

        binding.ivBacktoList.setOnClickListener {
            it.findNavController().popBackStack()
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingContainer.visibility = View.VISIBLE
            } else {
                binding.loadingContainer.visibility = View.GONE
            }
        }

        viewModel.detail.observe(viewLifecycleOwner) { it ->

            Glide.with(binding.ivBack)
                .load(urlImage + it?.backdropPath)
                .error(R.drawable.ic_error)
                .into(binding.ivBack)

            Glide.with(binding.ivPosterDetail)
                .load(urlImage + it?.posterPath)
                .error(R.drawable.ic_error)
                .into(binding.ivPosterDetail)

            binding.apply {
                tvTitleDetail.text = it?.title
                tvVoteDetail.text = "from " + it?.voteCount.toString() + " voters"
                tvOverviewDetail.text = it?.overview
                tvRatingDetail.text = "Rating " + it?.voteAverage.toString()
                tvPopularityDetail.text = "Popularity: " + it?.popularity.toString()

                if (it?.releaseDate != null && it.releaseDate.isNotBlank()) {
                    tvDateDetail.text = "Release: " + it.releaseDate
                } else {
                    tvDateDetail.visibility = View.GONE
                }
            }

        }

        viewModel.errorStatus.observe(viewLifecycleOwner) { text ->
            text?.let {
                Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show()
                viewModel.onSnackbarShown()
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}