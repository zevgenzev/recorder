package ndd.com.recorder.listRecord

import androidx.lifecycle.ViewModel
import ndd.com.recorder.database.RecordDatabaseDao

class ListRecordViewModel(dataSource: RecordDatabaseDao) : ViewModel() {
    val database = dataSource
    val records = dataSource.getAllRecords()
}