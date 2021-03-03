package ndd.com.recorder.listRecord

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import ndd.com.recorder.R
import ndd.com.recorder.database.RecordItem
import ndd.com.recorder.player.PlayerFragment
import ndd.com.recorder.removeDialog.RemoveDialogFragment
import java.io.File
import java.lang.Exception
import java.util.concurrent.TimeUnit

class ListRecordAdapter : RecyclerView.Adapter<ListRecordAdapter.ListRecordViewHolder>() {
    var data = listOf<RecordItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListRecordViewHolder {
        return ListRecordViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ListRecordViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        val recordItem = data[position]
        val itemDuration = recordItem.length
        val minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration)
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(itemDuration) - TimeUnit.MILLISECONDS.toSeconds(minutes)

        holder.vName.text = recordItem.name
        holder.vLength.text = String.format("%02d:%02d", minutes, seconds)

        holder.itemView.setOnClickListener {
            val filePath = recordItem.filePath
            val file = File(filePath)
            if (file.exists()) {
                try {
                    playRecord(filePath, context)
                } catch (e: Exception) {

                }
            } else {
                Toast.makeText(context, "Audio hasn't found", Toast.LENGTH_LONG).show()
            }
        }
        holder.itemView.setOnLongClickListener {
            removeItemDialog(recordItem, context)
            false
        }

    }

    override fun getItemCount(): Int = data.size

    private fun playRecord(filePath: String, context: Context?) {
        val playerFragment = PlayerFragment().newInstance(filePath)
        val transaction: FragmentTransaction = (context as FragmentActivity)
            .supportFragmentManager
            .beginTransaction()
        playerFragment.show(transaction, "dialog_playback")
    }

    private fun removeItemDialog(recordItem: RecordItem, context: Context?) {
        val removeDialogFragment =
            RemoveDialogFragment().newInstance(recordItem.id, recordItem.filePath)
        val transaction = (context as FragmentActivity)
            .supportFragmentManager
            .beginTransaction()
        removeDialogFragment.show(transaction, "dialog_remove")
    }

    class ListRecordViewHolder private constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var vName: TextView = itemView.findViewById(R.id.file_name_text)
        var vLength: TextView = itemView.findViewById(R.id.file_length_text)

        companion object {
            fun from(parent: ViewGroup): ListRecordViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view: View = layoutInflater.inflate(R.layout.list_item_record, parent, false)
                return ListRecordViewHolder(view)
            }
        }
    }
}
