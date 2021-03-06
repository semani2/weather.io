package com.sai.weatherio.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sai.weatherio.R
import com.sai.weatherio.main.MainActivity
import com.sai.weatherio.model.SingleDayForecast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_forecast.view.*

/**
 * Created by sai on 2/3/18.
 */
class ForecastAdapter(private val activity: MainActivity, private val forecastList: MutableList<SingleDayForecast>)
    : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) = holder.bind(forecastList[position])

    override fun getItemCount() = forecastList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ForecastViewHolder(activity, LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_forecast, parent, false))


    class ForecastViewHolder(private var activity: MainActivity, itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(item: SingleDayForecast) {
            with(itemView) {
                day_text_view.text = item.dayName
                description_text_view.text = item.description
                Picasso.with(activity)
                        .load(item.iconUrl)
                        .into(forecast_image_view)
            }
        }
    }
}