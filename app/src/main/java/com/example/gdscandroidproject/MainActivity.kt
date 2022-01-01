package com.example.gdscandroidproject


import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.location.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login_page.*
import kotlinx.android.synthetic.main.fragment_main_page.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        this.getWindow()
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        RequestPermission()
        getLastLocation()
    }

    fun getLastLocation() {
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
                        val lati=location.latitude
                        val long=location.longitude
                        mapdata(lati.toFloat(),long.toFloat())
                        start_info.text=
                            "Your Current Location : \n" + getLocalName(
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
    var latitude :Float ?=null
    var longitude :Float ?=null
    public fun mapdata(lat:Float, long: Float) {
        latitude = lat
        longitude = long
    }

    fun toMap(view: View) {
            var lati=latitude
            var long=longitude
            val url =
                "https://www.google.com/maps/search/covid+19+vaccine/@$latitude,$longitude,15z/data=!5m1!1e7"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
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

    fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    fun fetchJson(ansState :String) {
        val client =OkHttpClient()
        val request = Request.Builder().url("https://api.apify.com/v2/key-value-stores/toDWvRj1JpTXiM8FF/records/LATEST?disableRedirect=true")
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            @RequiresApi(Build.VERSION_CODES.M)
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                var body = response.body?.string()
                var gson = GsonBuilder().create()
                var IndiaData = gson.fromJson(body,IndiaData::class.java )
                var state =ansState
                var stateCode: Int = 0
                for (i in IndiaData.regionData.indices){
                    if (IndiaData.regionData[i].region.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        } == state.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }){
                        stateCode = i
                        break
                    }
                }

                //Region Data

                var sRegion =IndiaData.regionData[stateCode].region
                var sActiveCases= IndiaData.regionData[stateCode].activeCases.toString()
                var sRecovered =IndiaData.regionData[stateCode].recovered.toString()
                var sinfected = IndiaData.regionData[stateCode].totalInfected.toString()
                var sNinfected = IndiaData.regionData[stateCode].newInfected.toString()
                var sNrecovered =IndiaData.regionData[stateCode].newRecovered.toString()
                //Data India
                var Nactive =IndiaData.activeCasesNew.toString()
                var ActiveCases= IndiaData.activeCases.toString()
                var Recovered =IndiaData.recovered.toString()
                var death = IndiaData.deaths.toString()
                var infected = IndiaData.totalCases.toString()
                var Ndeath = IndiaData.deathsNew.toString()
                var Nrecovered =IndiaData.recoveredNew.toString()

                textviewIndia.append ("Covid-19 Stats of India\n\nActive Cases found today :-$Nactive \nDeaths Today :- $Ndeath\nNew Recovered Cases :- $Nrecovered\nTotal Active cases :- $ActiveCases \nTotal Infected Cases :- $infected\nTotal Recovered Cases as of now :- $Recovered \nTotal Death :- $death")
                textview2new.append("Covid stats in your state $sRegion\nActiveCases :- $sActiveCases \nNewly Infected Cases :- $sNinfected\nNewly Recovered Cases :- $sNrecovered\nTotal Infected Cases :- $sinfected\nTotal Recovered Cases :- $sRecovered")
                if (sActiveCases.toInt()>1000){
                    textview2new.setBackgroundResource(R.drawable.redcolor)
                }
                else if(sActiveCases.toInt()<=1000&&sActiveCases.toInt()>100){
                    textview2new.setBackgroundResource(R.drawable.orangecolor)
                }
                else
                {
                    textview2new.setBackgroundResource(R.drawable.greencolor)
                }
            }
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }
}
class IndiaData(val activeCases : Int,val activeCasesNew : Int,val recovered : Int,val recoveredNew : Int,val deaths : Int,val deathsNew : Int,val totalCases : Int,val regionData : List<StateData>)
class StateData(val region :String, val activeCases :Int,val  newInfected: Int,val  newRecovered: Int,val recovered :Int,val decresed :Int,val totalInfected :Int)

