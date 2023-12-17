package com.alloys.e_tix

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class updateProfile : AppCompatActivity() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val _newName = findViewById<EditText>(R.id.etupdateNama)
        val _newEmail = findViewById<EditText>(R.id.etupdateEmail)
        val _updateBtn = findViewById<Button>(R.id.btnUpdate)

            _updateBtn.setOnClickListener {
                val name = _newName.text.toString()
                val email = _newEmail.text.toString()

                if (name.isNotEmpty() && email.isNotEmpty() && email != user?.email) {
                    showPasswordPrompt(this) { password ->
                        val credential = EmailAuthProvider.getCredential(user?.email.toString(), password)
                        user?.reauthenticate(credential)?.addOnCompleteListener { reauthTask ->
                            if (reauthTask.isSuccessful) {
                                showToast("Reautentikasi berhasil")

                                    user!!.verifyBeforeUpdateEmail(email).addOnCompleteListener { emailUpdateTask ->
                                        if (emailUpdateTask.isSuccessful) {
                                            showToast("Cek email baru untuk verifikasi")
                                            auth.signOut()
                                            val intent = Intent(this@updateProfile, MainActivity::class.java)
                                            startActivity(intent)
                                        } else {
                                            val exception = emailUpdateTask.exception
                                            showToast("Gagal memperbarui email: ${exception?.message}")
                                            Log.d("emailku rusakk", "Gagal memperbarui email: ${exception?.message}")
                                        }
                                    }
                                // Update Profile
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build()

                                user?.updateProfile(profileUpdates)?.addOnCompleteListener { profileUpdateTask ->
                                    if (profileUpdateTask.isSuccessful) {
                                        showToast("Nama pengguna berhasil diperbarui")
                                        // Kembali ke FragmentProfile
                                        onBackPressed()
                                    } else {
                                        val exception = profileUpdateTask.exception
                                        showToast("Gagal memperbarui nama pengguna: ${exception?.message}")
                                    }
                                }
                            } else {
                                showToast("Gagal reautentikasi: ${reauthTask.exception?.message}")
                                // Handle reauthentication failure
                            }
                        }
                    }
                } else {
                    showToast("Nama dan email tidak boleh kosong")
                }
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
