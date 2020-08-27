package com.bjornfree.tochka.loyalty.ui.home

import android.os.Bundle
import android.transition.ChangeBounds
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bjornfree.prices.utils.ConsoleLog
import com.bjornfree.prices.utils.Coroutines
import com.bjornfree.prices.utils.changeFragment
import com.bjornfree.tochka.loyalty.R
import com.bjornfree.tochka.loyalty.ui.account.AccountFragment
import com.bjornfree.tochka.loyalty.ui.catalog.CatalogFragment
import com.bjornfree.tochka.loyalty.ui.contacts.ContactsFragment
import com.bjornfree.tochka.loyalty.ui.shops.AboutShopFragment
import com.bjornfree.tochka.loyalty.ui.shops.ShopMainFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yandex.mapkit.MapKitFactory
import kotlinx.android.synthetic.main.activity_loading.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity(), MainListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel: MainViewModel
    private var back_pressedTimer: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setContentView(R.layout.activity_loading)
        viewModel.mainListener = this
        clog = ConsoleLog(120, "clogLoyalty.txt", this)
        MapKitFactory.setApiKey("399fc6c8-98e9-477b-bb02-6b3d4cd54df2")
        MapKitFactory.initialize(this)

        initLayout()

    }

    private fun initLayout() {
        window.enterTransition = null
        val bounds = ChangeBounds()
        bounds.duration = 700
        window.sharedElementEnterTransition = bounds

        Coroutines.main {
            delay(1000)
            tv_hello.visibility = View.VISIBLE
            tv_title.visibility = View.VISIBLE
            tv_tochka.visibility = View.VISIBLE

            viewModel.loadMainData()
        }
    }


    override fun onStarted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSuccess() {
        setContentView(R.layout.activity_main)
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        changeFragment(
            supportFragmentManager,
            HomeFragment(), HomeFragment::class.simpleName, R.id.fragment_container
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return onNavigationItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var selectedFragment: Fragment? = null
        when (item.itemId) {
            R.id.homeFragment -> changeFragment(
                supportFragmentManager,
                HomeFragment(), HomeFragment::class.simpleName, R.id.fragment_container
            )
            R.id.shopMainFragment -> changeFragment(
                supportFragmentManager,
                ShopMainFragment(),
                ShopMainFragment::class.simpleName, R.id.fragment_container
            )
            R.id.catalogFragment -> changeFragment(
                supportFragmentManager,
                CatalogFragment(),
                CatalogFragment::class.simpleName, R.id.fragment_container
            )
            R.id.accountFragment -> changeFragment(
                supportFragmentManager,
                AccountFragment(),
                AccountFragment::class.simpleName, R.id.fragment_container
            )
            R.id.contactsFragment -> changeFragment(
                supportFragmentManager,
                ContactsFragment(),
                ContactsFragment::class.simpleName, R.id.fragment_container
            )
        }
        return true
    }

    override fun onFailure(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        lateinit var clog: ConsoleLog
    }

    override fun onBackPressed() {
        var hasBackStk = false
        val fm = supportFragmentManager
        for (fragment in fm.fragments) {
            if (fragment.isVisible && hasBackStack(fragment)) {
                if (popFragment(fragment)) return
            }
        }

        val currentFragment = fm.findFragmentByTag(HomeFragment::class.simpleName)
        val aboutShop = fm.findFragmentByTag(AboutShopFragment::class.simpleName)

        if (aboutShop != null  && aboutShop.isVisible) {
            super.onBackPressed()
            return
        }

        if (currentFragment != null  && !currentFragment.isVisible) {
            changeFragment(supportFragmentManager, HomeFragment(), HomeFragment::class.simpleName, R.id.fragment_container)
            bottom_navigation.selectedItemId = R.id.homeFragment
            return
        }

        if(currentFragment != null && currentFragment.isVisible){
            if (back_pressedTimer + 2000 > System.currentTimeMillis()) {
                super.onBackPressed()
            } else {
                Toast.makeText(baseContext, "Нажмите еще раз для выхода.", Toast.LENGTH_SHORT).show()
            }
            back_pressedTimer = System.currentTimeMillis()
        }

    }

    private fun hasBackStack(fragment: Fragment): Boolean {
        val childFragmentManager = fragment.childFragmentManager
        return childFragmentManager.backStackEntryCount > 0
    }

    private fun popFragment(fragment: Fragment): Boolean {
        val fragmentManager = fragment.childFragmentManager
        for (childFragment in fragmentManager.fragments) {
            if (childFragment.isVisible) {
                return if (hasBackStack(childFragment)) {
                    popFragment(childFragment)
                } else {
                    fragmentManager.popBackStack()
                    true
                }
            }
        }
        return false
    }
}

