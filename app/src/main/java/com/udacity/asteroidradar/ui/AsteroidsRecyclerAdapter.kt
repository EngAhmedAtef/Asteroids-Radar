package com.udacity.asteroidradar.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidItemViewBinding
import com.udacity.asteroidradar.domain.Asteroid

class AsteroidsRecyclerAdapter(private val asteroidClickListener: AsteroidClickListener) :
    ListAdapter<Asteroid, AsteroidsRecyclerAdapter.AsteroidsViewHolder>(DiffCallback) {

    class AsteroidsViewHolder(private val binding: AsteroidItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AsteroidsViewHolder {
        return AsteroidsViewHolder(AsteroidItemViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(
        holder: AsteroidsViewHolder,
        position: Int
    ) {
        val asteroid = getItem(position)
        holder.itemView.setOnClickListener { asteroidClickListener.onAsteroidClick(asteroid) }
        holder.bind(asteroid)
    }

    class AsteroidClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onAsteroidClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}
