package ndd.com.recorder.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ndd.com.recorder.R
import ndd.com.recorder.databinding.FragmentPlayerBinding


class PlayerFragment : DialogFragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PlayerViewModel
    private lateinit var itemPath: String

    companion object {
        private const val ARG_ITEM_PATH = "recording_item_path"
    }

    fun newInstance(itemPath: String): PlayerFragment {
        val fragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putString(ARG_ITEM_PATH, itemPath)
        fragment.arguments = bundle
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemPath = arguments?.getString(ARG_ITEM_PATH).toString()
        binding.playerView.showTimeoutMs = 0
        val application = requireNotNull(this.activity).application
        val viewModelFactory = PlayerViewModelFactory(itemPath, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(PlayerViewModel::class.java)
        viewModel.itemPath = itemPath
        viewModel.player.observe(viewLifecycleOwner, Observer {
            binding.playerView.player = it
        })
    }
}