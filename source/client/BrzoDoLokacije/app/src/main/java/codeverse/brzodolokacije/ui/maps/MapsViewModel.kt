package codeverse.brzodolokacije.ui.maps

import android.R
import android.content.Context
import android.provider.Settings.Global.getString
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import codeverse.brzodolokacije.data.models.UserCoordinateResponse
import codeverse.brzodolokacije.data.models.maps.MapUsage
import codeverse.brzodolokacije.data.models.maps.input.Feature
import codeverse.brzodolokacije.data.models.maps.input.InputResponse
import codeverse.brzodolokacije.utils.MyState
import codeverse.brzodolokacije.utils.awaitMap
import codeverse.brzodolokacije.utils.managers.FeatureManager
import codeverse.brzodolokacije.utils.managers.LocationManager
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapsViewModel @Inject constructor(savedStateHandle: SavedStateHandle,
                                        private val locationManager: LocationManager,
                                        private val featureManager: FeatureManager
) : ViewModel() {

    lateinit var mapView: MapView
    lateinit var libreMap: MapboxMap
    lateinit var symbolManager: SymbolManager
    lateinit var style: Style

    private val _latitude = mutableStateOf(100.0)
    val latitude: State<Double> = _latitude

    private val _longitude = mutableStateOf(200.0)
    val longitude: State<Double> = _longitude

    private val _inputPlaceState = mutableStateOf(MyState<InputResponse>())
    val inputPlaceState: State<MyState<InputResponse>> = _inputPlaceState

    var _newLocation = mutableStateOf<Feature?>(null)
    var argLatitude = mutableStateOf<Double?>(null)
    var argLongitude = mutableStateOf<Double?>(null)
    var _mapsEnum = mutableStateOf<MapUsage>(MapUsage.None)
    var _userId = mutableStateOf<Long>(0L)

    init {
        savedStateHandle.get<String>("mapsEnum")?.let { ordinal ->
            _mapsEnum.value = MapUsage.values()[ordinal.toInt()]
        }
        if (_mapsEnum.value == MapUsage.ShowMany){
            //salje se kao parametar userId
            savedStateHandle.get<String>("mapParametar")?.let { userId ->
                _userId.value = userId.toLong()
            }
        }
        if(_mapsEnum.value == MapUsage.ShowOne){
            savedStateHandle.get<String>("latitude")?.let { latitude ->

                this.argLatitude.value = latitude.toDouble()
            }

            savedStateHandle.get<String>("longitude")?.let { longitude ->

                this.argLongitude.value = longitude.toDouble()
            }
        }

    }

    fun setMap(
        mapView: MapView,
        context: Context,
        getCoordinatesByUser: (Long, (ArrayList<UserCoordinateResponse>, (postId: Long) -> Unit) -> Unit, (postId: Long) -> Unit) -> Unit,
        navigateToPost: (postId: Long) -> Unit
    ) {

        val myClass = this

        GlobalScope.launch(Dispatchers.Main) {
            libreMap = mapView.awaitMap(context = context)
            libreMap.getStyle {
                if (it.isFullyLoaded) {
                    style = it
                    myClass.mapView = mapView

                    if (!myClass::symbolManager.isInitialized) {
                        symbolManager = SymbolManager(mapView, libreMap, it)

                        symbolManager.iconAllowOverlap = true
                        symbolManager.iconIgnorePlacement = true
                    }
                    if (myClass::symbolManager.isInitialized && _mapsEnum.value == MapUsage.None){
                        symbolManager = SymbolManager(mapView, libreMap, it)

                        symbolManager.iconAllowOverlap = true
                        symbolManager.iconIgnorePlacement = true
                    }

                    when(_mapsEnum.value) {

                        MapUsage.Add, MapUsage.Search,MapUsage.None -> {
                            if(_newLocation.value != null){
                                println("Postoji nova lokacija")
                                setNewLocationOnMap(_newLocation.value!!)
                            }
                            else if(latitude.value <= 90 && longitude.value <= 180){
                                println("Postoji latLng")
                                setCameraPosition(LatLng(latitude.value,longitude.value))
                                setSingleMarker(LatLng(latitude.value,longitude.value))
                            }
                            else{
                                val currentLocation = locationManager.getLocation()

                                if (currentLocation != null){
                                    println("Postoji trenutna lokacija")
                                    setCameraPosition(LatLng(currentLocation.latitude, currentLocation.longitude))
                                    onClick(LatLng(currentLocation.latitude, currentLocation.longitude))
                                    setSingleMarker(LatLng(currentLocation.latitude, currentLocation.longitude))
                                }

                            }
                        }
                        MapUsage.ShowOne -> {
                            if (argLatitude.value != null && argLongitude.value != null){
                                setCameraPosition(LatLng(argLatitude.value!!, argLongitude.value!!))
                                setSingleMarker(LatLng(argLatitude.value!!, argLongitude.value!!))
                            }
                        }
                        MapUsage.ShowMany -> {
                            getCoordinatesByUser(_userId.value, myClass::setMultipleMarkers, navigateToPost)
                        }
                    }

                }
            }
            libreMap.addOnMapClickListener {

                if(_mapsEnum.value == MapUsage.Add ||
                    _mapsEnum.value == MapUsage.Search ||
                    _mapsEnum.value == MapUsage.None){
                    onClick(it)
                    setSingleMarker(it)
                }

                true
            }

        }
    }

    fun onClick(coordinates: LatLng){
        _newLocation.value = null;
        _latitude.value = coordinates.latitude
        _longitude.value = coordinates.longitude

    }

    fun setNewLocationOnMap(feature: Feature) {

        _newLocation.value = feature
        println("Postavljam novu lokaciju: ")
        println(_newLocation.value!!.place_name)
        val latLng = LatLng(feature.center[1],feature.center[0])

        setCameraPosition(latLng)
        setSingleMarker(latLng)
    }

    fun onConfirmMap(feature: Feature){
        //featureManager.saveFeature(_newLocation.value!!)

    }

    fun setSingleMarker(latLng: LatLng) {

        println("Postavljam marker")
        symbolManager.deleteAll()
        setMarker(latLng)
    }

    fun setMultipleMarkers(latLngs: ArrayList<UserCoordinateResponse>, navigateToPost: (postId: Long) -> Unit) {

        for (latLng in latLngs) {
            setMarker(LatLng(latLng.latitude, latLng.longitude),latLng.postId, navigateToPost)
        }

    }

    fun setMarker(latLng : LatLng, postId: Long? = null, navigateToPost: (postId: Long) -> Unit = {}){

        val symbolOptions = SymbolOptions()
            .withLatLng(
                com.mapbox.mapboxsdk.geometry.LatLng(
                    latLng.latitude,
                    latLng.longitude
                )
            )
            .withIconImage("marker")
            .withIconSize(1.3f)
            .withIconColor("#d9534f")


        symbolManager.addClickListener(OnSymbolClickListener { symbol ->

            println("Pritisnuo si na pin sa koordinatama " +  symbol.latLng.latitude + " " + symbol.latLng.longitude)

            if(postId != null){
                //odvedi ga na stranicu posta
                navigateToPost(postId)

            }

            true
        })
        symbolManager.create(symbolOptions)
    }

    fun setCameraPosition(latLng: LatLng){
        libreMap.cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(10.0)
            .build()
    }
}