package school.election

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_statistics.*
import org.eazegraph.lib.models.PieModel

class StatisticsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val colors = arrayOf(
            "#FE6DA8",
            "#56B7F1",
            "#CDA67F",
            "#FED70E",
            "#45D09E",
            "#00848C",
            "#41B619",
            "#8EAF0C",
            "#FFD600"
        )
        val firebaseDate = FirebaseDatabase.getInstance()
        val rootReference = firebaseDate.reference
        val vote = arrayOf("За Mac", "За собак", "За кошек")
        for (i in 0..vote.lastIndex) {
            val voteReference = rootReference.child("Vote").child(i.toString())
            voteReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val keys = snapshot.getValue(String::class.java)
                    if (keys != null) {
                        piechart.addPieSlice(
                            PieModel(
                                vote[i],
                                keys.toFloat(),
                                Color.parseColor(colors[i])
                            )
                        )
                        if (i == vote.size - 1) piechart.startAnimation()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
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