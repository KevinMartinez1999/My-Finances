package com.example.myfinances.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.myfinances.data.server.UserServer
import com.example.myfinances.databinding.FragmentAjustesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class AjustesFragment : Fragment() {
    private var _binding: FragmentAjustesBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if(result.resultCode== Activity.RESULT_OK){
            val data: Intent? = result.data
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.profileImageView.setImageBitmap(imageBitmap)
            uploadPhoto()
        }
    }

    private fun uploadPhoto() {
        val currentUser = auth.currentUser
        val storage = FirebaseStorage.getInstance()
        val pictureRef = storage.reference.child(currentUser?.uid.toString())

        binding.profileImageView.isDrawingCacheEnabled = true
        binding.profileImageView.buildDrawingCache()
        val bitmap = (binding.profileImageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = pictureRef.putBytes(data)
        uploadTask.continueWithTask { task ->
            if (task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            pictureRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = task.result.toString()
                auth = Firebase.auth
                val db = Firebase.firestore
                val id = currentUser?.uid.toString()
                val documentUpdate = HashMap<String, Any>()
                documentUpdate["picture"] = downloadUrl
                db.collection("users").document(id).update(documentUpdate).addOnSuccessListener {
                    Toast.makeText(requireContext(), "Fotografía actualizada con éxito", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAjustesBinding.inflate(inflater, container, false)

        auth = Firebase.auth
        val db = Firebase.firestore
        val currentUser = auth.currentUser
        if(currentUser != null){
            db.collection("users").get().addOnSuccessListener { result ->
                for(document in result){
                    val user = document.toObject<UserServer>()
                    if(user.id == currentUser.uid){
                        Picasso.get().load(user.picture).into(binding.profileImageView)
                        binding.profileUsername.text = user.nickname
                    }
                }
            }
        }

        with(binding){
            addPhotoButton.setOnClickListener {
                photointent()
            }
        }

        return binding.root
    }

    private fun photointent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
