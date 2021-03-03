package ndd.com.recorder.record

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ndd.com.recorder.MainActivity
import ndd.com.recorder.R
import ndd.com.recorder.database.RecordDatabase
import ndd.com.recorder.database.RecordDatabaseDao
import ndd.com.recorder.databinding.FragmentRecordBinding
import java.io.File


class RecordFragment : Fragment() {
    private lateinit var viewModel: RecordViewModel
    private lateinit var mainActivity: MainActivity
    private var count: Int? = null
    private var database: RecordDatabaseDao? = null
    private val MY_PERMISSION_RECORD_AUDIO = 123
    private var _binding: FragmentRecordBinding? = null
    private val binding get() = _binding!!
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                onRecord(true)
                viewModel.startTimer()
            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.RECORD_AUDIO)) {
                    Toast.makeText(
                        activity,
                        getString(R.string.toast_permission_denied_need_it),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.toast_permission_denied_with_settings),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentRecordBinding.inflate(inflater, container, false)
        database = context?.let { RecordDatabase.getInstance(it).recordDatabaseDao }
        viewModel = ViewModelProvider(this).get(RecordViewModel::class.java)
        mainActivity = activity as MainActivity
        database?.getCount()?.observe(viewLifecycleOwner) { count = it }

        binding.recordViewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        if (!mainActivity.isServiceRunning()) {
            viewModel.resetTimer()
        } else {
            binding.buttonRecord.setImageResource(R.drawable.ic_stop_36)
        }
        binding.buttonRecord.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.RECORD_AUDIO
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission.launch(android.Manifest.permission.RECORD_AUDIO)
//                requestPermissions(
//                    arrayOf(android.Manifest.permission.RECORD_AUDIO), MY_PERMISSION_RECORD_AUDIO
//                )
            } else {
                if (mainActivity.isServiceRunning()) {
                    onRecord(false)
                    viewModel.stopTimer()
                } else {
                    onRecord(true)
                    viewModel.startTimer()
                }
            }
        }

        createChannel(getString(R.string.channel_id), getString(R.string.channel_name))

        return binding.root
    }

    private fun onRecord(start: Boolean) {
        val intent: Intent = Intent(activity, RecordService::class.java)
        intent.putExtra("COUNT", count)
        if (start) {
            binding.buttonRecord.setImageResource(R.drawable.ic_stop_36)
            Toast.makeText(activity, getString(R.string.toast_recording_start), Toast.LENGTH_SHORT)
                .show()
            val folder =
                File(activity?.getExternalFilesDir(null)?.absolutePath.toString() + "/Recorder")
            if (!folder.exists()) {
                folder.mkdir()
            }
            activity?.startService(intent)
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            binding.buttonRecord.setImageResource(R.drawable.ic_mic_white_36)
            activity?.stopService(intent)
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        when (requestCode) {
//            MY_PERMISSION_RECORD_AUDIO -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    onRecord(true)
//                    viewModel.startTimer()
//                } else {
//                    Toast.makeText(
//                        activity,
//                        getString(R.string.toast_permission_denied),
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//                return
//            }
//        }
//    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
                    .apply {
                        setShowBadge(false)
                        setSound(null, null)
                    }
            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


}