package ru.test.testapplication.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import ru.test.testapplication.R
import ru.test.testapplication.adapter.OnInteractionListener
import ru.test.testapplication.adapter.RepositoriesListAdapter
import ru.test.testapplication.databinding.FragmentRepositoriesListBinding
import ru.test.testapplication.dto.Repository
import ru.test.testapplication.utils.Utils.createDialog
import ru.test.testapplication.utils.Utils.hideKeyboard
import ru.test.testapplication.viewmodel.AppViewModel

class RepositoryFragment : Fragment() {

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
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.title)

        val binding = FragmentRepositoriesListBinding.inflate(inflater, container, false)

        val adapter = RepositoriesListAdapter(object : OnInteractionListener {
            override fun onOpenRepository(repository: Repository) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repository.html_url))
                if (packageManager?.let { intent.resolveActivity(it) } != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(activity, R.string.app_not_found, Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onDownload(repository: Repository) {
                viewModel.downloadUserRepository(
                    repository.owner.login,
                    repository.name,
                    requireContext()
                )
            }
        })

        viewModel.loadRepositoryExceptionEvent.observe(viewLifecycleOwner, {
            createDialog(requireActivity(), R.string.failed_download)
        })

        with(binding) {
            lifecycleScope.launchWhenCreated {
                viewModel.data
                    .collectLatest { state ->
                        adapter.submitList(state)
                        emptyText.isVisible = state.isEmpty()
                    }
            }

            recyclerRepositoriesList.adapter = adapter

            searchButton.setOnClickListener {
                if (searchEditText.text.isNullOrBlank()) {
                    Toast.makeText(
                        requireContext(), R.string.toast_empty_field, Toast.LENGTH_LONG
                    ).show()
                } else {
                    viewModel.getUserRepositories(searchEditText.text.toString())
                    searchEditText.clearFocus()
                    searchEditText.setText("")
                    hideKeyboard(requireView())
                }
            }

            downloadsButton.setOnClickListener {
                findNavController().navigate(R.id.action_repositoryFragment_to_fragmentDownloads)
            }
        }

        return binding.root
    }
}