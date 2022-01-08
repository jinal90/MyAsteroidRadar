package com.udacity.asteroidradar.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidRowLayoutBinding
import com.udacity.asteroidradar.model.Asteroid

/**
 * @author Jinal Tandel
 * @since 05/01/2022
 */
class AsteroidViewAdapter : RecyclerView.Adapter<AsteroidViewAdapter.ViewHolder>() {

    var data = ArrayList<Asteroid>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    /**
     * custom ViewHolder to handle each row
     */
    inner class ViewHolder(var view: AsteroidRowLayoutBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = DataBindingUtil
            .inflate<AsteroidRowLayoutBinding>(
                LayoutInflater.from(viewGroup.context),
                R.layout.asteroid_row_layout,
                viewGroup,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        data.let {
            viewHolder.view.asteroid = it[position]
            viewHolder.itemView.setOnClickListener(View.OnClickListener { view ->
                val bundle = bundleOf("selectedAsteroid" to it[position])
                view.findNavController().navigate(R.id.action_showDetail, bundle)
            })
        }
    }

    override fun getItemCount() = data.size ?: 0
}