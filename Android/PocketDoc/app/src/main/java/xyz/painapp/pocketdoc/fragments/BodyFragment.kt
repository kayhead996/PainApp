package xyz.painapp.pocketdoc.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import org.json.JSONObject

import xyz.painapp.pocketdoc.R
import xyz.painapp.pocketdoc.activities.BodyActivity
import xyz.painapp.pocketdoc.entities.BodyRegion
import xyz.painapp.pocketdoc.entities.HTTPUrlMethod

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RegionFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RegionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BodyFragment : Fragment(), View.OnClickListener {

    private lateinit var bodyConstraintLayout: ConstraintLayout
    private lateinit var bodyRegionList: ArrayList<BodyRegion>
    private var mListener: OnBodyRegionSelectedListener? = null
    private var front: Boolean = true

    interface OnBodyRegionSelectedListener {
        fun onRegionSelected(bodyRegion: BodyRegion)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            bodyRegionList = arguments.getParcelableArrayList(ARG_BODY_LIST)
            front = arguments.getBoolean(ARG_ORIENTATION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return if (front) {
            inflater!!.inflate(R.layout.fragment_body_front, container, false)
        } else {
            inflater!!.inflate(R.layout.fragment_body_back, container, false)
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bodyConstraintLayout = getView().findViewById(R.id.body_constraintLayout)

        (0..bodyConstraintLayout.childCount)
                .map { bodyConstraintLayout.getChildAt(it) }
                .forEach { (it as? ImageView)?.setOnClickListener(this) }
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnBodyRegionSelectedListener) {
            mListener = context
        } else {
          throw RuntimeException(context!!.toString() + " must implement OnBodyRegionSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    override fun onClick(v: View?) {
        val found: BodyRegion? = (0 until bodyRegionList.size)
                .firstOrNull { bodyRegionList[it].viewId == v?.id }
                ?.let { bodyRegionList[it] }
        if (found != null) {
            mListener!!.onRegionSelected(found)

        }
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_BODY_LIST = "body_list"
        private val ARG_ORIENTATION = "orientation"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @return A new instance of fragment RegionFragment.
         */
        fun newInstance(bodyRegionList: ArrayList<BodyRegion>, orientation: Boolean): BodyFragment {
            val fragment = BodyFragment()
            val args = Bundle()
            args.putParcelableArrayList(ARG_BODY_LIST, bodyRegionList)
            args.putBoolean(ARG_ORIENTATION, orientation)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
