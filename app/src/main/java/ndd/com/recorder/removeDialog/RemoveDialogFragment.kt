package ndd.com.recorder.removeDialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import ndd.com.recorder.R
import ndd.com.recorder.database.RecordDatabase
import ndd.com.recorder.database.RecordDatabaseDao


class RemoveDialogFragment : DialogFragment() {
    private lateinit var viewModel: RemoveViewModel

    companion object {
        private const val ARG_ITEM_PATH = "recording_item_path"
        private const val ARG_ITEM_ID = "recording_item_id"

    }

    fun newInstance(itemId: Long, itemPath: String?): RemoveDialogFragment {
        val fragment = RemoveDialogFragment()
        val bundle = Bundle()
        bundle.putString(ARG_ITEM_PATH, itemPath)
        bundle.putLong(ARG_ITEM_ID, itemId)
        fragment.arguments = bundle
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_remove_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val database = RecordDatabase.getInstance(application).recordDatabaseDao
        val viewModelFactory = RemoveViewModelFactory(database, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RemoveViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val itemPath = arguments?.getString(ARG_ITEM_PATH)
        val itemId = arguments?.getLong(ARG_ITEM_ID)
        return AlertDialog.Builder(activity)
            .setTitle(getString(R.string.dialog_title_delete_audio))
            .setMessage(getString(R.string.dialog_message_delete_file))
            .setPositiveButton(getString(R.string.dialog_button_yes)) { dialog, which ->
                try {
                    itemId?.let { viewModel.removeItem(it) }
                    itemPath?.let { viewModel.removeFile(it) }
                } catch (e: java.lang.Exception) {
                    Log.e("RemoveDialogFragment", "deleteFileDialog exception", e)
                }
                dialog.cancel()
            }
            .setNegativeButton(getString(R.string.dialog_button_noo)) { dialog, which ->
                dialog.cancel()
            }
            .create()

    }
}