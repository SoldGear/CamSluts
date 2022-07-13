package cast.camsluts.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cast.camsluts.models.Model
import cast.camsluts.sites.Chaturbate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModel : ViewModel() {
    private var items: MutableLiveData<MutableList<Model>> = MutableLiveData()

    private val repo = Chaturbate()

    fun fetchData(url:String, page:Int): MutableLiveData<MutableList<Model>> {
        viewModelScope.launch(Dispatchers.IO) {
            items.postValue(repo.parseCams(url + page))
        }
        return items
    }}