package com.example.tuliamind

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class JournalEntry(val id: Long, val title: String, val body: String, val created: String)

data class Appointment(
    val id: Long, val name: String, val counsellor: String, val county: String,
    val date: String, val time: String, val mode: String, val topics: String, val notes: String
)

fun sha256(s: String): String =
    MessageDigest.getInstance("SHA-256").digest(s.toByteArray()).joinToString("") { "%02x".format(it) }

private fun fmt() = SimpleDateFormat("yyyy-MM-dd", Locale.US)
fun today(): String = fmt().format(Date())

private const val CREATE_APPOINTMENT =
    "CREATE TABLE IF NOT EXISTS appointment(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, " +
        "counsellor TEXT, county TEXT, appt_date TEXT, appt_time TEXT, appt_mode TEXT, topics TEXT, notes TEXT)"

/** The app's local SQLite database. Nothing leaves the phone. */
class Db(context: Context) : SQLiteOpenHelper(context, "tulia.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE mood(id INTEGER PRIMARY KEY AUTOINCREMENT, score INTEGER, note TEXT, day TEXT)")
        db.execSQL("CREATE TABLE journal(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, body TEXT, created TEXT)")
        db.execSQL("CREATE TABLE streak(substance TEXT PRIMARY KEY, start_date TEXT)")
        db.execSQL("CREATE TABLE trigger_log(id INTEGER PRIMARY KEY AUTOINCREMENT, trigger_name TEXT, day TEXT)")
        db.execSQL(CREATE_APPOINTMENT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) db.execSQL(CREATE_APPOINTMENT)
    }

    // ---------- Mood ----------
    fun addMood(score: Int, note: String) {
        val v = ContentValues().apply { put("score", score); put("note", note); put("day", today()) }
        writableDatabase.insert("mood", null, v)
    }

    fun lastMoods(n: Int): List<Pair<String, Int>> {
        val out = mutableListOf<Pair<String, Int>>()
        readableDatabase.rawQuery("SELECT day, score FROM mood ORDER BY id DESC LIMIT ?", arrayOf(n.toString())).use { c ->
            while (c.moveToNext()) out.add(c.getString(0) to c.getInt(1))
        }
        return out.reversed()
    }

    // ---------- Journal ----------
    fun addJournal(title: String, body: String) {
        val v = ContentValues().apply { put("title", title); put("body", body); put("created", today()) }
        writableDatabase.insert("journal", null, v)
    }

    fun journals(): List<JournalEntry> {
        val out = mutableListOf<JournalEntry>()
        readableDatabase.rawQuery("SELECT id, title, body, created FROM journal ORDER BY id DESC", null).use { c ->
            while (c.moveToNext()) out.add(JournalEntry(c.getLong(0), c.getString(1), c.getString(2), c.getString(3)))
        }
        return out
    }

    fun deleteJournal(id: Long) {
        writableDatabase.delete("journal", "id=?", arrayOf(id.toString()))
    }

    // ---------- Sober streak ----------
    fun startStreak(substance: String) {
        val v = ContentValues().apply { put("substance", substance); put("start_date", today()) }
        writableDatabase.insertWithOnConflict("streak", null, v, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun resetStreak(substance: String) {
        writableDatabase.delete("streak", "substance=?", arrayOf(substance))
    }

    fun streakDays(substance: String): Int? {
        readableDatabase.rawQuery("SELECT start_date FROM streak WHERE substance=?", arrayOf(substance)).use { c ->
            if (c.moveToFirst()) {
                val start = fmt().parse(c.getString(0))!!
                return ((Date().time - start.time) / 86_400_000L).toInt()
            }
        }
        return null
    }

    // ---------- Triggers ----------
    fun addTrigger(name: String) {
        val v = ContentValues().apply { put("trigger_name", name); put("day", today()) }
        writableDatabase.insert("trigger_log", null, v)
    }

    fun topTriggers(): List<Pair<String, Int>> {
        val out = mutableListOf<Pair<String, Int>>()
        readableDatabase.rawQuery(
            "SELECT trigger_name, COUNT(*) AS c FROM trigger_log GROUP BY trigger_name ORDER BY c DESC LIMIT 5", null
        ).use { c ->
            while (c.moveToNext()) out.add(c.getString(0) to c.getInt(1))
        }
        return out
    }

    // ---------- Appointments ----------
    fun addAppointment(
        name: String, counsellor: String, county: String, date: String,
        time: String, mode: String, topics: String, notes: String
    ) {
        val v = ContentValues().apply {
            put("name", name); put("counsellor", counsellor); put("county", county)
            put("appt_date", date); put("appt_time", time); put("appt_mode", mode)
            put("topics", topics); put("notes", notes)
        }
        writableDatabase.insert("appointment", null, v)
    }

    fun appointments(): List<Appointment> {
        val out = mutableListOf<Appointment>()
        readableDatabase.rawQuery(
            "SELECT id, name, counsellor, county, appt_date, appt_time, appt_mode, topics, notes " +
                "FROM appointment ORDER BY appt_date, appt_time", null
        ).use { c ->
            while (c.moveToNext()) {
                out.add(
                    Appointment(
                        c.getLong(0), c.getString(1), c.getString(2), c.getString(3),
                        c.getString(4), c.getString(5), c.getString(6), c.getString(7), c.getString(8)
                    )
                )
            }
        }
        return out
    }

    fun deleteAppointment(id: Long) {
        writableDatabase.delete("appointment", "id=?", arrayOf(id.toString()))
    }
}
