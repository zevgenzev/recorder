package ndd.com.recorder.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecordDatabaseDao {
    @Insert
    fun insert(record: RecordItem)

    @Update
    fun update(record: RecordItem)

    @Query("SELECT * from record_table WHERE id=:key")
    fun getRecord(key: Long?): RecordItem?

    @Query("DELETE FROM record_table")
    fun clearAll()

    @Query("DELETE FROM record_table WHERE id=:key")
    fun removeRecord(key: Long?)

    @Query("SELECT * FROM record_table ORDER BY id DESC")
    fun getAllRecords(): LiveData<MutableList<RecordItem>>

    @Query("SELECT COUNT(*) FROM record_table")
    fun getCount(): Int
}