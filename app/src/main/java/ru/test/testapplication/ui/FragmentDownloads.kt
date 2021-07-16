package ru.test.testapplication.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import ru.test.testapplication.R
import ru.test.testapplication.adapter.DownloadsListAdapter
import ru.test.testapplication.adapter.DownloadsOnInteractionListener
import ru.test.testapplication.databinding.FragmentDownloadsListBinding
import ru.test.testapplication.dto.Repository
import ru.test.testapplication.utils.Utils
import ru.test.testapplication.utils.Utils.createDialog
import ru.test.testapplication.viewmodel.AppViewModel

class FragmentDownloads : Fragment() {

    private val viewModel: AppViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    val Fragment.packageManager: PackageManager?
        get() = context?.packageManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.downloads)

        val binding = FragmentDownloadsListBinding.inflate(inflater, container, false)

        val adapter = DownloadsListAdapter(object : DownloadsOnInteractionListener {
            override fun onOpenRepository(repository: Repository) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repository.html_url))
                if (packageManager?.let { intent.resolveActivity(it) } != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(activity, R.string.app_not_found, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        viewModel.loadRepositoryExceptionEvent.observe(viewLifecycleOwner, {
            createDialog(requireActivity(), R.string.failed_download)
        })

        lifecycleScope.launchWhenCreated {
            viewModel.downloads
                .collectLatest { state ->
                    adapter.submitList(state)
                    binding.emptyText.isVisible = state.isEmpty()
                }
        }

        binding.recyclerRepositoriesList.adapter = adapter

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        return binding.root
    }
}