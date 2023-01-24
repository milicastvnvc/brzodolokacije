package codeverse.brzodolokacije.ui.maps

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import codeverse.brzodolokacije.data.models.maps.MapUsage
import codeverse.brzodolokacije.data.models.maps.input.Feature
import codeverse.brzodolokacije.ui.maps.CoordinatesViewModel
import codeverse.brzodolokacije.ui.maps.MapsViewModel
import codeverse.brzodolokacije.ui.maps.search.SearchMapScreen
import codeverse.brzodolokacije.ui.theme.backgroundColor
import codeverse.brzodolokacije.ui.theme.primaryColor
import codeverse.brzodolokacije.ui.theme.secondaryColor
import codeverse.brzodolokacije.utils.Routes
import codeverse.brzodolokacije.utils.rememberMapViewWithLifecycle

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MapScreen(navController: NavController,
              onConfirmMap : (feature: Feature) -> Unit,
              onDismissMap : () -> Unit,
              mapsViewModel: MapsViewModel = hiltViewModel(),
              coordinatesViewModel: CoordinatesViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val map = rememberMapViewWithLifecycle()

//    lateinit var libreMap: MapboxMap
//    lateinit var symbolManager: SymbolManager

    val context= LocalContext.current
    val newLocation = mapsViewModel._newLocation

    val argLatitude = remember { mapsViewModel.argLatitude }
    val argLongitude = remember { mapsViewModel.argLongitude }

    Scaffold(
        scaffoldState = scaffoldState
    ) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { map },
            update = { mapView ->
                    mapsViewModel.setMap(mapView,context,
                        coordinatesViewModel::getCoordinatesByUser,
                        navigateToPost = { postId ->
                            navController.navigate(Routes.DETAILS + "/${postId}")
                        }
                    )
                })

        FloatingActionButton(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .size(30.dp),
            backgroundColor = backgroundColor,
            onClick = {

                if(mapsViewModel._mapsEnum.value == MapUsage.Add || mapsViewModel._mapsEnum.value == MapUsage.None){
                    onDismissMap()
                }
                else{
                    navController.navigateUp()
                }

            }) {
            Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close") }

        if (mapsViewModel._mapsEnum.value == MapUsage.Search ||
            mapsViewModel._mapsEnum.value == MapUsage.Add ||
            mapsViewModel._mapsEnum.value == MapUsage.None){

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 60.dp, top = 8.dp, end = 10.dp),
                horizontalAlignment = Alignment.End
            ) {
                    SearchMapScreen(mapsViewModel::setNewLocationOnMap)
            }
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(25.dp)) {

                FloatingActionButton(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomEnd),
                    backgroundColor = primaryColor,
                    contentColor = secondaryColor,
                    onClick = {

                        if ((mapsViewModel.latitude.value <= 90 && mapsViewModel.longitude.value <= 180)
                            || newLocation.value != null) {

                            //sacuvaj koordinate
                            if (newLocation.value == null) {
                                println("Potrebno je slati zahtev")
                                coordinatesViewModel.onCoordinatesSearch(
                                    newLocation,
                                    mapsViewModel.longitude.value,
                                    mapsViewModel.latitude.value,
                                    showErrorToast = {
                                        Toast.makeText(context,"Ups! Ne znamo gde se ovo nalazi!", Toast.LENGTH_LONG).show()
                                    },
                                    onConfirmMap = onConfirmMap) {
                                    if (mapsViewModel._mapsEnum.value == MapUsage.Search){
                                        navController.navigate(Routes.SEARCH + "/"
                                                +mapsViewModel.latitude.value+","
                                                +mapsViewModel.longitude.value+","
                                                +it.place_name)
                                    }
                                    true
                                }
                            } else {
                                if (mapsViewModel._mapsEnum.value == MapUsage.Search) {
                                    navController.navigate(Routes.SEARCH + "/"
                                            + newLocation.value!!.center[1]+","
                                            +newLocation.value!!.center[0]+","
                                            +newLocation.value!!.place_name)
                                }
                                else{
                                    onConfirmMap(newLocation.value!!)
                                }
                                //navController.navigateUp()
                            }

                        }
                    }) {
                    Icon(imageVector = Icons.Filled.Check, contentDescription = "Check")
                }
            }
        }

    }
}