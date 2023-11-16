package com.green.birdscapeopsc7312poe.location

/**code commented due to errors and not functional
 *
 * references: https://docs.mapbox.com/android/navigation/guides/turn-by-turn-navigation/route-generation/
 * reference: https://docs.mapbox.com/android/navigation/guides/turn-by-turn-navigation/route-progress/
 * reference: https://github.com/mapbox/mapbox-navigation-android-examples - MapBox api docs

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.mapbox.android.core.location.LocationEngineCallback
import com.mapbox.android.core.location.LocationEngineResult
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.common.TileStoreOptions.MAPBOX_ACCESS_TOKEN
import com.mapbox.geojson.Point
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.route.RouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.route.RouterOrigin
import com.mapbox.navigation.base.trip.model.RouteProgress
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
import com.mapbox.navigation.core.trip.session.TripSessionStateObserver
import com.mapbox.navigation.core.routealternatives.RouteAlternativesObserver


class NavigationManager(private val context: Context) {

    private val mapboxNavigation: MapboxNavigation =
        MapboxNavigationProvider.create(MapboxNavigationOptions.Builder(context, MAPBOX_ACCESS_TOKEN).build())

    // Initialize observers with empty implementations
    private val routesObserver = RoutesObserver { routes -> }
    private val routeProgressObserver = RouteProgressObserver { routeProgress -> }
    private val tripSessionStateObserver = TripSessionStateObserver { tripSessionState -> }
    private val routeAlternativesObserver = RouteAlternativesObserver { routeProgress: RouteProgress, directionsRoutes: List<DirectionsRoute>, routerOrigin: RouterOrigin -> }

    init {
        mapboxNavigation.registerRoutesObserver(routesObserver)
        mapboxNavigation.registerRouteProgressObserver(routeProgressObserver)
        mapboxNavigation.registerTripSessionStateObserver(tripSessionStateObserver)
        mapboxNavigation.registerRouteAlternativesObserver(routeAlternativesObserver)
    }

    fun requestRoute(origin: Point, destination: Point) {
        val routeOptions = RouteOptions.builder()
            .applyDefaultNavigationOptions(MapboxNavigation.defaultNavigationOptionsBuilder(context, MAPBOX_ACCESS_TOKEN).build())
            .coordinates(listOf(origin, destination).toString())
            .alternatives(true)
            .build()

        mapboxNavigation.requestRoutes(routeOptions, routerCallback)
    }

    private val routerCallback = object : RouterCallback {
        override fun onRoutesReady(routes: List<DirectionsRoute>, routerOrigin: RouterOrigin) {
            mapboxNavigation.setRoutes(routes)
        }

        override fun onCanceled(routeOptions: RouteOptions, routerOrigin: RouterOrigin) {}

        override fun onFailure(reasons: List<RouterFailure>, routeOptions: RouteOptions) {}
    }

    // Added permission check using context
    @SuppressLint("MissingPermission")
    fun startNavigation() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Handle missing permissions
            return
        }
        mapboxNavigation.startTripSession()
    }

    fun stopNavigation() {
        mapboxNavigation.stopTripSession()
    }

    @SuppressLint("MissingPermission")
    fun retrieveLocation() {
        mapboxNavigation.navigationOptions.locationEngine.getLastLocation(locationEngineCallback)
    }

    private val locationEngineCallback = object : LocationEngineCallback<LocationEngineResult> {
        override fun onSuccess(result: LocationEngineResult?) {}

        override fun onFailure(exception: Exception) {}
    }

    fun onDestroy() {
        mapboxNavigation.run {
            unregisterRoutesObserver(routesObserver)
            unregisterRouteProgressObserver(routeProgressObserver)
            unregisterTripSessionStateObserver(tripSessionStateObserver)
            unregisterRouteAlternativesObserver(routeAlternativesObserver)
            onDestroy()
        }
    }
}
*/