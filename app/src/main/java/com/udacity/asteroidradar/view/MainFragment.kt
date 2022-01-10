package com.udacity.asteroidradar.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

//    private val viewModel: MainViewModel by lazy {
//        ViewModelProvider(this).get(MainViewModel::class.java)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        val dataSource = AsteroidDatabase.getInstance(application).asteroidDatabaseDao()
        val viewModelFactory = MainViewModelFactory(dataSource, application)
        val viewModel =
            ViewModelProvider(
                this, viewModelFactory
            )[MainViewModel::class.java]

        val asteroidListAdapter = AsteroidViewAdapter()

        viewModel.asteroidList.observe(viewLifecycleOwner, Observer {
            it?.let {
                asteroidListAdapter.data = it
            }
        })

        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.asteroidRecycler.adapter = asteroidListAdapter

        viewModel.imageOfTheDayUrl.observe(viewLifecycleOwner, Observer {
            it?.let {
                Picasso
                    .with(requireContext())
                    .load(it)
                    .fit()
                    .centerInside()
                    .into(binding.activityMainImageOfTheDay, object : Callback.EmptyCallback() {
                        override fun onSuccess() {
                            // Index 0 is the image view.
                        }
                    })
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
