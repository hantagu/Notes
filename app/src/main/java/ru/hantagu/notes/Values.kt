package ru.hantagu.notes

import android.provider.BaseColumns

object Values
{
    const val ID = "id"
    const val TITLE = "title"
    const val TEXT = "text"

    const val ACTION = "action"
    const val NEW = "new"
    const val EDIT = "edit"
    const val VIEW = "view"

    const val DB_VERSION = 1
    const val DB_NAME = "Notes"

    const val TABLE_NAME = "Notes"
    const val TABLE_COL_ID = BaseColumns._ID
    const val TABLE_COL_TITLE = "title"
    const val TABLE_COL_TEXT = "text"
}