package io.jadu.trackdown.presentation.companyList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.jadu.trackdown.domain.repository.CompanyRepository
import io.jadu.trackdown.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListingViewModel @Inject constructor(
    private val repository: CompanyRepository
):ViewModel() {
    var state by mutableStateOf(CompanyListingState())
    private var searchJob: Job? = null

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
    ){
        viewModelScope.launch {
            repository.getCompanyList(getFromRemoteSource, query)
                .collect { result ->
                    when(result) {
                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                        is Resource.Success -> {
                            result.data?.let { listings ->
                                state = state.copy(
                                    companies = listings
                                )
                            }
                        }
                        is Resource.Error -> {

                        }
                    }
                }
        }
    }
}