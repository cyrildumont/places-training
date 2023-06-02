package com.example.places

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    lateinit var autocompleteAddress : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Places.initialize(applicationContext, "<API_KEY>")

        autocompleteAddress = findViewById<EditText>(R.id.autocompleteAddress)
        autocompleteAddress.setOnClickListener {
            startAutocompleteIntent()
        }
    }

    private val startAutocomplete = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    autocompleteAddress.setText(place.address)
                    Log.d(TAG, "Place: " + place.addressComponents)
                }
            } else if (result.resultCode == RESULT_CANCELED) {
                Log.i(TAG, "User canceled autocomplete")
            }
        } as ActivityResultCallback<ActivityResult>)

    private fun startAutocompleteIntent() {

        val fields: List<Place.Field> = Arrays.asList(
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.LAT_LNG, Place.Field.VIEWPORT
        )

        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .setCountries(Arrays.asList("FR"))
            .setTypesFilter(object : ArrayList<String?>() {
                init {
                    add(TypeFilter.ADDRESS.toString().lowercase(Locale.getDefault()))
                }
            })
            .build(this)
        startAutocomplete.launch(intent)
    }



}