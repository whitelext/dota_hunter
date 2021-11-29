package com.whitelext.dotaHunter.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.example.ItemsListQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.repository.ItemsRepository
import com.whitelext.dotaHunter.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _itemsLiveData by lazy { MutableLiveData<List<ItemsListQuery.Item>>() }
    val itemsLiveData: LiveData<List<ItemsListQuery.Item>> = _itemsLiveData

    init {
        getItems()
    }

    fun getItemById(itemId: Int): ItemsListQuery.Item? {
        return _itemsLiveData.value?.first { it.id == itemId }
    }

    private fun getItems() {
        Utils.asyncCall(
            coroutineScope = viewModelScope,
            destinationFunction = ::performGetItems
        ).invoke()
    }

    private suspend fun performGetItems() {
        when (val response = itemsRepository.getItems()) {
            is Resource.Success -> {
                _itemsLiveData.postValue(response.data)
            }
            is Resource.Error -> {
                Toast.makeText(getApplication(), response.error.message, Toast.LENGTH_SHORT).show()
            }
            else -> {
                // TODO: same ui notification
            }
        }
    }
}
