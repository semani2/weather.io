package com.sai.weatherio.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.sai.weatherio.R
import com.sai.weatherio.adapters.ForecastAdapter
import com.sai.weatherio.model.Resource
import com.sai.weatherio.model.SingleDayForecast
import com.sai.weatherio.viewmodels.ForecastCollectionViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
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

        viewmodel = ViewModelProviders.of(this, viewmodelFactory)
                .get(ForecastCollectionViewModel::class.java)

        viewmodel.getForecast("xyz", true).observe(this, Observer<Resource<List<SingleDayForecast>>> {
            resource ->
            run {
                resource?.let {
                    if (resource.status.contentEquals(Resource.SUCCESS)) {
                        setListData(resource.data)
                    } else if (resource.status.contentEquals(Resource.ERROR)) {
                        Toast.makeText(this, "Error finding weather", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

        initRecyclerView()
    }

    private fun initRecyclerView() {
        forecast_recycler_view.layoutManager = LinearLayoutManager(this)

        adapter = ForecastAdapter(this, forecastList)
        forecast_recycler_view.adapter = adapter
    }

    private fun setListData(list: List<SingleDayForecast>?) {
        list?.let {
            if(list.isEmpty()) {
                Timber.d("Empty weather forecast, display empty list")
                Toast.makeText(this, "Empty weather", Toast.LENGTH_LONG).show()
                return
            }

            forecastList.clear()
            forecastList.addAll(list)
            adapter.notifyDataSetChanged()
        }
    }
}
