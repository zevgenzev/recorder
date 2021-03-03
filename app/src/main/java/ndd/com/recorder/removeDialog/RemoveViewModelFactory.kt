package ndd.com.recorder.removeDialog

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ndd.com.recorder.database.RecordDatabaseDao

class RemoveViewModelFactory(
    private val databaseDao: RecordDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemoveViewModel::class.java)) {
            return RemoveViewModel(databaseDao, application) as T
        }
        throw IllegalArgumentException("unknown viewModel class")
    }
}