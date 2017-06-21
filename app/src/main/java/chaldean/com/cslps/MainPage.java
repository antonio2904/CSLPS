package chaldean.com.cslps;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainPage extends Activity{

    EditText ed1,ed2,ed3,ed4,ed5;
    Button b1;
    TextView tx1,tx2;
    String caste,classs;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ed1 = (EditText)findViewById(R.id.studentName);
        ed2 = (EditText)findViewById(R.id.studentAdmn);
        ed3 = (EditText)findViewById(R.id.studentCode);
        tx1 = (TextView) findViewById(R.id.resultView);
        tx2 = (TextView)findViewById(R.id.resultCount);
        b1 = (Button)findViewById(R.id.searchButton);

        // Spinner element
        final Spinner spinner = (Spinner) findViewById(R.id.studentCaste);
        final Spinner spinner1 = (Spinner)findViewById(R.id.studentClass);


        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("--CASTE--");
        categories.add("GENERAL");
        categories.add("OBC");
        categories.add("EZHAVA");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setPrompt("CASTE");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                    caste = "";
                else
                    caste = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> arg0){
            }
        });

        // Spinner Drop down elements
        List<String> classes = new ArrayList<String>();
        classes.add("--CLASS--");
        classes.add("1 A");
        classes.add("1 B");
        classes.add("2 A");
        classes.add("2 B");
        classes.add("3 A");
        classes.add("3 B");
        classes.add("4 A");
        classes.add("4 B");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, classes);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner1.setAdapter(dataAdapter1);
        spinner1.setPrompt("CLASS");

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                    classs = "";
                else
                    classs = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> arg0){
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx1.setText("");
                final String name = ed1.getText().toString();
                final String admn = ed2.getText().toString();
                final String stcode = ed3.getText().toString();
                if(name.isEmpty()&&admn.isEmpty()&&stcode.isEmpty()&&classs.isEmpty()&&caste.isEmpty())
                    Toast.makeText(MainPage.this,"Please Enter Any of the Fields",Toast.LENGTH_LONG).show();
                else{
                    pd = ProgressDialog.show(MainPage.this,"","Fetching...");
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://antony-leons.000webhostapp.com/Students.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            showJSON(response);
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    pd.dismiss();
                                    Toast.makeText(MainPage.this,"Please Try Again",Toast.LENGTH_LONG).show();
                                }
                            }){
                        @Override
                        protected Map<String,String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            if(!name.isEmpty())
                                params.put("name", name);
                            if(!admn.isEmpty())
                                params.put("admn", admn);
                            if(!stcode.isEmpty())
                                params.put("stcode", stcode);
                            if(!classs.isEmpty())
                                params.put("class", classs);
                            if(!caste.isEmpty())
                                params.put("caste", caste);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(MainPage.this);
                    requestQueue.add(stringRequest);
                }
            }
        });
    }
    private void showJSON(String response){
        String name1;
        String admn;
        String st_code;
        String class1;
        int i;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");
            StringBuilder builder = new StringBuilder();
            for(i=0;i<result.length();i++) {
                JSONObject collegeData = result.getJSONObject(i);
                name1 = collegeData.getString("name" + String.valueOf(i));
                admn = collegeData.getString("admn_no" + String.valueOf(i));
                st_code = collegeData.getString("st_code" + String.valueOf(i));
                class1 = collegeData.getString("class" + String.valueOf(i));
                builder.append("Name:\t" + name1 + "\nAdmission number:\t" + admn + "\nStudent Code:\t" + st_code + "\nClass:\t" + class1 + "\n\n\n");
            }
            tx1.setText(builder.toString());
            tx2.setText("TOTAL NUMBER: "+ String.valueOf(i));
            pd.dismiss();
            } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
