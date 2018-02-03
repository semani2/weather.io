package com.sai.weatherio.main

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.sai.weatherio.R
import com.sai.weatherio.adapters.ForecastAdapter
import com.sai.weatherio.model.SingleDayForecast
import com.sai.weatherio.viewmodels.ForecastCollectionViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var viewmodelFactory: ViewModelProvider.Factory

    private lateinit var viewmodel: ForecastCollectionViewModel

    private lateinit var adapter: ForecastAdapter

    private val forecastList: MutableList<SingleDayForecast> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidInjection.inject(this)

        viewmodel = ViewModelProviders.of(this, viewmodelFactory).get(ForecastCollectionViewModel::class.java)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = ForecastAdapter(forecastList)
        forecast_recycler_view.adapter = adapter
    }
}
