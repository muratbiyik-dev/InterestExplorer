package com.pureblacksoft.interestexplorer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.databinding.ActivityMainBinding
import com.pureblacksoft.interestexplorer.function.PrefFun
import com.pureblacksoft.interestexplorer.function.StoreFun

class MainActivity : AppCompatActivity() {
    companion object {
        const val URL_INTEREST_EXPLORER = "InterestExplorer-URL"
    }

    lateinit var binding: ActivityMainBinding
    lateinit var storeFun: StoreFun
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_InterestExplorer)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storeFun = StoreFun(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.containerMA) as NavHostFragment
        navController = navHostFragment.findNavController()

        applyPrefs()
    }

    fun setToolbar(
        icon: Int? = null,
        title: Int? = null,
        button: Int? = null,
        tabVisible: Boolean? = null,
        searchVisible: Boolean? = null
    ) {
        if (icon != null) {
            binding.txtToolbarTitleMA.setCompoundDrawablesWithIntrinsicBounds(
                icon, 0, 0, 0
            )
        }

        if (title != null) {
            binding.txtToolbarTitleMA.text = getString(title)
        }

        if (button != null) {
            binding.imgToolbarButtonMA.setImageResource(button)
        }

        if (tabVisible != null) {
            if (tabVisible)
                binding.tabMA.visibility = View.VISIBLE
            else
                binding.tabMA.visibility = View.GONE
        }

        if (searchVisible != null) {
            if (searchVisible)
                binding.searchMA.visibility = View.VISIBLE
            else
                binding.searchMA.visibility = View.GONE
        }
    }

    fun setBottomNav(button1: Int? = null, button2: Int? = null, navVisible: Boolean? = null) {
        if (button1 != null) {
            binding.bottomNavMA.menu.findItem(R.id.homeFragment).setIcon(button1)
        }

        if (button2 != null) {
            binding.bottomNavMA.menu.findItem(R.id.searchFragment).setIcon(button2)
        }

        if (navVisible != null) {
            if (navVisible)
                binding.bottomNavMA.visibility = View.VISIBLE
            else
                binding.bottomNavMA.visibility = View.GONE
        }
    }

    private fun applyPrefs() {
        PrefFun.currentThemeId = storeFun.readInt(StoreFun.KEY_ID_THEME)
        PrefFun.setAppTheme()
    }
}

//PureBlack Software / Murat BIYIK