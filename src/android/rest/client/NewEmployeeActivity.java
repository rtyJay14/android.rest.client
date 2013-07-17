package android.rest.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.rest.client.constants.ServiceUrl;
import android.rest.client.entity.Employee;
import android.rest.client.util.JSONHttpClient;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class NewEmployeeActivity extends Activity {
    private ProgressDialog progressDialog;
    private EditText editTextName, editTextLoginDate, editTextLogoutDate;
    private Button buttonCreateEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_employee);

        initializeComponents();
    }

    private void initializeComponents() {
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextLoginDate = (EditText) findViewById(R.id.editTextPrice);
        editTextLogoutDate = (EditText) findViewById(R.id.editTextDescription);
        buttonCreateEmployee = (Button) findViewById(R.id.buttonCreateEmployee);
        buttonCreateEmployee.setOnClickListener(buttonCreateEmployeeClickListener);
    }

    private View.OnClickListener buttonCreateEmployeeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new CreateNewEmployee().execute();
        }
    };

    class CreateNewEmployee extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NewEmployeeActivity.this);
            progressDialog.setMessage("Creating Employee...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("name", editTextName.getText().toString()));
            args.add(new BasicNameValuePair("price", editTextLoginDate.getText().toString()));
            args.add(new BasicNameValuePair("description", editTextLogoutDate.getText().toString()));
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            Employee employee = (Employee) jsonHttpClient.PostParams(ServiceUrl.EMPLOYEE, args, Employee.class);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            return null;
        }
    }
}
