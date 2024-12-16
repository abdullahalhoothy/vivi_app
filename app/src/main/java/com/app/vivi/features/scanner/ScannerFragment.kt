package com.app.vivi.features.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.vivi.R
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.databinding.FragmentNotificationsBinding
import com.app.vivi.databinding.FragmentScannerBinding
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.features.login.LoginViewModel
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class ScannerFragment : BaseFragmentVB<FragmentScannerBinding>(FragmentScannerBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var barcodeScanner: BarcodeScanner

    @ExperimentalGetImage
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
        handleBackPress()
        addObservers()
        initCamera()
    }

    private fun initViews(){
        binding.apply {
            instructions.text = getString(R.string.take_a_photo_of_wine_labels_then_tap_any_to_see_the_wine_information_txt,
                getString(R.string.app_name))

            inCameraItem.tvDescTakePhoto.text = getString(R.string.take_a_photo_of_wine_labels_then_tap_any_to_see_the_wine_information_txt,
                getString(R.string.app_name))

        }
    }

    private fun initListeners(){
        binding.captureButton.setOnClickListener {
            takePhoto()
        }

        binding.inCameraItem.ivDelete.setOnClickListener {
            binding.inCameraItem.clScanImagePreview.visibility = View.GONE
            binding.inCameraItem.clMessage.visibility = View.VISIBLE
        }
    }

    private fun handleBackPress() {
        binding.ivClose.setOnClickListener { findNavController().popBackStack() }
    }

    @ExperimentalGetImage
    private fun initCamera(){
        cameraExecutor = Executors.newSingleThreadExecutor()
        barcodeScanner = BarcodeScanning.getClient()

        /*if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestCameraPermission()
        }*/

        checkAndRequestCameraPermission()

    }



    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Camera Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            // Image Analysis for QR/Barcode scanning
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                processImageProxy(imageProxy)
            }

            // Select the back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind any previous camera
                cameraProvider.unbindAll()

                // Bind camera lifecycle
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageAnalysis
                )

                Log.d("Camera", "Camera preview started successfully")

            } catch (exc: Exception) {
                Log.e("Camera", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @ExperimentalGetImage
    private fun processImageProxy(imageProxy: ImageProxy) {
        val image = imageProxy.image ?: return
        val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)

        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    val value = barcode.displayValue
                    if (value != null) {
                        Log.d("Barcode", "Scanned: $value")
                        Toast.makeText(requireContext(), "Scanned: $value", Toast.LENGTH_SHORT).show()
                        imageProxy.close()
                        return@addOnSuccessListener
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Barcode", "Scanning failed", e)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    private var imageCapture: ImageCapture? = null

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        // Create a file to save the image
        val photoFile = File(
            requireContext().externalMediaDirs.firstOrNull(),
            "CameraX_${System.currentTimeMillis()}.jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Capture the image
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    Log.d("CameraX", "Image saved: $savedUri")

                    // Display the captured image in the ImageView
                    binding.inCameraItem.clScanImagePreview.visibility = View.VISIBLE
                    binding.inCameraItem.clMessage.visibility = View.GONE
                    binding.inCameraItem.imageView.setImageURI(savedUri)

                    Toast.makeText(requireContext(), "Image saved", Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraX", "Image capture failed", exception)
                    Toast.makeText(requireContext(), "Image capture failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        )
    }


    private fun checkAndRequestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted, start the camera
            startCamera()
        } else {
            // Request camera permission
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Handle the permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start the camera
                startCamera()
            } else {
                // Permission denied
                Toast.makeText(requireContext(), "Camera permission is required to scan QR codes", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }

    override fun getMyViewModel() = viewModel

    private fun addObservers() {

        collectWhenStarted {
            viewModel.channelLoginEmail.collectLatest { event ->
                when (event) {
                    is LoginViewModel.NavigationLoginEmailEvents.NavigateToLoginScreen -> {
                        /*findNavController().navigateWithSingleTop(
                            LoginEmailFragmentDirections.actionLoginEmailFragmentToLoginFragment()
                        )*/
                    }
                }
            }
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 101
    }

}