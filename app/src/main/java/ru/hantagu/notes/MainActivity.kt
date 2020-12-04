package ru.hantagu.notes

import android.os.*
import android.app.*
import android.widget.*
import android.content.*
import android.database.*
import ru.hantagu.notes.db.*
import androidx.appcompat.app.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.*

class MainActivity : Activity()
{
    private lateinit var notesDb: NotesDB
    private lateinit var mainNotesList: ListView
    private lateinit var mainButtonAdd: ExtendedFloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notesDb = NotesDB(applicationContext)

        mainNotesList = findViewById(R.id.main_notes_list)
        mainButtonAdd = findViewById(R.id.main_button_add)

        mainButtonAdd.setOnLongClickListener {
            Intent(applicationContext, EditorActivity::class.java).also {
                val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                it.putExtra(Values.ACTION, Values.NEW)
                it.putExtra(Values.TEXT, clipboardManager.primaryClip?.getItemAt(0)?.text.toString())
                startActivity(it)
            }
            return@setOnLongClickListener true
        }

        mainButtonAdd.setOnClickListener {
            startActivity(Intent(applicationContext, EditorActivity::class.java).putExtra(Values.ACTION, Values.NEW))
        }
    }

    override fun onResume()
    {
        super.onResume()

        val arrayList = ArrayList<HashMap<String, String>>()
        val cursor = notesDb.getAllNotes()

        while (cursor.moveToNext())
        {
            HashMap<String, String>().also {
                it[Values.TABLE_COL_ID] = cursor.getString(cursor.getColumnIndex(Values.TABLE_COL_ID))
                it[Values.TABLE_COL_TITLE] = cursor.getString(cursor.getColumnIndex(Values.TABLE_COL_TITLE))
                it[Values.TABLE_COL_TEXT] = cursor.getString(cursor.getColumnIndex(Values.TABLE_COL_TEXT))
                arrayList.add(it)
            }
        }

        SimpleAdapter(applicationContext, arrayList, R.layout.note_list_item, arrayOf(Values.TABLE_COL_ID, Values.TABLE_COL_TITLE, Values.TABLE_COL_TEXT), intArrayOf(R.id.note_item_id, R.id.note_item_title, R.id.note_item_text)).also {
            mainNotesList.adapter = it
            it.notifyDataSetChanged()
        }

        mainNotesList.setOnItemClickListener { _, _, position, _ ->
            Intent(applicationContext, EditorActivity::class.java).also {
                it.putExtra(Values.ACTION, Values.VIEW)
                it.putExtra(Values.TABLE_COL_ID, arrayList[position][Values.TABLE_COL_ID])
                it.putExtra(Values.TABLE_COL_TITLE, arrayList[position][Values.TABLE_COL_TITLE])
                it.putExtra(Values.TABLE_COL_TEXT, arrayList[position][Values.TABLE_COL_TEXT])
                startActivity(it)
            }
        }

        mainNotesList.setOnItemLongClickListener { _, _, position, _ ->
            MaterialAlertDialogBuilder(this).also {
                it.setTitle(getString(R.string.alert_dialog_title))
                it.setMessage(getString(R.string.alert_dialog_message))
                it.setPositiveButton(R.string.yes) { _, _ ->
                    notesDb.deleteNote(arrayList[position][Values.TABLE_COL_ID]!!)
                    onResume()
                }
                it.show()
            }
            return@setOnItemLongClickListener true
        }
    }
}