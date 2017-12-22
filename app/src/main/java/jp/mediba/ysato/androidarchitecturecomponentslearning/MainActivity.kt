package jp.mediba.ysato.androidarchitecturecomponentslearning

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity :  AppCompatActivity(), MastodonFragment.OnFragmentInteractionListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}
