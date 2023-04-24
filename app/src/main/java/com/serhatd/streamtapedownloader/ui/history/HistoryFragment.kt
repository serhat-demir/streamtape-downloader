package com.serhatd.streamtapedownloader.ui.history

import android.app.DownloadManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.serhatd.streamtapedownloader.R
import com.serhatd.streamtapedownloader.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var viewModel: HistoryViewModel
    private lateinit var downloadManager: DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: HistoryViewModel by viewModels()
        viewModel = tempViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        binding.fragment = this

        // init download manager
        downloadManager = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        // init observers
        initObservers()

        return binding.root
    }

    fun deleteDownloadHistory(view: View) {
        Snackbar.make(view, getString(R.string.msg_delete_all_history), Snackbar.LENGTH_LONG).setAction(getString(R.string.btn_yes)) {
            viewModel.deleteDownloadHistory()
        }.show()
    }

    fun goBack() {
        findNavController().popBackStack()
    }

    private fun initObservers() {
        viewModel.downloadHistory.observe(viewLifecycleOwner) {
            it?.let {
                binding.adapter = HistoryAdapter(requireContext(), downloadManager, it)
            }
        }

        viewModel.getDownloadHistory()
    }
}