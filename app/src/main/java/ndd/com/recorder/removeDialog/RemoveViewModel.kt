package ndd.com.recorder.removeDialog

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ndd.com.recorder.R
import ndd.com.recorder.database.RecordDatabase
import ndd.com.recorder.database.RecordDatabaseDao
import java.io.File
import java.lang.Exception

class RemoveViewModel(
    private var databaseDao: RecordDatabaseDao,
    private var application: Application
) : ViewModel() {

    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    fun removeItem(itemId: Long) {
//        databaseDao = RecordDatabase.getInstance(application).recordDatabaseDao
        try {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    databaseDao.removeRecord(itemId)
                }
            }
        } catch (e: Exception) {
            Log.e("RemoveViewModel", "removeItem exception", e)
        }
    }

    fun removeFile(path: String) {
        val file = File(path)
        if (file.exists()) {
            file.delete()
            Toast.makeText(
                application,
                contextR.string.toast_file_deleted,
                Toast.LENGTH_LONG
            ).show()

        }
    }
}