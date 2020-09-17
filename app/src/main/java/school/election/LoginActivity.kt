package school.election

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var msp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val firebaseDate = FirebaseDatabase.getInstance()
        val rootReference = firebaseDate.reference
        val passReference = rootReference.child("Password")
        msp = this.getSharedPreferences("Password", Context.MODE_PRIVATE)
        var failed = 1
        var password = ""
        passReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                password = snapshot.getValue(String::class.java)!!
            }

            override fun onCancelled(error: DatabaseError) {}
        })


        btnPass.setOnClickListener {

            if (msp.contains(KEY_FAILED)) failed = msp.getInt(KEY_FAILED, 0)

            if (txtPass.text.toString().equals(password) && failed < 3) {
                Log.d("settings", "true")
                val mintent = Intent()
                setResult(Activity.RESULT_OK, mintent)
                finish()
            } else {
                failed++
                val editor = msp.edit()
                editor.putInt(KEY_FAILED, failed)
                editor.apply()
            }


        }
    }

    companion object {
        private const val KEY_FAILED = "failed"
    }

}
