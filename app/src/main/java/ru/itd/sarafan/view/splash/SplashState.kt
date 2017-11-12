package ru.itd.sarafan.view.splash

import ru.itd.sarafan.rest.model.Categories

/**
 * Created by macbook on 23.10.17.
 */
data class SplashState(val data: Categories? = null, val loading: Boolean? = null, val error: Throwable? = null)