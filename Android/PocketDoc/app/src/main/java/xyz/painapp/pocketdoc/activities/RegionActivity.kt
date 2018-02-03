package xyz.painapp.pocketdoc.activities

import android.app.Fragment
import android.app.FragmentManager
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import org.json.JSONObject
import xyz.painapp.pocketdoc.R
import xyz.painapp.pocketdoc.entities.BodyRegion
import xyz.painapp.pocketdoc.entities.DownloadDataTask
import xyz.painapp.pocketdoc.entities.HTTPUrlMethod
import xyz.painapp.pocketdoc.fragments.LoadingFragment
import xyz.painapp.pocketdoc.fragments.RegionFragment

class RegionActivity : AppCompatActivity() {
    private lateinit var bodyRegion: BodyRegion
   /* private var symptomsListView: ListView? = null
    private var regionImageView: ImageView? = null*/
    private var fManager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_region)

        // Set toolbar
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        bodyRegion = intent.getParcelableExtra(BodyRegion.BODY_REGION_STR)


        fManager = fragmentManager


        DownloadRegionInfoTask().execute(HTTPUrlMethod(
                HTTPUrlMethod.BODY_REGION_URL,
                HTTPUrlMethod.POST,
                bodyRegion.toJSONObject()
                )
        )

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.body_activity_options_menu, menu)
        return true
    }



    inner class DownloadRegionInfoTask: DownloadDataTask() {
        override fun onPreExecute() {
            val transaction = fManager!!.beginTransaction()
            val newFragment = LoadingFragment()
            transaction.replace(R.id.region_fragment_container, newFragment)
            transaction.commit()
        }

        override fun onPostExecute(result: JSONObject?) {
            val transaction = fManager!!.beginTransaction()
            val newFragment: Fragment? = if (!result!!.has(HTTPUrlMethod.RESPONSE_CODE_STR)) {
                RegionFragment.newInstance(BodyRegion(result))
            } else {
                LoadingFragment.newInstance(getString(R.string.error_server_message))
            }


            transaction.replace(R.id.region_fragment_container, newFragment!!)
            transaction.commit()
        }

    }
/*

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v) {
            regionImageView -> return imgButtonTouch(v, event)
            else -> v!!.performClick()
        }
        return true
    }

    private fun getSymptom(x: Int, y: Int): String {
        return "test"
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val symptom = symptomsListView!!.getItemAtPosition(position) as String
        val intent = Intent(this, CausesActivity::class.java)

        intent.putExtra("Cause", symptom)
        startActivity(intent)
    }

    private fun imgButtonTouch(v: View?, event: MotionEvent?): Boolean {
        val x : Int = event!!.x.toInt()
        val y : Int = event.y.toInt()
        val intent = Intent(this, CausesActivity::class.java)

        val symptom = getSymptom(x, y)


        intent.putExtra("Cause", symptom)
        startActivity(intent)

        v!!.performClick()

        return true
    }*/

}
