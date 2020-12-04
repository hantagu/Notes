package ru.hantagu.notes.db

import android.content.*
import android.database.*
import ru.hantagu.notes.Values
import android.database.sqlite.*


class NotesDB(context: Context?) : SQLiteOpenHelper(context, Values.DB_NAME, null, Values.DB_VERSION)
{

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE ${Values.TABLE_NAME} (${Values.TABLE_COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${Values.TABLE_COL_TITLE} TEXT, ${Values.TABLE_COL_TEXT} TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${Values.TABLE_NAME}")
        onCreate(db)
    }

    fun getAllNotes(): Cursor {
        return readableDatabase.rawQuery("SELECT * FROM ${Values.TABLE_NAME}", null)
    }

    fun createNote(title: String, text: String) {
        val values = ContentValues()
        values.put("title", title)
        values.put("text", text)
        writableDatabase.insert(Values.TABLE_NAME, null, values)
    }

    fun updateNote(id: String, newTitle: String, newText: String) {
        writableDatabase.execSQL("UPDATE ${Values.TABLE_NAME} SET ${Values.TABLE_COL_TITLE} = $newTitle, ${Values.TABLE_COL_TEXT} = $newText WHERE ${Values.TABLE_COL_ID} = $id")
    }

    fun deleteNote(id: String) {
        writableDatabase.delete(Values.TABLE_NAME, "${Values.TABLE_COL_ID}=$id", null)
        if (getCount() == 0) {
            writableDatabase.execSQL("DROP TABLE IF EXISTS ${Values.TABLE_NAME}")
            onCreate(writableDatabase)
        }
    }

    private fun getCount(): Int {
        val cursor: Cursor = readableDatabase.rawQuery("SELECT * FROM ${Values.TABLE_NAME}", null)
        val count: Int = cursor.count
        cursor.close()
        return count
    }
}