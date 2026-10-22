package com.example.tuliamind

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

/**
 * COMPONENT 4: CONTENT PROVIDER.
 * Shares the mood table through a content:// address so other code (or other apps,
 * if exported) can read and write it with a ContentResolver.
 */
class MoodProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.example.tuliamind.provider"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/moods")
    }

    private fun helper() = Db(context!!)

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri, projection: Array<out String>?, selection: String?,
        selectionArgs: Array<out String>?, sortOrder: String?
    ): Cursor? =
        helper().readableDatabase.rawQuery(
            "SELECT id AS _id, score, note, day FROM mood ORDER BY id DESC", null
        )

    override fun getType(uri: Uri): String? = "vnd.android.cursor.dir/vnd.com.example.tuliamind.mood"

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = helper().writableDatabase.insert("mood", null, values)
        return Uri.withAppendedPath(CONTENT_URI, id.toString())
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
}
