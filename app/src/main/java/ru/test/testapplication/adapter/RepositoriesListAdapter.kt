package ru.test.testapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.test.testapplication.databinding.RepositoriesListCardBinding
import ru.test.testapplication.dto.Repository
import ru.test.testapplication.utils.Utils

interface OnInteractionListener {
    fun onOpenRepository(repository: Repository)
    fun onDownload(repository: Repository)
}

class RepositoriesListAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<Repository, RepositoriesListAdapter.RepositoryViewHolder>(Utils.RepositoryDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val binding = RepositoriesListCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RepositoryViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        getItem(position)?.let { repository ->
            holder.bind(repository)
        }
    }

    class RepositoryViewHolder(
        private val binding: RepositoriesListCardBinding,
        private val onInteractionListener: OnInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repository: Repository) {

            binding.apply {
                user.text = repository.owner.login
                repositories.text = repository.name

                repositories.setOnClickListener {
                    onInteractionListener.onOpenRepository(repository)
                }

                saveRepositoryButton.setOnClickListener {
                    onInteractionListener.onDownload(repository)
                }
            }
        }
    }
}