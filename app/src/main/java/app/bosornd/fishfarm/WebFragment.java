package app.bosornd.fishfarm;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by jun on 17. 9. 29.
 */

public class WebFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private WebView webView;

    public WebFragment() {
        // Required empty public constructor
    }


    public static WebFragment newInstance(String normalText) {
        WebFragment fragment = new WebFragment();
        Bundle args = new Bundle();
        args.putString(Constants.FRAG_B, normalText);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view =  inflater.inflate(R.layout.fragment_web, container, false);

        webView = (WebView) view.findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);



        if(getArguments().getString(Constants.FRAG_B)=="Main"){
            webView.loadUrl("http://okhwa.sogari.co.kr/app/main/index.php");
        } else if(getArguments().getString(Constants.FRAG_B)=="Record") {
            webView.loadUrl("http://okhwa.sogari.co.kr/app/record/bbs/write.php");
        }

        webView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
                try {
                    webView.stopLoading();
                } catch (Exception e) {
                }

                if (webView.canGoBack()) {
                    webView.goBack();
                }

                webView.loadUrl("about:blank");


                /*
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("에러발생");
                new Handler().postDelayed(new Runnable() {// 1.5 초 후에 실행
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                }, 1500);

                alertDialog.show();
                */

                super.onReceivedError(webView, errorCode, description, failingUrl);
            }
        });


        //webView.loadUrl("http://api.sogari.co.kr/html/info/index.php");
        //webView.loadUrl("http://api.sogari.co.kr/record/bbs/write.php");

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        return view;
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}