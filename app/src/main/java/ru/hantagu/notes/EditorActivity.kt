package ru.hantagu.notes

import android.content.ClipData
import android.content.ClipboardManager
import android.os.*
import android.view.*
import android.widget.*
import ru.hantagu.notes.db.*
import androidx.appcompat.app.*
import com.google.android.material.appbar.*
import com.google.android.material.button.*


class EditorActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        val notesDB = NotesDB(applicationContext)

        val materialToolbar: MaterialToolbar = findViewById(R.id.editor_toolbar)
        setSupportActionBar(materialToolbar)

        if (!intent.hasExtra(Values.ACTION)) finish()

        when (intent.getStringExtra(Values.ACTION)) {
            Values.NEW -> actionNew(notesDB, materialToolbar)
            Values.EDIT -> actionEdit(notesDB, materialToolbar)
            Values.VIEW -> actionView(materialToolbar)
        }
    }

    private fun actionNew(notesDB: NotesDB, materialToolbar: MaterialToolbar)
    {
        val titleEditText: EditText = findViewById(R.id.editor_new_title)
        val textEditText: EditText = findViewById(R.id.editor_new_text)
        val materialButton: MaterialButton = findViewById(R.id.editor_new_button)

        materialToolbar.title = getString(R.string.title_action_new)

        findViewById<RelativeLayout>(R.id.editor_new).visibility = View.VISIBLE

        materialButton.setOnClickListener {
            notesDB.createNote(titleEditText.text.toString(), textEditText.text.toString())
            finish()
        }
    }

    private fun actionEdit(notesDB: NotesDB, materialToolbar: MaterialToolbar)
    {
        val titleEditText: EditText = findViewById(R.id.editor_edit_title)
        val textEditText: EditText = findViewById(R.id.editor_edit_text)
        val materialButton: MaterialButton = findViewById(R.id.editor_edit_button)

        materialToolbar.title = getString(R.string.title_action_edit)

        findViewById<RelativeLayout>(R.id.editor_edit).visibility = View.VISIBLE

        materialButton.setOnClickListener {
            notesDB.updateNote(intent.getStringExtra(Values.ID)!!, titleEditText.text.toString(), textEditText.text.toString())
            finish()
        }
    }

    private fun actionView(materialToolbar: MaterialToolbar)
    {
        val textView: TextView = findViewById(R.id.editor_view_text)
        val materialButton: MaterialButton = findViewById(R.id.editor_view_button)

        materialToolbar.title = intent.getStringExtra(Values.TITLE)

        findViewById<RelativeLayout>(R.id.editor_view).visibility = View.VISIBLE

        textView.text = intent.getStringExtra(Values.TEXT)

        materialButton.setOnClickListener {
            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.setPrimaryClip(ClipData(ClipData.newPlainText("", textView.text)))
        }
    }
}