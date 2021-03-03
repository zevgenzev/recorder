package ndd.com.recorder.listRecord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ndd.com.recorder.R
import ndd.com.recorder.database.RecordDatabase
import ndd.com.recorder.database.RecordDatabaseDao
import ndd.com.recorder.databinding.FragmentListRecordBinding


class ListRecordFragment : Fragment() {
    private var _binding: FragmentListRecordBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListRecordBinding.inflate(inflater, container, false)
        val application = requireNotNull(activity).application
        val dataSource = RecordDatabase.getInstance(application).recordDatabaseDao
        val viewModelFactory = ListRecordViewModelFactory(dataSource)
        val listRecordViewModel =
            ViewModelProvider(this, viewModelFactory).get(ListRecordViewModel::class.java)

        binding.listRecordViewModel = listRecordViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        val adapter = ListRecordAdapter()
        binding.recyclerView.adapter = adapter
        listRecordViewModel.records.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}