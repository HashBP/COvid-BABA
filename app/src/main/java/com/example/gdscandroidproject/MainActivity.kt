package com.example.gdscandroidproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smarteist.autoimageslider.SliderView
import java.util.ArrayList
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.*
import android.text.TextUtils





class MainActivity : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest



    var url1 =
        "https://www.cdc.gov/handwashing/images/Hispanic_SuperHero_HandWashing_Poster_8x11.jpg?_=01372"
    var url2 =
        "https://www.cdc.gov/handwashing/images/posters/key-times-to-wash-hands-eng-11x17-p.jpg?_=29978"
    var url3 =
        "https://www.cdc.gov/handwashing/images/germs-are-everywhere-tn.jpg?_=86268"
    var url4 =
        "https://www.cdc.gov/handwashing/images/posters/wash-your-hands-steps-11x17-1.jpg?_=51895"
    var url5 =
        "https://www.cdc.gov/handwashing/images/Hispanic_Princess_Handwashing_Poster_8x11.jpg?_=01461"
    var url6 =
        "https://www.cdc.gov/handwashing/images/posters/wash-your-hands-campaign-thumb-sml.png?_=85535"
    var url7 =
        "https://www.cdc.gov/handwashing/images/handwashing-is-in-8x11-sm.jpg?_=84717"
    var url8 =
        "https://www.cdc.gov/handwashing/images/keep-calm-wash-your-hands-eng.jpg?_=16783"
    var url9 =
        "https://www.cdc.gov/handwashing/images/AA_BoyGirl_SuperHero_8x11.jpg?_=01420"
    var url10 =
        "https://www.cdc.gov/handwashing/images/handwashing-activities-tn.jpg?_=88620"




    override fun onCreate(savedInstanceState: Bundle?) {
        this.window
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val sliderDataArrayList = ArrayList<SliderData>()

        sliderDataArrayList.add(SliderData(url1))
        sliderDataArrayList.add(SliderData(url2))
        sliderDataArrayList.add(SliderData(url3))
        sliderDataArrayList.add(SliderData(url4))
        sliderDataArrayList.add(SliderData(url5))
        sliderDataArrayList.add(SliderData(url6))
        sliderDataArrayList.add(SliderData(url7))
        sliderDataArrayList.add(SliderData(url8))
        sliderDataArrayList.add(SliderData(url9))
        sliderDataArrayList.add(SliderData(url10))

        val adapter = SliderAdapter(this, sliderDataArrayList)
        slider.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        slider.setSliderAdapter(adapter)
        slider.scrollTimeInSec = 2
        slider.isAutoCycle = true
        slider.startAutoCycle()


        start_button.setOnClickListener{
            val name = input_text.text.toString()
            click()
            RequestPermission()
            getLastLocation(name)
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_down)
            val animationfadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            val animationfadein2 = AnimationUtils.loadAnimation(this, R.anim.fade_in2)
            val animationbounce = AnimationUtils.loadAnimation(this, R.anim.bounce)
            textview2new.startAnimation(animation)
            containerDataState.startAnimation(animationfadein)
            icon.startAnimation(animationfadein2)
            textview3new.startAnimation(animationbounce)
        }
    }





    fun click(){
        if (TextUtils.isEmpty(input_text.getText())) {
            Toast.makeText(this, "Name can not be left blank",Toast.LENGTH_SHORT).show()
            val i = Intent(applicationContext, MainActivity::class.java)
            startActivity(i)
        }
        textview1.visibility=View.VISIBLE
        Container.visibility=View.VISIBLE
        containerDataState.visibility=View.VISIBLE
        textview3new.visibility=View.VISIBLE
        icon.visibility=View.VISIBLE
        gg.visibility=View.GONE

        Container.animate().apply {
        }
    }






    fun getLastLocation(name:String) {
        if (CheckPermission()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        NewLocationData()
                    } else {
                        val lati = location.latitude
                        val long = location.longitude
                        mapdata(lati.toFloat(), long.toFloat())
                        start_info.text =
                            "Hii "+name+"\nYour Current Location : \n" + getLocalName(
                                location.latitude,
                                location.longitude
                            ) + "\nDistrict: " + getDstName(
                                location.latitude,
                                location.longitude
                            ) + "\nState: " + getStateName(location.latitude, location.longitude)
                    }
                }
            } else {
                Toast.makeText(this, "Please Turn on Your device Location", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            RequestPermission()
        }
    }

    private fun getLocalName(lat: Double, long: Double): String {
        var LocalName: String = ""
        val geoCoder = Geocoder(this, Locale.getDefault())
        val Adress = geoCoder.getFromLocation(lat, long, 3)
        Log.d("list", Adress.toString());
        LocalName = Adress.get(0).locality
        return LocalName
    }

    private fun getDstName(lat: Double, long: Double): String {
        var DstName: String = ""
        val geoCoder = Geocoder(this, Locale.getDefault())
        val Adress = geoCoder.getFromLocation(lat, long, 3)
        Log.d("list", Adress.toString());
        DstName = Adress.get(0).subAdminArea
        return DstName
    }

    private fun getStateName(lat: Double, long: Double): String {
        var State = ""
        val geoCoder = Geocoder(this, Locale.getDefault())
        val Adress = geoCoder.getFromLocation(lat, long, 3)
        Log.d("list", Adress.toString());
        State = Adress.get(0).adminArea
        fetchJson(State)
        return State
    }

    fun NewLocationData() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        Looper.myLooper()?.let {
            fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest, locationCallback, it
            )
        }
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            Log.d("Debug:", "your last last location: " + lastLocation.longitude.toString())
            start_info.text =
                "You Last Location was : \nLongitude: " + lastLocation.longitude + "  \nLatitude: " + lastLocation.latitude + "\nCity:" + getStateName(
                    lastLocation.latitude,
                    lastLocation.longitude
                )
        }
    }

    fun toCoWin(view: View) {
        val url = "https://selfregistration.cowin.gov.in/"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    fun toWho(view: View) {
        val url =
            "https://www.who.int/emergencies/diseases/novel-coronavirus-2019/technical-guidance"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    var latitude: Float? = null
    var longitude: Float? = null
    public fun mapdata(lat: Float, long: Float) {
        latitude = lat
        longitude = long
    }

    fun toMap(view: View) {
        var lati = latitude
        var long = longitude
        val url =
            "https://www.google.com/maps/search/covid+19+vaccine/@$latitude,$longitude,15z/data=!5m1!1e7"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
    val approval = 1010
    fun CheckPermission(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    fun RequestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            approval
        )
    }

    fun isLocationEnabled():Boolean{
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    fun fetchJson(ansState: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.apify.com/v2/key-value-stores/toDWvRj1JpTXiM8FF/records/LATEST?disableRedirect=true")
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            @RequiresApi(Build.VERSION_CODES.M)
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                var body = response.body?.string()
                var gson = GsonBuilder().create()
                var IndiaData = gson.fromJson(body, IndiaData::class.java)
                var state = ansState
                var stateCode: Int = 0
                for (i in IndiaData.regionData.indices) {
                    if (IndiaData.regionData[i].region.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        } == state.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }) {
                        stateCode = i
                        break
                    }
                }

                //Region Data

                var sRegion = IndiaData.regionData[stateCode].region
                var sActiveCases = IndiaData.regionData[stateCode].activeCases.toString()
                var sRecovered = IndiaData.regionData[stateCode].recovered.toString()
                var sinfected = IndiaData.regionData[stateCode].totalInfected.toString()
                var sNinfected = IndiaData.regionData[stateCode].newInfected.toString()
                var sNrecovered = IndiaData.regionData[stateCode].newRecovered.toString()
                //Data India
                var Nactive = IndiaData.activeCasesNew.toString()
                var ActiveCases = IndiaData.activeCases.toString()
                var Recovered = IndiaData.recovered.toString()
                var death = IndiaData.deaths.toString()
                var infected = IndiaData.totalCases.toString()
                var Ndeath = IndiaData.deathsNew.toString()
                var Nrecovered = IndiaData.recoveredNew.toString()

                textviewIndia.append("Covid-19 Stats of India\n\nActive Cases found today :-$Nactive \nDeaths Today :- $Ndeath\nNew Recovered Cases :- $Nrecovered\nTotal Active cases :- $ActiveCases \nTotal Infected Cases :- $infected\nTotal Recovered Cases as of now :- $Recovered \nTotal Death :- $death")
                textview2new.append("Covid stats in your state $sRegion\nActiveCases :- $sActiveCases \nNewly Infected Cases :- $sNinfected\nNewly Recovered Cases :- $sNrecovered\nTotal Infected Cases :- $sinfected\nTotal Recovered Cases :- $sRecovered")

                if (sActiveCases.toInt() > 5000) {
                    textview2new.setBackgroundResource(R.drawable.redcolor)
                    textview2new.append("\n\nYou are in a RED state zone having more then 5000 active cases\nPlease follow the guidelines, avoid unwanted travelling,wear mask and stay safe.")
                } else if (sActiveCases.toInt() <= 5000 && sActiveCases.toInt() > 500) {
                    textview2new.setBackgroundResource(R.drawable.orangecolor)
                    textview2new.append("\n\nYou are in a ORANGE state zone having less then 5000 active cases\nPlease follow" +
                            "the guidelines properly and avoid unwanted travelling.")
                } else {
                    textview2new.setBackgroundResource(R.drawable.greencolor)
                    textview2new.append("\n\nYou are in a GREEN state zone having less then 500 active cases\nStill you are not safe\n" +
                            "Please follow the covid guidelines and stay safe." )
                }
            }
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }
}
class IndiaData(
    val activeCases: Int,
    val activeCasesNew: Int,
    val recovered: Int,
    val recoveredNew: Int,
    val deaths: Int,
    val deathsNew: Int,
    val totalCases: Int,
    val regionData: List<StateData>
)

class StateData(
    val region: String,
    val activeCases: Int,
    val newInfected: Int,
    val newRecovered: Int,
    val recovered: Int,
    val decresed: Int,
    val totalInfected: Int
)

