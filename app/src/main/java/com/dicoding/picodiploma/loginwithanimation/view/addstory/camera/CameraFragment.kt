package com.dicoding.picodiploma.loginwithanimation.view.addstory.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.domain.Result
import com.dicoding.picodiploma.loginwithanimation.databinding.FragmentCameraBinding
import com.dicoding.picodiploma.loginwithanimation.util.createFile
import com.dicoding.picodiploma.loginwithanimation.util.rotateBitmap
import com.dicoding.picodiploma.loginwithanimation.util.toBitmap
import com.dicoding.picodiploma.loginwithanimation.util.uriToFile
import com.dicoding.picodiploma.loginwithanimation.view.addstory.upload.model.ImageModel
import androidx.navigation.fragment.findNavController

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launcherPermission.launch(PERMISSIONS.first())
        setUpButton()
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    private fun setUpButton() {
        binding?.apply {
            btnCapture.setOnClickListener { takePhoto() }
            btnChange.setOnClickListener {
                cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA

                startCamera()
            }
            btnClose.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
            btnGallery.setOnClickListener { startGallery() }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherGallery.launch(chooser)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val selectedImage = it.data?.data as Uri
            selectedImage.let { uri ->
                val myFile = uriToFile(uri, requireActivity())
                moveToUpload(imageResult = ImageModel(myFile, uri, isFromCamera = false))
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding?.viewFinder?.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    requireActivity(),
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(requireActivity(), getString(R.string.camera_failed), Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(requireActivity()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photo = createFile(requireActivity().application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photo).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireActivity()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val photoBitmap = photo.toBitmap()
                    val rotatedBitmap = photoBitmap.rotateBitmap(
                        cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
                    )
                    moveToUpload(ImageModel(photo, imageBitmap = rotatedBitmap))
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(requireActivity(), getString(R.string.get_photo_failed), Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun moveToUpload(imageResult: ImageModel) {
        val upload = CameraFragmentDirections.actionCameraFragmentToUploadFragment(imageResult)
        if (findNavController().currentDestination?.id == R.id.cameraFragment) {
            findNavController().navigate(upload)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val launcherPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted and !isAllPermissionsGranted()) {
            Toast.makeText(requireActivity(), getString(R.string.flip_to_front_camera), Toast.LENGTH_SHORT).show()
        }
    }

    private fun isAllPermissionsGranted() = PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireActivity(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}