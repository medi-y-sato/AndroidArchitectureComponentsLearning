package jp.mediba.ysato.androidarchitecturecomponentslearning

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MastodonFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MastodonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MastodonFragment : Fragment(), View.OnClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view:View = inflater!!.inflate(R.layout.fragment_mastodon, container, false)

        val btn_oauth_regist: Button = view.findViewById(R.id.oauth2_client_registration)
        btn_oauth_regist.setOnClickListener(this)

        val btn_login: Button = view.findViewById(R.id.login)
        btn_login.setOnClickListener(this)

        Toast.makeText(getActivity(),"set button onclick listener",Toast.LENGTH_SHORT).show()

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MastodonFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): MastodonFragment {
            val fragment = MastodonFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    internal var client = OkHttpClient()

    override fun onClick(v:View?){
        when (v?.id) {
            R.id.oauth2_client_registration -> {

                object: MyAsyncTask(){
                    override fun doInBackground(vararg params: Void): String {
                        val MIMEType= MediaType.parse("application/json; charset=utf-8");

                        val urlString:String ="https://mstdn.monappy.jp/api/v1/apps"
                        val requestBodyJson:String = "{" +
                                "\"client_name\":\"test\"," +
                                "\"redirect_uris\":\"urn:ietf:wg:oauth:2.0:oob\"," +
                                "\"scopes\":\"read write follow\"" +
                                "}"
                        val requestBody = RequestBody.create(MIMEType, requestBodyJson)

                        val request = Request.Builder().url(urlString).post(requestBody).build()

                        val response = client.newCall(request).execute()
                        val res = response.body()!!.string()

                        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                        val dto = moshi.adapter(JsonRegisterClient::class.java).fromJson(res)

                        if (dto?.id != null && dto?.client_id != null && dto?.client_secret != null && dto?.redirect_uri != null){
                            val pref = PreferenceManager.getDefaultSharedPreferences(context)
                            pref.edit().putString("id", dto?.id.toString()).commit()
                            pref.edit().putString("redirect_uri", dto?.redirect_uri.toString()).commit()
                            pref.edit().putString("client_id", dto?.client_id.toString()).commit()
                            pref.edit().putString("client_secret", dto?.client_secret.toString()).commit()
                        }else{
                            Toast.makeText(getActivity(),"Failed : oauth2_client_registration",Toast.LENGTH_SHORT).show()
                        }

                        if (checkStateOauthClientRegist()){
                            Log.d("DEBUG", "TRUE!!!!!1")
                        } else{
                            Log.d("DEBUG", "False ....")
                        }
                        return res
                    }
                }.execute()

            }

            R.id.login -> {
                Timber.d("login")
                Toast.makeText(getActivity(),"login",Toast.LENGTH_SHORT).show()
            }

            else -> {
            }
        }
    }

    fun checkState(){

        val oAuthClientRegistered = checkStateOauthClientRegist()

    }

    private fun checkStateOauthClientRegist(): Boolean{
        val pref = PreferenceManager.getDefaultSharedPreferences(context)

        val getprefId = pref.getString("id",null)
        val getprefRedirect_uri = pref.getString("redirect_uri",null)
        val getprefClient_id = pref.getString("client_id",null)
        val getprefClient_secret = pref.getString("client_secret",null)

        return (getprefId != null && getprefClient_id != null && getprefClient_secret != null && getprefRedirect_uri != null)

    }


}// Required empty public constructor
