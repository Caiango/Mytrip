package com.example.mytrip

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import mumayank.com.airlocationlibrary.AirLocation
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var airloc: AirLocation? = null
    lateinit var floating: FloatingActionButton
    lateinit var imgbutgo: ImageButton
    lateinit var calcDist: Button
    lateinit var backbut: Button
    lateinit var typedAddres: EditText
    lateinit var teste: String

    val loc1 = Location("")
    val loc2 = Location("")

    lateinit var myposition: String
    var destino: String = ""

    companion object {
        var dist = ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

//        URL("www.google.com").readText()


        typedAddres = findViewById(R.id.address)
        imgbutgo = findViewById(R.id.imageButtonGo)
        floating = findViewById(R.id.floatingActionButton)
        calcDist = findViewById(R.id.calc_dist)
        backbut = findViewById(R.id.button_back)

        //função de click para mostrar minha posição
        floating.setOnClickListener {
            airloc = AirLocation(this, true, true,
                object : AirLocation.Callbacks {
                    override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {

                        Toast.makeText(applicationContext, "ERRO", Toast.LENGTH_SHORT).show()
                    }

                    override fun onSuccess(location: Location) {

                        val ll = LatLng(location.latitude, location.longitude)
                        mMap.addMarker(
                            MarkerOptions().position(ll).title("Sua Posição")
                        )
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 16.0f))

                    }

                })

        }

        //função de click para mostrar destino
        imgbutgo.setOnClickListener {
            try {
                //pego texto do edittext
                if (typedAddres.text.isNotEmpty()) {
                    val Address = typedAddres.text.toString().trim()
                    destino = typedAddres.text.toString().trim()

                    //chamo funções Lat e Lng com o texto do edittext e retorno a Lat e Lng para duas variáveis
                    val addressLat = getAddressNameLat(Address)
                    val addressLng = getAddressNameLng(Address)


                    loc2.latitude = addressLat.toDouble()
                    loc2.longitude = addressLng.toDouble()


                    //mostro no mapa com animação meu destino
                    val destino = LatLng(addressLat.toDouble(), addressLng.toDouble())
                    mMap.addMarker(
                        MarkerOptions().icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_ORANGE
                            )
                        )
                            .position(destino).title("Seu destino")
                    )
                    Toast.makeText(applicationContext, "CLIQUE NO MARCADOR DE DESTINO PARA VISUALIZAR SUA ROTA",Toast.LENGTH_LONG).show()
                    hideKeyboard()
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destino, 13.0f))
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Por favor informe um Destino",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    applicationContext,
                    "Por favor seja mais preciso",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        calcDist.setOnClickListener {
            if (destino.isNotEmpty()) {

                calcDistance()
                callFrag()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Por favor procure por seu destino",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        backbut.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }


        //código responsável por mostrar o mapa no fragment
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    //função responsável por pegar meu LatLng atual e marcar no mapa com animação onde estou
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (mMap != null) {

            airloc = AirLocation(this, true, true,
                object : AirLocation.Callbacks {
                    override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {

                        Toast.makeText(applicationContext, "ERRO", Toast.LENGTH_SHORT).show()
                    }

                    override fun onSuccess(location: Location) {

                        val ll = LatLng(location.latitude, location.longitude)

                        myposition = getAddressLatLng(location.latitude, location.longitude)

                        mMap.addMarker(
                            MarkerOptions().position(ll).title("Sua Posição")
                        )
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 16.0f))

                        loc1.latitude = location.latitude
                        loc1.longitude = location.longitude


                    }

                })
        }


    }

    //função padrão para retornar posição atual
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        airloc?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    //função padrão para retornar posição atual
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        airloc?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    //função para retornar endereço da LatLng digitada
    fun getAddressLatLng(lat: Double, long: Double): String {
        val geocoder = Geocoder(applicationContext)
        val adress = geocoder.getFromLocation(lat, long, 1)
        return adress[0].getAddressLine(0)
    }


    //função para retornar Latitude do endereço digitado
    fun getAddressNameLat(Address: String): String {
        val geocoder = Geocoder(applicationContext)
        val adress = geocoder.getFromLocationName(Address, 1)
        return adress[0].latitude.toString()
    }

    //função para retornar Longitude do endereço digitado
    fun getAddressNameLng(Address: String): String {
        val geocoder = Geocoder(applicationContext)
        val adress = geocoder.getFromLocationName(Address, 1)
        return adress[0].longitude.toString()
    }

    //função que utiliza a DirectionsAPI para calcular distancia
    fun calcDistance() {
        val apiRequest = DirectionsApi.newRequest(getGeoContext())
        apiRequest.origin(myposition)
        apiRequest.destination(destino)
        apiRequest.mode(TravelMode.DRIVING)
        apiRequest.setCallback(object : com.google.maps.PendingResult.Callback<DirectionsResult> {
            override fun onResult(result: DirectionsResult?) {

                val routes = result!!.routes
                dist = routes[0].legs[0].distance.toString().trim()
                if (dist.isNullOrBlank()) {
                    Toast.makeText(
                        applicationContext,
                        "Por favor insira o Estado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                val mainpart = dist.split(" ", "km", ",")
                val a = mainpart[0]
                val b = mainpart[1]
                dist = a+b

            }

            override fun onFailure(e: Throwable?) {
                Log.e("Erro", e.toString())
            }

        })
    }

    //função autenticadora para utilizar o DirectionAPI
    fun getGeoContext(): GeoApiContext {
        val geoApiContext =
            GeoApiContext.Builder().apiKey("AIzaSyCKEM68jXakrWtvUcv9D1kNW9SVTLeegYs").build()
        return geoApiContext
    }

    fun callFrag() {
        var a = supportFragmentManager.beginTransaction()
        var b = TransitionFragment()
        a.replace(R.id.maps_lay, b)
        a.commit()
    }

    //Três funções para esconder o teclado
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


}
