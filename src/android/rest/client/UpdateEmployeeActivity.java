package android.rest.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.rest.client.constants.ResultCode;
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


public class UpdateEmployeeActivity extends Activity {
    private EditText editTextName, editLoginDate, editLogoutDate;
    private Button buttonSave, buttonDelete;
    private int employeeId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_employee);

        initializeComponents();

        Intent intent = getIntent();
        employeeId = intent.getIntExtra(Employee.EMPLOYEE_ID, 0);
        new GetEmployeeDetails().execute();

    }

    private void initializeComponents() {
        editTextName = (EditText) findViewById(R.id.editTextName);
        editLogoutDate = (EditText) findViewById(R.id.editLogoutDate);
        editLoginDate = (EditText) findViewById(R.id.editLoginDate);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        buttonSave.setOnClickListener(buttonSaveClickListener);
        buttonDelete.setOnClickListener(buttonDeleteClickListener);
    }

    private View.OnClickListener buttonDeleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DeleteEmployee().execute();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    };
    private View.OnClickListener buttonSaveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new SaveEmployeeDetails().execute();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    };

    class DeleteEmployee extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UpdateEmployeeActivity.this);
            progressDialog.setMessage("Deleting Employee...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair(Employee.EMPLOYEE_ID, String.valueOf(employeeId)));
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            if (jsonHttpClient.Delete(ServiceUrl.EMPLOYEE, args)) {
                Intent intent = getIntent();
                setResult(ResultCode.EMPLOYEE_DELETE_SUCCESS);
                finish();
            }
            return null;
        }
    }

    class SaveEmployeeDetails extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UpdateEmployeeActivity.this);
            progressDialog.setMessage("Saving Employee...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            Employee employee = new Employee();
            employee.setId(employeeId);
            employee.setName(editTextName.getText().toString());
            employee.setLogin_Date(editLogoutDate.getText().toString());
            employee.setLogout_Date(editLoginDate.getText().toString());
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            employee = (Employee) jsonHttpClient.PostObject(ServiceUrl.EMPLOYEE, employee, Employee.class);
            Intent intent = getIntent();
            setResult(ResultCode.EMPLOYEE_UPDATE_SUCCESS, intent);
            finish();
            return null;
        }
    }

    class GetEmployeeDetails extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UpdateEmployeeActivity.this);
            progressDialog.setMessage("Loading Employee Details. Please wait...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair(Employee.EMPLOYEE_ID, String.valueOf(employeeId)));
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            final Employee employee = jsonHttpClient.Get(ServiceUrl.EMPLOYEE, args, Employee.class);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (employee != null) {
                        editTextName.setText(employee.getName());
                        editLoginDate.setText(String.valueOf(employee.getLogin_Date()));
                        editLogoutDate.setText(String.valueOf(employee.getLogout_Date()));
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }
    }
}
