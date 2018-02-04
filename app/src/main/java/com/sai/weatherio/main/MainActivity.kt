package com.sai.weatherio.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
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
import android.support.v7.widget.RecyclerView
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.text.Editable
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    @Inject lateinit var viewmodelFactory: ViewModelProvider.Factory

    private lateinit var viewmodel: ForecastCollectionViewModel

    private lateinit var adapter: ForecastAdapter

    private lateinit var layoutManager: LinearLayoutManager

    private val forecastList: MutableList<SingleDayForecast> = mutableListOf()

    private val sScrollPosition = "scroll_position"

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
                        toggleBusy(false)
                        location_edit_text.clearFocus()
                    } else if (resource.status.contentEquals(Resource.ERROR)) {
                        Snackbar.make(main_coordinator_layout, resource.message.toString(), Snackbar.LENGTH_LONG).show()
                        toggleListVisibility(false)
                        toggleBusy(false)
                    }
                }
            }
        })

        initRecyclerView()
        initFetch()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        val scrollPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
        outState?.putInt(sScrollPosition, scrollPosition)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let {
            val scrollPosition: Int = savedInstanceState.getInt(sScrollPosition)
            val count = layoutManager.childCount
            if(scrollPosition != RecyclerView.NO_POSITION && scrollPosition < count)
                layoutManager.scrollToPosition(scrollPosition)
        }
    }

    private fun initFetch() {
        location_edit_text.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                fetch_button.visibility = if (s.isNotEmpty())
                    View.VISIBLE
                else
                    View.GONE
            }
        })

        fetch_button.setOnClickListener {
            toggleBusy(true)
            fetchWeatherData()
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                    this.currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    private fun fetchWeatherData() {
        viewmodel.getForecast(location_edit_text.text.toString())
    }

    private fun initRecyclerView() {
        layoutManager = LinearLayoutManager(this)
        forecast_recycler_view.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(forecast_recycler_view.context,
                layoutManager.orientation)
        forecast_recycler_view.addItemDecoration(dividerItemDecoration)

        adapter = ForecastAdapter(this, forecastList)
        forecast_recycler_view.adapter = adapter
    }

    private fun setListData(list: List<SingleDayForecast>?) {
        list?.let {
            if(list.isEmpty()) {
                Timber.d("Empty weather forecast, display empty list")
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

    private fun toggleBusy(isBusy: Boolean) {
        progress_bar_layout.visibility = if(isBusy) View.VISIBLE else View.GONE
        when(isBusy) {
            true -> {
                progress_bar_layout.visibility = View.VISIBLE
                emptyLayout.visibility = View.GONE
            }
            false -> {
                progress_bar_layout.visibility = View.GONE
            }
        }
    }
}
