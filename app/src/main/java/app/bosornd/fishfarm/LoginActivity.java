package app.bosornd.fishfarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jun on 17. 9. 14.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUsername;
    private EditText editTextPassword;

    private Button buttonLogin;
    String loginId, loginPwd, fishFarm;
    private TextView tvregist;


    public static final String ROOT_URL = "http://api.sogari.co.kr";
    private String success_login = "로그인성공";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //Initializing Views
        editTextUsername = (EditText) findViewById(R.id.editTextUsername1);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword1);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        //Adding listener to button

        tvregist = (TextView) findViewById(R.id.tvregist);

        tvregist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        buttonLogin.setOnClickListener(this);




        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        loginId = auto.getString("inputId", null);
        loginPwd = auto.getString("inputPwd", null);
        fishFarm = auto.getString("fishFarm", null);

        if (loginId != null && loginPwd != null) {
            if (fishFarm != null) {
                if (fishFarm.equals("옥화양어장")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (fishFarm.equals("내수면연구소")) {
                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }
    }


    private void checkUser(){
        //Here we will handle the http request to insert user to mysql db
        //Creating a RestAdapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        LoginAPI api = adapter.create(LoginAPI.class);

        //Defining the method insertuser of our interface
        api.checkUser(

                //Passing the values by getting it from editTexts
                editTextUsername.getText().toString(),
                editTextPassword.getText().toString(),

                //Creating an anonymous callback
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {
                        //On success we will read the server's output using bufferedreader
                        //Creating a bufferedreader object
                        BufferedReader reader = null;

                        //An string to store output from the server
                        String output[] = new String[10];

                        try {
                            //Initializing buffered reader
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                            //Reading the output in the string
                            for(int i=0; i<output.length; i++){
                                output[i] = reader.readLine();
                            }


                            if(output[0].equals(success_login)) {


                                if (output[1].equals("옥화양어장")) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                                else if (output[1].equals("내수면연구소"))  {

                                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor autoLogin = auto.edit();
                        autoLogin.putString("inputId", editTextUsername.getText().toString());
                        autoLogin.putString("inputPwd", editTextPassword.getText().toString());
                        autoLogin.putString("fishFarm", output[1]);
                        autoLogin.commit();

                        //Displaying the output as a toast
                        Toast.makeText(LoginActivity.this, output[0], Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast
                        Toast.makeText(LoginActivity.this, error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void onClick(View v) {
        //Calling insertUser on button click
        checkUser();

    }
}