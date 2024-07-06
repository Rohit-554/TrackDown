package io.jadu.trackdown.presentation.details

import android.util.Log
import android.widget.TextView.SavedState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.jadu.trackdown.domain.model.CompanyInfo
import io.jadu.trackdown.domain.repository.CompanyRepository
import io.jadu.trackdown.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: CompanyRepository
) : ViewModel() {
    var state by mutableStateOf(CompanyInfoState())
    private var currentSymbol: String? = null

    init {
        getInfo()
    }

    fun getInfo(){
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            currentSymbol = symbol
            Log.d("StockDetailsViewModel", "Symbol: $symbol")
            state = state.copy(isLoading = true)
            val companyInfoResult = async { repository.getCompanyInfo(symbol) }
            val intraDayInfoResult = async { repository.getIntraDayInfo(symbol) }

            when (val result = companyInfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        company = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                        company = null
                    )
                }
                else -> Unit
            }

            when (val result = intraDayInfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        stockInfos = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                        company = null
                    )
                }
                else -> Unit
            }
        }
    }

    fun updateStockInfo(timePeriod: String) {
        viewModelScope.launch {
            val symbol = currentSymbol ?: return@launch
            state = state.copy(isLoading = true)
            val result = when (timePeriod) {
                "1W" -> repository.getWeeklyInfo(symbol)
                "1M" -> repository.getMonthlyInfo(symbol)
                "3M" -> repository.getMonthlyInfo(symbol) // Replace with actual method if exists
                "6M" -> repository.getMonthlyInfo(symbol) // Replace with actual method if exists
                "1Y" -> repository.getMonthlyInfo(symbol) // Replace with actual method if exists
                else -> return@launch
            }

            when (result) {
                is Resource.Success -> {
                    state = result.data?.let {
                        state.copy(
                            dailyInfos = it,
                            isLoading = false,
                            error = null
                        )
                    }!!
                }
                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                        company = null
                    )
                }
                else -> Unit
            }
        }
    }
}
