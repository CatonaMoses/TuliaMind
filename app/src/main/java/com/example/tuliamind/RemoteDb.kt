package com.example.tuliamind

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Helpline(
    val name: String,
    val number: String,
    val category: String,
    val description: String
)

/**
 * The app's cloud database (Firebase Firestore).
 * Mood, journal and streak data are NOT stored here. They stay on the phone in Db.kt.
 * Only helplines (read) and counsellor appointments (write) use the cloud.
 */
object RemoteDb {
    private val auth get() = FirebaseAuth.getInstance()
    private val firestore get() = FirebaseFirestore.getInstance()

    /** Anonymous sign-in: no email, no password. Gives the app a private random ID. */
    fun ensureSignedIn(onReady: (String) -> Unit, onError: (String) -> Unit) {
        val existing = auth.currentUser
        if (existing != null) {
            onReady(existing.uid)
            return
        }
        auth.signInAnonymously()
            .addOnSuccessListener { result -> onReady(result.user?.uid ?: "") }
            .addOnFailureListener { e -> onError(e.message ?: "Could not sign in") }
    }

    /** Reads the helplines collection. Firestore caches it, so it also works offline after the first load. */
    fun loadHelplines(onResult: (List<Helpline>) -> Unit, onError: (String) -> Unit) {
        firestore.collection("helplines").get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.map { d ->
                    Helpline(
                        name = d.getString("name") ?: "",
                        number = d.getString("number") ?: "",
                        category = d.getString("category") ?: "",
                        description = d.getString("description") ?: ""
                    )
                }
                onResult(list)
            }
            .addOnFailureListener { e -> onError(e.message ?: "Could not load helplines") }
    }

    /** Saves a counsellor booking to the cloud. Requires the user's consent (enforced by the security rules). */
    fun addAppointment(
        name: String, counsellor: String, county: String, date: String,
        time: String, mode: String, topics: String, notes: String,
        consent: Boolean,
        onDone: () -> Unit, onError: (String) -> Unit
    ) {
        ensureSignedIn(
            onReady = { uid ->
                val data = hashMapOf(
                    "uid" to uid,
                    "name" to name,
                    "counsellor" to counsellor,
                    "county" to county,
                    "appt_date" to date,
                    "appt_time" to time,
                    "appt_mode" to mode,
                    "topics" to topics,
                    "notes" to notes,
                    "consent" to consent,
                    "status" to "pending",
                    "created_at" to System.currentTimeMillis()
                )
                firestore.collection("appointments").add(data)
                    .addOnSuccessListener { onDone() }
                    .addOnFailureListener { e -> onError(e.message ?: "Could not save booking") }
            },
            onError = onError
        )
    }
}

