package ru.test.testapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.test.testapplication.databinding.DownloadsListCardBinding
import ru.test.testapplication.dto.Repository
import ru.test.testapplication.utils.Utils

interface DownloadsOnInteractionListener {
    fun onOpenRepository(repository: Repository)
}

class DownloadsListAdapter(
    private val onInteractionListener: DownloadsOnInteractionListener
) : ListAdapter<Repository, DownloadsListAdapter.DownloadsViewHolder>(Utils.RepositoryDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadsViewHolder {
        val binding = DownloadsListCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DownloadsViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: DownloadsViewHolder, position: Int) {
        getItem(position)?.let { repository ->
            holder.bind(repository)
        }
    }

    class DownloadsViewHolder(
        private val binding: DownloadsListCardBinding,
        private val onInteractionListener: DownloadsOnInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repository: Repository) {
            binding.apply {
                user.text = repository.owner.login
                repositories.text = repository.name

                repositories.setOnClickListener {
                    onInteractionListener.onOpenRepository(repository)
                }
            }
        }
    }
}