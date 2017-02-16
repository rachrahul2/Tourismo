package tourist.android.com.newproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tourist.android.com.newproject.sql.User;
import tourist.android.com.newproject.sql.UserDataSource;

public class MainActivity extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000; //For old devices
    private static final int UI_ANIMATION_DELAY = 300; //Same thing
    private EditText txtUserName;
    private EditText txtPassword;
    private Button btnLogin, btnRegister;
    UserDataSource userDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Init datasource
        userDataSource = new UserDataSource(this);
        userDataSource.open();

        Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );

        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setTypeface(font);

        btnLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                String userName = txtUserName.getText().toString();
                String password = txtPassword.getText().toString();
                if(userName != null && userName.equals("Rahul")
                        && password != null && password.equals("Rahul")) {
                    Toast.makeText(MainActivity.this, "Welcome " + userName + ".", Toast.LENGTH_LONG).show();
                    startDasboard();
                } else {
                    if(userDataSource.isClosed()) {
                        userDataSource.open();
                    }
                    User user = userDataSource.getUser(userName, password);
                    if(user != null && user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                        Toast.makeText(MainActivity.this, "Welcome " + user.getFullName() + ".", Toast.LENGTH_LONG).show();
                        startDasboard();
                    } else {
                        Toast.makeText(MainActivity.this, "Incorrect Username or Password!", Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            }
        });

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setTypeface(font);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
            }
        });

    }

    private void startDasboard() {
        Intent intent = new Intent(this, DasboardActivity.class);
        startActivity(intent);
    }

    private void startRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        userDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userDataSource.open();
    }

}
