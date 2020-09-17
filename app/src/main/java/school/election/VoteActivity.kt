package school.election

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_vote.*


class VoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote)
        val intent = intent
        val mAuth = FirebaseAuth.getInstance()
        val name = intent.extras!!.getString("name")
        val classes = intent.extras!!.getString("classes")
        val realName = intent.extras!!.getString("realName")
        val firebaseDate = FirebaseDatabase.getInstance()
        val rootReference = firebaseDate.reference
        val classesReference = rootReference.child("Classes")

        if (name != null && classes != null && realName != null) txtHead.text =
            txtHead.text.toString() + realName

        btnVote.setOnClickListener {
            if (name != null && classes != null && realName != null) {
                val classReference = classesReference.child(classes)
                val nameReference = classReference.child(name)
                val index: Int = radioG.indexOfChild(findViewById(radioG.checkedRadioButtonId))
                nameReference.setValue(index.toString())
                mAuth.signOut()
                val voteReference = rootReference.child("Vote").child(index.toString())
                voteReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val chet = snapshot.getValue(String::class.java)!!
                        var chetInt = chet.toInt()
                        chetInt++
                        voteReference.setValue(chetInt)
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })


                val pDialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
                pDialog.titleText = "Вы успешно проголосовали"
                pDialog.contentText = "Ваш голос важен для нас!"
                pDialog.confirmText = "Готово"
                pDialog.setCancelable(false)
                pDialog.setConfirmClickListener {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                pDialog.progressHelper.spin()
                pDialog.show()

            }
        }


    }
}