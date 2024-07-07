package io.jadu.trackdown.presentation.companyList

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.jadu.trackdown.domain.model.CompanyListing
import io.jadu.trackdown.domain.model.LogoModel
import io.jadu.trackdown.domain.model.LogoModelItem
import io.jadu.trackdown.domain.repository.CompanyRepository
import io.jadu.trackdown.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class CompanyListingViewModel @Inject constructor(
    private val repository: CompanyRepository
) : ViewModel() {
    var state by mutableStateOf(CompanyListingState())
    var state2 by mutableStateOf(CompanyListingState())
    private var _logoState:MutableStateFlow<LogoModelItem?> = MutableStateFlow(null)
    val logoState: StateFlow<LogoModelItem?> = _logoState


    private var searchJob: Job? = null

    init {
        getCompanyListings()
        autoQuerySuggestion()
        /*viewModelScope.launch {
            val logo = async { repository.getCompanyLogo("IBM") }
            Log.d("CompanyListingViewModel", "Logo: ${logo.await().data}")
        }*/
    }

    fun onEvent(event: CompanyListingEvents) {
        when (event) {
            is CompanyListingEvents.Refresh -> {
                getCompanyListings(getFromRemoteSource = true)
            }

            is CompanyListingEvents.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanyListings()
                }
            }
        }
    }

    private fun getCompanyListings(
        query: String = state.searchQuery.lowercase(),
        getFromRemoteSource: Boolean = false
    ) {
        viewModelScope.launch {
            repository.getCompanyList(getFromRemoteSource, query)
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            Log.d("CompanyListingViewModel", "Loading: ${result.isLoading}")
                            state = state.copy(isLoading = result.isLoading)
                        }

                        is Resource.Success -> {
                            Log.d("CompanyListingViewModel", "Success: ${result.data}")
                            result.data?.let { listings ->
                                state = state.copy(
                                    companies = listings
                                )
                                fetchCompanyLogos(listings)

                            }
                        }

                        is Resource.Error -> {
                            Log.d("CompanyListingViewModel", "Error: ${result.message}")
                        }
                    }
                }
        }
    }

    private fun autoQuerySuggestion() {
        viewModelScope.launch {
            Log.d("CompanyListingViewModel", "Search Query: ${state.searchQuery}")
            repository.searchCompany("I")
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            Log.d("CompanyListingViewModel", "Loading: ${result.isLoading}")
                            state = state.copy(isLoading = result.isLoading)
                        }

                        is Resource.Success -> {
                            Log.d("CompanyListingViewModel", "Successd: ${result.data}")
                            result.data?.let { listings ->

                                state = state.copy(
                                    searchQuerySuggestions = listings
                                )
                            }
                        }

                        is Resource.Error -> {
                            Log.d("CompanyListingViewModel", "Error: ${result.message}")
                        }
                    }
                }
        }

    }
    private fun fetchCompanyLogos(listings: List<CompanyListing>) {
        Log.d("CompanyListingViewModel", "Listings: ${listings}")
        viewModelScope.launch {
            listings.forEach {
                val logo = async { getCompanyLogo(it.symbol) }
                val logoResult = logo.await()
                if (logoResult?.isNotEmpty() == true) {
                    Log.d("CompanyListingViewModel", "Logo: ${logoResult[0].image}")
                    _logoState.value = logoResult[0]
                }
            }
        }
    }


    private suspend fun getCompanyLogo(ticker: String): LogoModel? {
        return viewModelScope.async {
            val logo = repository.getCompanyLogo(ticker)
            // Assuming repository.getCompanyLogo returns a LogoModelItem or similar
            logo.data // Return the logo data
        }.await()
    }


}