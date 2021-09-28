package com.pureblacksoft.interestexplorer.function

import androidx.appcompat.app.AppCompatDelegate

class PrefFun {
    companion object {
        //region Theme Id
        const val ID_THEME_DEFAULT = 0
        const val ID_THEME_LIGHT = 1
        const val ID_THEME_DARK = 2
        //endregion

        var currentThemeId = ID_THEME_DEFAULT

        fun setAppTheme() {
            when (currentThemeId) {
                ID_THEME_DEFAULT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                ID_THEME_LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                ID_THEME_DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }
}

//PureBlack Software / Murat BIYIK