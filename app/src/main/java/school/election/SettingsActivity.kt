package school.election

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        btnLoad.setOnClickListener {
            val sc = Scanner(textName.text.toString())
            val firebaseDate = FirebaseDatabase.getInstance()
            val rootReference = firebaseDate.reference
            val  classesReference =  rootReference.child("Classes")
            while (sc.hasNext()) {
                val newClass: String = sc.next()
                val name: String = sc.next()
                val classReference = classesReference.child(newClass)
                val namebaseReference = classReference.child(name)
                namebaseReference.setValue("0")
            }
            textName.setText("")
        }


    }
}