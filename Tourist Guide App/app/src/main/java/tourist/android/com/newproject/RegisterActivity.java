package tourist.android.com.newproject;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import tourist.android.com.newproject.sql.UserDataSource;


public class RegisterActivity extends AppCompatActivity {

    TextView txtUserName, txtPassword, txtPasswordAgain, txtFullName;
    Button btnOK;
    UserDataSource userDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Init datasource
        userDataSource = new UserDataSource(this);
        userDataSource.open();

        Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );

        txtUserName = (TextView)findViewById(R.id.txtUserName);
        txtPassword = (TextView)findViewById(R.id.txtPassword);
        txtPasswordAgain = (TextView)findViewById(R.id.txtPasswordAgain);
        txtFullName = (TextView)findViewById(R.id.txtFullName);

        btnOK = (Button)findViewById(R.id.btnOK);
        btnOK.setTypeface(font);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = txtUserName.getText().toString();
                String password = txtPassword.getText().toString();
                String passwordAgain = txtPasswordAgain.getText().toString();
                String fullName = txtFullName.getText().toString();

                if(userName == null || userName.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "UserName cannot be empty.", Toast.LENGTH_SHORT).show();
                    txtUserName.requestFocus();
                    return;
                }
                if(password == null || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
                    txtPassword.requestFocus();
                    return;
                }
                if(passwordAgain == null || passwordAgain.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Password Again cannot be empty.", Toast.LENGTH_SHORT).show();
                    txtPasswordAgain.requestFocus();
                    return;
                }
                if(fullName == null || fullName.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Full Name cannot be empty.", Toast.LENGTH_SHORT).show();
                    txtFullName.requestFocus();
                    return;
                }
                if(!password.equals(passwordAgain)) {
                    Toast.makeText(RegisterActivity.this, "Passwords must be same", Toast.LENGTH_SHORT).show();
                    return;
                }
                userDataSource.saveUser(userName, fullName, password);
                Toast.makeText(RegisterActivity.this, "Created " + userName + ".", Toast.LENGTH_SHORT).show();
            }
        });

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
