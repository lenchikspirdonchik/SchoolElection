package school.election

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var phoneVerificationId: String
    private lateinit var classes: String
    private val classesArray: ArrayList<String> = arrayListOf<String>()
    private var name = ""
    private var surname = ""
    private lateinit var snap: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val firebaseDate = FirebaseDatabase.getInstance()
        val rootReference = firebaseDate.reference
        val classesReference = rootReference.child("Classes")
        classesReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val map: Map<String, String> = snapshot.value as Map<String, String>
                for (i in map.keys) classesArray.add(i)

                val adaptermain: ArrayAdapter<String> =
                    ArrayAdapter<String>(
                        this@MainActivity,
                        R.layout.support_simple_spinner_dropdown_item,
                        classesArray
                    )
                adaptermain.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerClass.adapter = adaptermain
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        mAuth = FirebaseAuth.getInstance()

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Log.d("auth", "onVerificationCompleted")
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("auth", p0.toString())
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                phoneVerificationId = verificationId
                layoutCode.visibility = View.VISIBLE

                Log.d("auth", "onCodeSent")


            }
        }


        btnStart.setOnClickListener {
            classes = spinnerClass.selectedItem.toString()
            name = txtName.text.toString()
            surname = txtSurname.text.toString()
            if (name.length > 1) name = name[0].toUpperCase().toString()
            if (surname.length > 1) surname = surname[0].toUpperCase().toString()
            snap = "-"
            name = "$name$surname"
            if (name.isNotEmpty()) {
                val classReference = classesReference.child(classes)
                val nameReference = classReference.child(name)

                nameReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val buff: String? = snapshot.getValue(String::class.java)
                        if (buff != null) snap = snapshot.getValue(String::class.java)!!
                        else Toast.makeText(
                            applicationContext,
                            "Данного пользователя не найдено",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("auth", "snapname = $snap")
                        if (checkPhoneIsValid(txtPhone.text.toString()) && (snap == "0" || snap == "1" || snap == "2")) {

                            mAuth.useAppLanguage()

                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                txtPhone.text.toString(),
                                60,
                                TimeUnit.SECONDS,
                                this@MainActivity,
                                callbacks
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            } else Toast.makeText(this, "Пожалуйста введите имя", Toast.LENGTH_LONG).show()
        }


        btnCode.setOnClickListener {
            val credential = PhoneAuthProvider.getCredential(
                phoneVerificationId,
                txtCode.text.toString()
            )
            signInWithPhoneAuthCredential(credential)
        }

    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("auth", "signInWithCredential:success")
                    val user = task.result?.user
                    if (user != null) {
                        val creationTimestamp = user.metadata?.creationTimestamp
                        val lastSignInTimestamp = user.metadata?.lastSignInTimestamp
                        if (creationTimestamp == lastSignInTimestamp) {
                            Log.d(
                                "authSuccess", "your class is $classes \n" +
                                        "your name is $name\n" +
                                        "your phone is ${user.phoneNumber}\n" +
                                        "your vote in DB is $snap"
                            )
                            val intent = Intent(this, VoteActivity::class.java)
                            intent.putExtra("name", name)
                            intent.putExtra("classes", classes)
                            val realName =
                                txtName.text.toString() + " " + txtSurname.text.toString()
                            intent.putExtra("realName", realName)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this,
                                "Вы не можете войти второй раз!",
                                Toast.LENGTH_LONG
                            ).show()

                        }


                    }
                } else {
                    Log.w("auth", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    }
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_settings -> {
                val mintent = Intent(this, SettingsActivity::class.java)
                mintent.putExtra("arrayOfClass", classesArray)
                startActivity(mintent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}