package app.bosornd.fishfarm;

import android.app.Activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnFragmentInteractionListener, Listener {

    private TextView txtFishfarm;
    private TextView txtName;
    String loginId, fishFarm;
    private CoordinatorLayout coordinatorLayout;
    private ActionBarDrawerToggle toggle;
    private boolean mToolBarNavigationListenerIsRegistered = false;


    public static final String TAG = MainActivity.class.getSimpleName();
    private NFCReadFragment mNfcReadFragment;
    private boolean isDialogDisplayed = false;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(!NetworkConnection()){
            NotConnected_showAlert();
        }

        setContentView(R.layout.activity_main);


        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginId = auto.getString("inputId", null);
        fishFarm = auto.getString("fishFarm", null);
        View header=navigationView.getHeaderView(0);
        txtFishfarm = (TextView) header.findViewById(R.id.fishfarm_name);
        txtName = (TextView) header.findViewById(R.id.login_id);
        txtFishfarm.setText(loginId);
        txtName.setText(fishFarm);


        //initNFC();

        //showReadFragment();

        homeFragment();

    }


    /*
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);


        Toast.makeText(MainActivity.this, "인터넷접속을다시확인해주세요.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {// 1.5 초 후에 실행
            @Override
            public void run() {
                System.exit(0);
            }
        }, 1500);


    if(NetworkConnection() == false){
        NotConnected_showAlert();
    }
    */



    private void NotConnected_showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("네트워크 연결 오류");
        builder.setMessage("사용 가능한 무선네트워크가 없습니다.\n" + "먼저 무선네트워크 연결상태를 확인해 주세요.")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish(); // exit
                        //application 프로세스를 강제 종료
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }



    private boolean NetworkConnection() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if (activeNetwork != null) {
            return true;
        } else {
            return false;
        }
    }

/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        WebView webView = (WebView)findViewById(R.id.webView);
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        //백할 페이가 없다면
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (!webView.canGoBack())) {

            //다이아로그박스 출력
            new AlertDialog.Builder(this)
                    .setTitle("프로그램 종료")
                    .setMessage("프로그램을 종료하시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    })
                    .setNegativeButton("아니오", null).show();
        }


        return super.onKeyDown(keyCode, event);
    }
*/


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        WebView webView = (WebView)findViewById(R.id.webView);

        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        }


        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("종료하시겠습니까?")
                .setPositiveButton("예", dialogClickListener)
                .setNegativeButton("아니요", dialogClickListener)
                .setCancelable(false)
                .setTitle("종료여부");
        builder.show();

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onFragmentMessage(int TAG, String data) {

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_logout) {

            SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
            SharedPreferences.Editor autoLogin = auto.edit();
            //autoLogin.putString("inputId", null);
            //autoLogin.putString("inputPwd", null);
            autoLogin.clear();
            autoLogin.commit();


            new AlertDialog.Builder(this)
                    .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                    .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    })
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();


        android.support.v4.app.Fragment fragment = null;
        Class fragmentClass = null;
        Bundle args = new Bundle();


        switch (id) {
            case R.id.nav_main:
                fragmentClass = MainFragment.class;
                args.putString(Constants.FRAG_A, "http://okhwa.sogari.co.kr/app/main/");
                break;
            case R.id.nav_chart:
                fragmentClass = MainFragment.class;
                args.putString(Constants.FRAG_A, "http://okhwa.sogari.co.kr/app/chart/");
                break;
            case R.id.nav_record:
                //fragmentClass = WebFragment.class;
                //args.putString(Constants.FRAG_B, "Record");
                fragmentClass = MainFragment.class;
                args.putString(Constants.FRAG_A, "http://okhwa.sogari.co.kr/app/record/bbs/default/");
                break;
            case R.id.nav_camera:
                try {
                    final String CHECK_PACKAGE_NAME = "com.mcu.GuardingExpert";
                    PackageManager pm = getPackageManager();
                    PackageInfo pi = pm.getPackageInfo(CHECK_PACKAGE_NAME.trim(), PackageManager.GET_META_DATA);
                    ApplicationInfo appInfo = pi.applicationInfo;
                    // 패키지가 있을 경우.
                    Intent intent = pm.getLaunchIntentForPackage(CHECK_PACKAGE_NAME);
                    startActivity(intent);

                } catch (PackageManager.NameNotFoundException e) {
                    Uri webpage = Uri.parse("http://play.google.com/store/apps/details?id=com.mcu.GuardingExpert");
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(webIntent);
                }
                break;
            case R.id.nav_calendar:
                fragmentClass = CalendarFragment.class;
                break;
            case R.id.nav_send:
                fragmentClass = ContactFragment.class;
                args.putString(Constants.FRAG_D, "개발자문의: junwoo.park@bosornd.com, 010-3942-6226");
                break;

        }

        try {
            fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
            fragment.setArguments(args);
            // Insert the fragment by replacing any existing fragment
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_layout_for_activity_navigation, fragment).addToBackStack(null).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void initNFC(){
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    private void showReadFragment() {

        mNfcReadFragment = (NFCReadFragment) getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);

        if (mNfcReadFragment == null) {

            mNfcReadFragment = NFCReadFragment.newInstance();
        }

        //if문작성 .show가 작동될때와안될때
        /*
        Intent startIntent = getIntent();
        if ((startIntent != null) &&
                (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(startIntent.getAction()) ||
                        NfcAdapter.ACTION_TECH_DISCOVERED.equals(startIntent.getAction()))) {
            showReadFragment();
        }
*/
        try{
            mNfcReadFragment.show(getFragmentManager(), NFCReadFragment.TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogDisplayed() {

        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {

        isDialogDisplayed = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected, tagDetected, ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

        if (isDialogDisplayed) {
            mNfcReadFragment = (NFCReadFragment) getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);
            // mNfcReadFragment.onNfcDetected(ByteArrayToHexString(getIntent().getByteArrayExtra(NfcAdapter.EXTRA_ID)));

            String ndef = ByteArrayToHexString(getIntent().getByteArrayExtra(NfcAdapter.EXTRA_ID));


            if (ndef.equals("0496CF02904980")) {     //1
                testFragment1();
            } else if (ndef.equals("0496D002904980")) {      //2
                testFragment2();
            }
            /*

            1번수조
            0496CF02904980
            1-1번수조
            0491CD02904980
            2번수조
            0496D002904980
            2-1번수조
            0496AA02904980
            3번수조
            0492BF02904980
            3-1번수조
            0491CF02904980
            4번수조
            046A8C02904981
            4-1번수조
            046A7A02904981
            5번수조
            046A6402904981
            5-1번수조
            04961A02904980
            6번수조
            04972402904980
            6-1번수조
            0496A702904980
            7번수조
            04D97202904980
            7-1번수조
            04976A02904980
            8번수조
            04D90E02904980
            8-1번수조
            04D91802904980
            9번수조
            04D93702904980
            9-1번수조
            04D90D02904980

            */

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            showReadFragment();
        }
    }

    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }



    public void homeFragment(){
        try {
            Bundle args = new Bundle();
            Class fragmentClass =MainFragment.class;
            args.putString(Constants.FRAG_A, "http://okhwa.sogari.co.kr/app/main/");
            android.support.v4.app.Fragment fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
            fragment.setArguments(args);
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_layout_for_activity_navigation, fragment).commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    public void testFragment1(){
        try {
            Bundle args = new Bundle();
            Class fragmentClass =MainFragment.class;
            args.putString(Constants.FRAG_A, "http://okhwa.sogari.co.kr/app/record/bbs/");
            args.putString(Constants.FRAG_a, "1");
            android.support.v4.app.Fragment fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
            fragment.setArguments(args);
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_layout_for_activity_navigation, fragment).commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    public void testFragment2(){
        try {
            Bundle args = new Bundle();
            Class fragmentClass =MainFragment.class;
            args.putString(Constants.FRAG_A, "http://okhwa.sogari.co.kr/app/record/bbs/");
            args.putString(Constants.FRAG_a, "2");
            android.support.v4.app.Fragment fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
            fragment.setArguments(args);
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_layout_for_activity_navigation, fragment).commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


}