package com.example.gdscandroidproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import java.util.ArrayList

class SliderAdapter(context: MainActivity, sliderDataArrayList: ArrayList<SliderData>) :
    SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder>() {
    // list for storing urls of images.
    private val mSliderItems: List<SliderData>

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.slider_layout, null)
        return SliderAdapterViewHolder(inflate)
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    override fun onBindViewHolder(viewHolder: SliderAdapterViewHolder, position: Int) {
        val sliderItem = mSliderItems[position]

        // Glide is use to load image
        // from url in your imageview.
        Glide.with(viewHolder.itemView)
            .load(sliderItem.imgUrl)
            .fitCenter()
            .into(viewHolder.imageViewBackground)
    }

    // this method will return
    // the count of our list.
    override fun getCount(): Int {
        return mSliderItems.size
    }

    class SliderAdapterViewHolder(itemView: View) :
        ViewHolder(itemView) {
        val imageViewBackground: ImageView = itemView.findViewById(R.id.myimage)


    }

    // Constructor
    init {
        mSliderItems = sliderDataArrayList
    }
}