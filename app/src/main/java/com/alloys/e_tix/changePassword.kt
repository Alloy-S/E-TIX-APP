package com.alloys.e_tix

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.alloys.e_tix.databinding.ActivityChangePasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class changePassword : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var etPasswordChange: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        etPasswordChange = findViewById(R.id.etPasswordChange)
        val btnUpdate = findViewById<Button>(R.id.btnChange)
        firebaseAuth = FirebaseAuth.getInstance()

        // Pastikan pengguna sudah login
        val currentUser: FirebaseUser? = firebaseAuth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Anda belum login", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        btnUpdate.setOnClickListener {
            // Tampilkan pop-up untuk meminta kata sandi lama
            showOldPasswordDialog(currentUser)
        }
        binding.btnBackChangePass.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showOldPasswordDialog(currentUser: FirebaseUser) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Masukkan Kata Sandi Lama")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
            val oldPassword = input.text.toString().trim()

            // Validasi kata sandi lama
            if (oldPassword.isEmpty()) {
                Toast.makeText(this, "Masukkan kata sandi lama", Toast.LENGTH_SHORT).show()
            } else {
                // Authentikasi kata sandi lama
                val credential = currentUser.email?.let { firebaseAuth.signInWithEmailAndPassword(it, oldPassword) }

                credential?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Jika autentikasi berhasil, lanjutkan ke proses update password
                        updatePassword(currentUser)
                    } else {
                        Toast.makeText(this, "Kata sandi lama salah", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        builder.setNegativeButton("Batal", DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })

        builder.show()
    }

    private fun updatePassword(currentUser: FirebaseUser) {
        val newPassword = etPasswordChange.text.toString().trim()

        // Validasi kata sandi baru
        if (newPassword.isEmpty()) {
            Toast.makeText(this, "Masukkan kata sandi baru", Toast.LENGTH_SHORT).show()
            return
        }

        // Update kata sandi pengguna
        currentUser.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Kata sandi berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Gagal memperbarui kata sandi", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
