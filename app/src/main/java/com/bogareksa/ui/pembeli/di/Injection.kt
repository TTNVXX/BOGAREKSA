package com.bogareksa.ui.pembeli.di

import android.app.Application
import android.content.Context
import com.bogareksa.ui.pembeli.CustomerRepository
//import com.bogareksa.ui.pembeli.data.local.CartDatabase

object Injection {
    fun provideRepository(application: Application): CustomerRepository {
        return CustomerRepository.getInstance(application)
    }
}