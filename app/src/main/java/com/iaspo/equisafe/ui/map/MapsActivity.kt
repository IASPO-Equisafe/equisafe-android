package com.iaspo.equisafe.ui.map


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.iaspo.equisafe.R
import com.iaspo.equisafe.core.data.network.response.homeresponse.mapresponse.DataItem
import com.iaspo.equisafe.core.data.network.response.homeresponse.mapresponse.MapsItem
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.utils.gone
import com.iaspo.equisafe.core.utils.visible
import com.iaspo.equisafe.databinding.ActivityMapsBinding
import com.iaspo.equisafe.databinding.DialogDisasterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mapViewModel: MapViewModel by viewModel()
    private val boundsBuilder = LatLngBounds.Builder()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun intiView() {
        mapViewModel.getMap.observe(this) {
            it?.let { response ->
                when(response) {
                    is ApiResult.Loading -> {
                        Toast.makeText(this, "Sync data...", Toast.LENGTH_LONG).show()
                    }

                    is ApiResult.Success -> {
                        val listDisaster: List<MapsItem> = response.data.data.maps
                        setMarker(listDisaster)

                    }

                    is ApiResult.Error -> {
                        Toast.makeText(this, response.errorMessage, Toast.LENGTH_LONG).show()
                    }

                    else -> { }
                }
            }
        }
    }

    private fun setMarker(listDisaster: List<MapsItem>) {
        listDisaster.forEach { disaster ->
            val latLng = LatLng(disaster.lat, disaster.long)
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(disaster.nama)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
            boundsBuilder.include(latLng)
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
            bounds,
            resources.displayMetrics.widthPixels,
            resources.displayMetrics.heightPixels,
            0
        ))


        mMap.setOnInfoWindowClickListener {
            val idMarkerClick = it.id
            val id = idMarkerClick.substring(1)

            val dataDisasterProv = listDisaster[id.toInt()].data
            val nameProvince = listDisaster[id.toInt()].nama

            if (dataDisasterProv != null) {
                showDialogResult(dataDisasterProv, nameProvince)
            } else {
                showDialogResult(arrayListOf(), nameProvince)
            }

            Log.d("InfoMarker:", id)
        }
    }

    private fun showDialogResult(dataDisasterProv: List<DataItem>, nameProvince: String) {
        val context: Context = this
        val dialog = Dialog(this)
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(true)
            val dialogBinding = DialogDisasterBinding.inflate(LayoutInflater.from(context))
            setContentView(dialogBinding.root)

            with(dialogBinding) {
                if (dataDisasterProv.isEmpty()) {
                    this.emptyDisaster.visible()
                    this.titleDialog.gone()
                    this.ivArrow.gone()
                    this.emptyDisaster.text = getString(R.string.empty_disaster, nameProvince)
                } else {
                    this.emptyDisaster.gone()
                }

                this.titleDialog.text = getString(R.string.natural_disasters_that_have_happened, nameProvince)

                var listDisasterText = ""
                var countDisaster = 1

               dataDisasterProv.forEach {
                   listDisasterText += "$countDisaster. Bencana <b>${it.nama}</b><br>Jumlah terjadinya bencana: <b>${it.total}</b><br><br>"
                   countDisaster++
               }
                this.listDisaster.text = Html.fromHtml(listDisasterText, FROM_HTML_MODE_LEGACY)
            }

            show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        intiView()
    }
}