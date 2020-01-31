package com.example.catsdogs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_main.*

private const val STATE_TAB_INDEX = "STATE_TAB_INDEX"
private const val STATE_CATS_FRAGMENT = "STATE_CATS_FRAGMENT"
private const val STATE_DOGS_FRAGMENT = "STATE_DOGS_FRAGMENT"

class MainActivity : AppCompatActivity() {

    private lateinit var catsFragment: Fragment
    private lateinit var dogsFragment: Fragment
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            val tabIndex = savedInstanceState.getInt(STATE_TAB_INDEX)
            tabs.getTabAt(tabIndex)?.select()

            catsFragment =
                supportFragmentManager.getFragment(savedInstanceState, STATE_CATS_FRAGMENT)
                    ?: throw Exception("No cats! This should not happen.")
            dogsFragment =
                supportFragmentManager.getFragment(savedInstanceState, STATE_DOGS_FRAGMENT)
                    ?: throw Exception("No dogs! This should not happen.")
        } else {
            loadData()
        }

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { position ->
                    showTabInPosition(position)
                }
            }

        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_TAB_INDEX, tabs.selectedTabPosition)
        supportFragmentManager.putFragment(outState, STATE_CATS_FRAGMENT, catsFragment)
        supportFragmentManager.putFragment(outState, STATE_DOGS_FRAGMENT, dogsFragment)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private fun loadData() {
        data class CatsDogs(val cats: List<CatDogItem>, val dogs: List<CatDogItem>)

        val catsRequest = ApiRequests.requestCats()
        val dogsRequest = ApiRequests.requestDogs()

        val disposable = catsRequest.zipWith(
            dogsRequest,
            BiFunction<CatsDogsListJson, CatsDogsListJson, CatsDogs> { catsData, dogsData ->
                val cats = catsData.data.map { CatDogItem(it) }
                val dogs = dogsData.data.map { CatDogItem(it) }
                CatsDogs(cats = cats, dogs = dogs)
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { (cats, dogs) ->
                catsFragment = addListFragment(data = cats)
                dogsFragment = addListFragment(data = dogs)

                showTabInPosition(tabs.selectedTabPosition)
            }
        disposables.add(disposable)
    }

    private fun restoreData() {

    }

    private fun addListFragment(data: List<CatDogItem>): Fragment {
        val fragment = createListFragment(dataList = data)
        supportFragmentManager.beginTransaction()
            .add(R.id.list_container, fragment)
            .commit()
        return fragment
    }

    private fun showTabInPosition(position: Int) {
        when (position) {
            0 -> showCats()
            1 -> showDogs()
            else -> {}
        }
    }

    private fun showCats() {
        supportFragmentManager.beginTransaction()
            .hide(dogsFragment)
            .show(catsFragment)
            .commit()
    }

    private fun showDogs() {
        supportFragmentManager.beginTransaction()
            .hide(catsFragment)
            .show(dogsFragment)
            .commit()
    }
}
