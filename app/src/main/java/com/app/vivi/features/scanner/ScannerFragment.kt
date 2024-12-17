package com.app.vivi.features.scanner

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraControl
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
import com.app.vivi.extension.showShortToast
import com.app.vivi.features.login.LoginViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
    private var flashEnabled = false // Flag to track flash state
    private var cameraControl: CameraControl? = null


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
            instructions.text = getString(R.string.position_the_label_within_the_frame_txt,
                getString(R.string.app_name))

            inCameraItem.tvDescTakePhoto.text = getString(R.string.take_a_photo_of_wine_labels_then_tap_any_to_see_the_wine_information_txt,
                getString(R.string.app_name))
            inCameraItem.tvDesc.text = getString(R.string.something_went_wrong_while_we_nwere_trying_to_match_the_txt,
                getString(R.string.app_name))

            bottoomSheet()
        }
    }

    private fun initListeners(){
        binding.captureButton.setOnClickListener {
            takePhoto()
        }

        binding.galleryButton.setOnClickListener {
            openGallery()
        }

        binding.ivFlash.setOnClickListener {
            toggleFlash()
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

            // ImageCapture use case
            imageCapture = ImageCapture.Builder()
                .build()

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

                // Bind all use cases to the lifecycle
                val camera = cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageCapture, imageAnalysis
                )

                cameraControl = camera.cameraControl

                Log.d("Camera", "Camera preview and ImageCapture started successfully")

            } catch (exc: Exception) {
                Log.e("Camera", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }


    private fun startCamera_old() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Camera Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            // ImageCapture use case
            imageCapture = ImageCapture.Builder()
                .build()

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
        /*val photoFile = File(
            requireContext().externalMediaDirs.firstOrNull(),
            "CameraX_${System.currentTimeMillis()}.jpg"
        )*/
        val photoFile = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
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

    //======== open gallery ======
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                // Display selected image in ImageView

                binding.inCameraItem.clScanImagePreview.visibility = View.VISIBLE
                binding.inCameraItem.clMessage.visibility = View.GONE
                binding.inCameraItem.imageView.setImageURI(selectedImageUri)

            } else {
                "Image not selected".showShortToast(requireContext())
            }
        }
    }

    // ============ End of Open Gallery ==========
    // =========Flash ===========
    private fun toggleFlash() {
        flashEnabled = !flashEnabled // Toggle the flash state
        cameraControl?.enableTorch(flashEnabled) // Enable or disable torch
//        val state = if (flashEnabled) "ON" else "OFF"
        val state = if (flashEnabled) binding.ivFlash.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_flash_on))
        else binding.ivFlash.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_flash_off))
    }
    // ==========Flash end ========


    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    private fun bottoomSheet(){

        // Attach BottomSheetBehavior to the BottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(binding.clCameraItem)


//        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//        bottomSheetBehavior.peekHeight = 400  // Adjust height to show a small portion initially

        // Set the bottom sheet to be able to drag it to full screen
        bottomSheetBehavior.isDraggable = true

        // Set the BottomSheet behavior to allow full expansion
        bottomSheetBehavior.peekHeight = 390 // Default height when collapsed
        bottomSheetBehavior.isFitToContents = true // Adjusts to fit content
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        // Optional: Set the maximum height to match the parent
//        bottomSheetBehavior.maxHeight = resources.displayMetrics.heightPixels
        bottomSheetBehavior.isHideable = false // Prevent hiding the bottom sheet
        bottomSheetBehavior.skipCollapsed = false // Ensure collapsing is possible


        // Listen to BottomSheet state changes
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: android.view.View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
//                        binding.tvBottomSheetContent.text = "BottomSheet Fully Expanded"
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
//                        binding.tvBottomSheetContent.text = "BottomSheet Collapsed"
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
//                        binding.tvBottomSheetContent.text = "Dragging..."
                    }
                }
            }

            override fun onSlide(bottomSheet: android.view.View, slideOffset: Float) {
                // Optional: Handle the dragging offset
            }
        })
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
        private val PICK_IMAGE_REQUEST_CODE = 1001

    }

}