package com.sai.weatherio.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.sai.weatherio.R
import com.sai.weatherio.adapters.ForecastAdapter
import com.sai.weatherio.app.WeatherApplication
import com.sai.weatherio.model.Resource
import com.sai.weatherio.model.SingleDayForecast
import com.sai.weatherio.viewmodels.ForecastCollectionViewModel
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject
import android.support.v7.widget.DividerItemDecoration
import android.view.View


class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    @Inject lateinit var viewmodelFactory: ViewModelProvider.Factory

    private lateinit var viewmodel: ForecastCollectionViewModel

    private lateinit var adapter: ForecastAdapter

    private val forecastList: MutableList<SingleDayForecast> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as WeatherApplication).applicationComponent.inject(this);

        viewmodel = ViewModelProviders.of(this, viewmodelFactory)
                .get(ForecastCollectionViewModel::class.java)

        swipe_container.setOnRefreshListener(this)

        viewmodel.forecast.observe(this, Observer<Resource<List<SingleDayForecast>>> {
            resource ->
            run {
                resource?.let {
                    if (resource.status.contentEquals(Resource.SUCCESS)) {
                        swipe_container.isRefreshing = false
                        setListData(resource.data)
                        toggleListVisibility(true)
                    } else if (resource.status.contentEquals(Resource.ERROR)) {
                        Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                        toggleListVisibility(false)
                    }
                }
            }
        })

        initRecyclerView()
        initFetch()
    }

    private fun initFetch() {
        fetch_button.setOnClickListener { fetchWeatherData() }
    }

    private fun fetchWeatherData() {
        viewmodel.getForecast(location_edit_text.text.toString())
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        forecast_recycler_view.layoutManager = linearLayoutManager

        val dividerItemDecoration = DividerItemDecoration(forecast_recycler_view.context,
                linearLayoutManager.orientation)
        forecast_recycler_view.addItemDecoration(dividerItemDecoration)

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

    override fun onRefresh() {
        swipe_container.isRefreshing = true
        fetchWeatherData()
    }

    private fun toggleListVisibility(isListVisible: Boolean) {
        swipe_container.visibility = if(isListVisible) View.VISIBLE else View.GONE
        emptyLayout.visibility = if(isListVisible) View.GONE else View.VISIBLE
    }
}
