package com.alloys.e_tix

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.firestore

class updateProfile : AppCompatActivity() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val db = Firebase.firestore
        val _newName = findViewById<EditText>(R.id.etupdateNama)
        val _newEmail = findViewById<EditText>(R.id.etupdateEmail)
        val _updateBtn = findViewById<Button>(R.id.btnUpdate)

        _newName.setText(user?.displayName)
        _newEmail.setText(user?.email)
        _updateBtn.setOnClickListener {
            val name = _newName.text.toString()
            val email = _newEmail.text.toString()

            if (name != user?.displayName || email != user?.email) {
                // Ada perubahan, lanjutkan dengan proses update

                // Update Profile
                val profileUpdatesBuilder = UserProfileChangeRequest.Builder()

                if (name != user?.displayName) {
                    profileUpdatesBuilder.setDisplayName(name)
                }
                val data = hashMapOf(
                    "full_name" to name,
                )

                db.collection("users").document(user!!.uid).update(data as Map<String, Any>)
                    .addOnSuccessListener {
                        Log.d("UPDATED DATA", "success")
                    }

                if (email != user?.email) {
                    showPasswordPrompt(this) { password ->
                        val credential =
                            EmailAuthProvider.getCredential(user?.email.toString(), password)
                        user?.reauthenticate(credential)?.addOnCompleteListener { reauthTask ->
                            if (reauthTask.isSuccessful) {
                                showToast("Reautentikasi berhasil")

                                user!!.verifyBeforeUpdateEmail(email)
                                    .addOnCompleteListener { emailUpdateTask ->
                                        if (emailUpdateTask.isSuccessful) {
                                            showToast("Cek email baru untuk verifikasi")
                                            auth.signOut()
                                            val intent =
                                                Intent(this@updateProfile, MainActivity::class.java)
                                            startActivity(intent)
                                        } else {
                                            val exception = emailUpdateTask.exception
                                            showToast("Gagal memperbarui email: ${exception?.message}")
                                            Log.d(
                                                "emailku rusakk",
                                                "Gagal memperbarui email: ${exception?.message}"
                                            )
                                        }
                                    }
                            } else {
                                showToast("Gagal reautentikasi: ${reauthTask.exception?.message}")
                            }
                        }
                    }
                }

                user?.updateProfile(profileUpdatesBuilder.build())
                    ?.addOnCompleteListener { profileUpdateTask ->
                        if (profileUpdateTask.isSuccessful) {
                            showToast("Data berhasil diperbarui")
                            // Kembali ke FragmentProfile
                            onBackPressed()
                        } else {
                            val exception = profileUpdateTask.exception
                            showToast("Gagal memperbarui data: ${exception?.message}")
                        }
                    }
            } else {
                showToast("Tidak ada perubahan yang dilakukan")
            }
        }
        val _btnBack = findViewById<ImageView>(R.id.btnBackUpdate)
        _btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showPasswordPrompt(context: Context, onPasswordEntered: (String) -> Unit) {
        val passwordInput = EditText(context)
        passwordInput.hint = "Masukkan kata sandi"

        AlertDialog.Builder(context)
            .setTitle("Reautentikasi")
            .setMessage("Masukkan kata sandi untuk melanjutkan")
            .setView(passwordInput)
            .setPositiveButton("OK") { _, _ ->
                val password = passwordInput.text.toString()
                onPasswordEntered(password)
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
