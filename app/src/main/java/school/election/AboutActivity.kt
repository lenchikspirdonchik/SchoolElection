package school.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        txtVK.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/leonid.spiri"))
            startActivity(browserIntent)
        }
        txtIn.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.linkedin.com/in/leonid-spiridonov-424a601ab/")
            )
            startActivity(browserIntent)
        }
        txtGmail.setOnClickListener {
            val emailIntent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "spiridonov.production@gmail.com", null
                )
            )
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "О приложении школьных выборов")
            startActivity(Intent.createChooser(emailIntent, "Отправка сообщения"))
        }


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}