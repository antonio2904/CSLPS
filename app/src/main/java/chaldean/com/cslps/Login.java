package chaldean.com.cslps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

    EditText ed1;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ed1 = (EditText)findViewById(R.id.username);
        loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ed1.getText().toString();
                if(username.isEmpty())
                    ed1.setError("Enter Username");
                if(!username.isEmpty()){
                    if(username.equals("1234")){
                        Intent intent = new Intent(Login.this,MainPage.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                        Toast.makeText(Login.this, "Wrong credintials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
