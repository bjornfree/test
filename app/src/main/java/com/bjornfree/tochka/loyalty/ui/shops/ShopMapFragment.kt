package com.bjornfree.tochka.loyalty.ui.shops


import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bjornfree.prices.utils.toast
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.data.repository.Repository
import com.bjornfree.tochka.loyalty.model.Shop
import com.bjornfree.tochka.loyalty.ui.BaseFragment
import com.sdsmdg.tastytoast.TastyToast
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.runtime.image.ImageProvider
import kotlinx.android.synthetic.main.fragment_map_shop.*
import me.weyye.hipermission.HiPermission
import me.weyye.hipermission.PermissionCallback


class ShopMapFragment : BaseFragment(R.layout.fragment_map_shop), MapObjectTapListener {
    private lateinit var viewModel: ShopViewModel
    private lateinit var searchManager: SearchManager
    private lateinit var repository: Repository
    private var lastPinnedObject : PlacemarkMapObject? = null
    private var resourceBackedImage : ImageProvider? = null
    private var resourceBackedImage1 : ImageProvider? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        repository = Repository(requireContext())
        viewModel = ViewModelProvider(this).get(ShopViewModel::class.java)

        SearchFactory.initialize(context)
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.ONLINE)

        if (checkPermissions()) {
            val pointCollection = mapview.map.mapObjects.addCollection()
            resourceBackedImage = ImageProvider.fromResource(requireContext(), R.mipmap.ic_location_tochka)
            resourceBackedImage1 = ImageProvider.fromResource(requireContext(), R.drawable.ic_location_tochka_big)

            viewModel.citiesLiveData.observe(viewLifecycleOwner, Observer {
                pointCollection.clear()
                for (city in it) {
                    for (shop in city.items) {
                        if(shop.latitude.isNotEmpty() && shop.longitude.isNotEmpty()) {
                            val placemark: PlacemarkMapObject = pointCollection.addPlacemark(
                                Point(shop.latitude.toDouble(), shop.longitude.toDouble()))
                            placemark.setIcon(resourceBackedImage!!)
                            placemark.userData = shop
                            placemark.addTapListener(this)
                        }
                    }
                }
            })
            setLastLocation()
        } else {
            checkPermission()
        }
    }

    override fun onStart() {
        super.onStart()
        mapview.onStart()
        MapKitFactory.getInstance().onStart()

        setLastLocation()
    }

    override fun onStop() {
        super.onStop()
        mapview.onStop()
        MapKitFactory.getInstance().onStop()
    }

    private fun checkPermission() {
        HiPermission.create(context)
            .checkSinglePermission(ACCESS_FINE_LOCATION, object : PermissionCallback {
                override fun onFinish() {

                }

                override fun onDeny(permission: String?, position: Int) {

                }

                override fun onGuarantee(permission: String?, position: Int) {
                    setLastLocation()
                }

                override fun onClose() {
                    checkPermission()
                }

            })

    }

    private fun setLastLocation() {
        val position = getLastKnownLocation(requireContext())
        position?.let {
            mapview.map.move(CameraPosition(Point(position[0], position[1]), 15.0f, 0.0f, 0.0f), Animation(Animation.Type.SMOOTH, 0F), null)
        }
    }

    private fun getLastKnownLocation(context: Context): DoubleArray? {
        if (ActivityCompat.checkSelfPermission(
                context,
                ACCESS_FINE_LOCATION
            ) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PERMISSION_GRANTED
        ) {
            checkPermission()
            return null
        }
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers: List<String> = locationManager.getProviders(true)
        var location: Location? = null
        for (i in providers.size - 1 downTo 0) {
            location = locationManager.getLastKnownLocation(providers[i])
            if (location != null)
                break
        }
        val gps = DoubleArray(2)
        if (location != null) {
            gps[0] = location.latitude
            gps[1] = location.longitude
        }
        return gps
    }

    private fun checkPermissions() = ActivityCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PERMISSION_GRANTED

    override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
        lastPinnedObject?.let {
            it.setIcon(resourceBackedImage!!)
        }
        val s = mapObject as PlacemarkMapObject
        s.setIcon(resourceBackedImage1!!)
        val shop = mapObject.userData as Shop
        mapview.map.move(CameraPosition(Point(shop.latitude.toDouble(),shop.longitude.toDouble()), 15.0f, 0.0f, 0.0f), Animation(Animation.Type.SMOOTH, 0.5F), null)
        requireContext().toast(shop.name, TastyToast.SUCCESS)
        lastPinnedObject = s
        return true
    }

}