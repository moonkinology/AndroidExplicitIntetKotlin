package dev.moonkin.explicitintents

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val webButton = findViewById<Button>(R.id.btn_web)
        val phoneButton = findViewById<Button>(R.id.btn_phone)
        val smsButton = findViewById<Button>(R.id.btn_sms)
        val geoButton = findViewById<Button>(R.id.btn_geo)
        val navButton = findViewById<Button>(R.id.btn_nav)
        val emailButton = findViewById<Button>(R.id.btn_email)
        val input = findViewById<EditText>(R.id.input)
        val inputPhone = findViewById<EditText>(R.id.phoneInput)


        setupWebIntent(webButton, input)
        setupPhoneCallIntent(phoneButton, input)
        setupSMSIntent(smsButton, input, inputPhone)
        setupGeoIntent(geoButton, input)
        setupNavigationIntent(navButton, input)
        setupEmailIntent(emailButton, input)
    }

    private fun setupEmailIntent(emailButton: Button, input: EditText) {
        emailButton.setOnClickListener {
            composeEmail(input.text.toString(), "E-Mail Subject")
        }
    }

    private fun setupNavigationIntent(
        navButton: Button,
        input: EditText
    ) {
        navButton.setOnClickListener {
            //q=Taronga+Zoo,+Sydney+Australia
            //q=Berlin+Germany
            navigate(input.text.toString())
        }
    }

    private fun setupGeoIntent(geoButton: Button, input: EditText) {
        geoButton.setOnClickListener {
            //geo:0,0?q=1600+Amphitheatre+Parkway%2C+CA
            showMap(input.text.toString())
        }
    }

    private fun setupSMSIntent(
        smsButton: Button,
        input: EditText,
        inputPhone: EditText
    ) {
        smsButton.setOnClickListener {
            composeMmsMessage(input.text.toString(), null, inputPhone.text.toString())
        }
    }

    private fun setupPhoneCallIntent(
        phoneButton: Button,
        input: EditText
    ) {
        phoneButton.setOnClickListener {
            dialPhoneNumber(input.text.toString())
        }
    }

    private fun setupWebIntent(webButton: Button, input: EditText) {
        webButton.setOnClickListener {
            //  https://www.google.com/
            openWebPage(input.text.toString())
        }
    }


    private fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        tryLaunchIntent(intent)
    }

    private fun composeMmsMessage(message: String, attachment: Uri?, phone: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phone")
            putExtra("sms_body", message)
            attachment?.let {
                putExtra(Intent.EXTRA_STREAM, it)
            }
        }
        tryLaunchIntent(intent)
    }

    private fun tryLaunchIntent(intent: Intent) {
        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, "Application not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        tryLaunchIntent(intent)
    }

    private fun showMap(geoLocation: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(geoLocation)
        }
        tryLaunchIntent(intent)
    }

    private fun navigate(geoLocation: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("google.navigation:$geoLocation")
            `package` = "com.google.android.apps.maps"
        }
        tryLaunchIntent(intent)
    }

    private fun composeEmail(addresse: String, subject: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, addresse)
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        tryLaunchIntent(intent)
    }
}