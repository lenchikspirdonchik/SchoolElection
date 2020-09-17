package school.election

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val firebaseDate = FirebaseDatabase.getInstance()
        val rootReference = firebaseDate.reference
        val classesReference = rootReference.child("Classes")

        btnLoad.setOnClickListener {
            val sc = Scanner(textName.text.toString())
            while (sc.hasNext()) {
                val newClass: String = sc.next()
                val name: String = sc.next()
                val classReference = classesReference.child(newClass)
                val namebaseReference = classReference.child(name)
                namebaseReference.setValue("0")
            }
            textName.setText("")
        }



        btnNull.setOnClickListener {
            val intent = intent
            val classesArray = intent.extras!!.getStringArrayList("arrayOfClass")
            val thread = Thread {
                Log.d("nullDB", "Start")
                if (classesArray != null) {
                    for (oneClass in classesArray) {
                        val classReference = classesReference.child(oneClass)
                        classReference.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val map: Map<String, String> = snapshot.value as Map<String, String>
                                for (i in map.keys) {
                                    val studentReference = classReference.child(i)
                                    studentReference.setValue("0")
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }
                }
                Log.d("nullDB", "Stop")

            }
            thread.start()
            Toast.makeText(this, "База успешно обнулена", Toast.LENGTH_LONG).show()

        }
    }
}