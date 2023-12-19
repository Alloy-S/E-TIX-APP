package com.alloys.e_tix

import android.content.Intent
import android.os.Bundle
import android.support.v4.os.IResultReceiver._Parcel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [profileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class profileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val db = Firebase.firestore
    val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val _changePassword = view?.findViewById<ConstraintLayout>(R.id.changepinProfile)
        _changePassword?.setOnClickListener {
            val intent = Intent(requireContext(), changePassword::class.java)
            startActivity(intent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = Firebase.auth.currentUser!!
        val _nama = view.findViewById<TextView>(R.id.tvNamaProfile)
        val _email = view.findViewById<TextView>(R.id.tvEmailProfile)
        _nama.text = user.displayName
        _email.text = user.email
        val _logout = view.findViewById<ConstraintLayout>(R.id.logoutProfile)
        _logout.setOnClickListener {
            // Create an AlertDialog to ask for confirmation
            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { dialog, which ->
                    // User clicked "Yes," proceed with logout
                    auth.signOut()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                    Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { dialog, which ->
                    // User clicked "No," do nothing
                }
                .show()
        }


        val _delete = view.findViewById<ConstraintLayout>(R.id.deleteProfile)
        _delete.setOnClickListener {
            // Create an AlertDialog to ask for confirmation
            AlertDialog.Builder(requireContext())
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Yes") { dialog, which ->
                    // User clicked "Yes," proceed with account deletion
                    user.delete().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            db.collection("user").document(user.uid).delete()
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            requireActivity().finish()
                            Toast.makeText(requireContext(), "account has been deleted", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Failed to delete account", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("No") { dialog, which ->
                    // User clicked "No," do nothing
                }
                .show()
        }



        val _updateProfile = view.findViewById<ConstraintLayout>(R.id.updateProfile)
        _updateProfile.setOnClickListener {
            val intent = Intent(requireActivity(), updateProfile::class.java)
            startActivity(intent)
        }


        val _history = view.findViewById<ConstraintLayout>(R.id.historyProfile)
        _history.setOnClickListener {
            val intent = Intent(requireContext(), History::class.java)
            startActivity(intent)
        }
        val _profile = view.findViewById<ConstraintLayout>(R.id.historyProfile)
        _profile.setOnClickListener{
            val intent = Intent(requireActivity(), History::class.java)
            startActivity(intent)
        }



        _nama.text = user.displayName
        _email.text = user.email

        val _changePass = view.findViewById<ConstraintLayout>(R.id.changepinProfile)
        _changePass.setOnClickListener {
            val intent = Intent(requireActivity(), changePassword::class.java)
            startActivity(intent)
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment profileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            profileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}