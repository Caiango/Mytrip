package com.example.mytrip

import android.content.Intent
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mumayank.com.airlocationlibrary.AirLocation
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var airloc: AirLocation? = null
    lateinit var floating: FloatingActionButton
    lateinit var destination: Button
    lateinit var typedAddres: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        typedAddres = findViewById(R.id.address)
        destination = findViewById(R.id.find_destination)
        floating = findViewById(R.id.floatingActionButton)

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
                            MarkerOptions().position(ll).title("Marker in where you are")
                        )
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 16.0f))

                    }

                })

        }

        //função de click para mostrar destino
        destination.setOnClickListener {
            //pego texto do edittext
            val Address = typedAddres.text.toString().trim()

            //chamo funções Lat e Lng com o texto do edittext e retorno a Lat e Lng para duas variáveis
            val addressLat = getAddressNameLat(Address)
            val addressLng = getAddressNameLng(Address)

            //mostro no mapa com animação meu destino
            val destino = LatLng(addressLat.toDouble(), addressLng.toDouble())
            mMap.addMarker(
                MarkerOptions().position(destino).title("Seu destino")
            )
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destino, 16.0f))
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
                        mMap.addMarker(
                            MarkerOptions().position(ll).title("Sua Posição")
                        )
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 16.0f))

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

}
