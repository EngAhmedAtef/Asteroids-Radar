package com.udacity.asteroidradar.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.viewmodels.MainViewModel

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.asteroidRecycler.adapter = AsteroidsRecyclerAdapter(AsteroidsRecyclerAdapter.AsteroidClickListener {
            viewModel.displayAsteroidDetails(it)
        })

        viewModel.selectedAsteroid.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.onDisplayAsteroidDetails()
            }
        })

        viewModel.asteroidsFilter.observe(viewLifecycleOwner, Observer {
            viewModel.asteroids.observe(viewLifecycleOwner, Observer {
                val adapter = binding.asteroidRecycler.adapter as AsteroidsRecyclerAdapter
                adapter.submitList(it)
            })
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week_menu -> {
                viewModel.changeShownAsteroids(MainViewModel.AsteroidsShown.WEEK)
            }
            R.id.show_today_menu -> {
                viewModel.changeShownAsteroids(MainViewModel.AsteroidsShown.TODAY)
            }
            R.id.show_saved_menu -> {
                viewModel.changeShownAsteroids(MainViewModel.AsteroidsShown.ALL)
            }
        }
        return true
    }
}
