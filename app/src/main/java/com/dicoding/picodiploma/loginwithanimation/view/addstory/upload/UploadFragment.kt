package com.dicoding.picodiploma.loginwithanimation.view.addstory.upload

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.FragmentUploadBinding
import com.dicoding.picodiploma.loginwithanimation.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch
import java.io.File
import com.dicoding.picodiploma.loginwithanimation.data.domain.Result
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size
import androidx.navigation.fragment.navArgs

class UploadFragment : Fragment() {

    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding
    private val navArgs: UploadFragmentArgs by navArgs()
    private lateinit var uploadViewModel: UploadViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uploadViewModel = ViewModelProvider(this, ViewModelFactory(requireActivity()))[UploadViewModel::class.java]

        binding?.apply {

            Glide.with(requireActivity())
                .load(if (navArgs.photo.isFromCamera) navArgs.photo.imageBitmap else navArgs.photo.imageUri)
                .into(ivStory)

            btnClose.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            btnSend.setOnClickListener {
                val photoFile = navArgs.photo.imageFile
                val description = edAddDescription.text.toString().trim()

                if (description.isEmpty()) {
                    Toast.makeText(requireActivity(), getString(R.string.data_empty), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                compressThenUploadImage(photoFile, description)
            }
        }
    }

    private fun compressThenUploadImage(image: File, description: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            val compressedFile = Compressor.compress(requireActivity(), image) {
                quality(50)
                size(1_000_000)
            }
            uploadViewModel.storyUpload(compressedFile, description).observe(viewLifecycleOwner) {
                when (it) {
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(requireActivity(), it.error, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> { showLoading(true) }
                    is Result.Success -> {
                        showLoading(false)
                        requireActivity().setResult(Activity.RESULT_OK)
                        requireActivity().finish()
                        Toast.makeText(requireActivity(), it.data.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) { binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE }
}