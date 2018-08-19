package app.bosornd.fishfarm;

/**
 * Created by jun on 17. 9. 12.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class SettingActivity extends AppCompatActivity {

    Switch aSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        aSwitch = (Switch) findViewById(R.id.switch_alarm);
        final String sfName = "switch_mode";
        SharedPreferences pref = getSharedPreferences("switch_mode", 0);
        Boolean str = pref.getBoolean("service_status", true);
        aSwitch.setChecked(str);

        aSwitch.bringToFront();

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Toast.makeText(SettingActivity.this, "알림상태" + isChecked, Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = getSharedPreferences("switch_mode", 0).edit();
                editor.putBoolean("service_status", isChecked);
                editor.apply();

            }

        });

    }
}