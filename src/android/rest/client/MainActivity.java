package android.rest.client;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.rest.client.constants.RequestCode;
import android.rest.client.constants.ResultCode;
import android.rest.client.constants.ServiceUrl;
import android.rest.client.entity.Employee;
import android.rest.client.util.JSONHttpClient;
import android.view.View;
import android.widget.*;

import org.apache.http.NameValuePair;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends ListActivity {
    ArrayList<HashMap<String, String>> employeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_employees);

        initializeComponents();

        employeeList = new ArrayList<HashMap<String, String>>();
        new LoadAllEmployees().execute();
    }

    private void initializeComponents() {
        ListView listView = getListView();
        listView.setOnItemClickListener(listViewItemClickListener);
    }

    private AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int employeeId = Integer.parseInt(((TextView) view.findViewById(R.id.textViewId)).getText().toString());
            Intent intent = new Intent(getApplicationContext(), UpdateEmployeeActivity.class);
            intent.putExtra(Employee.EMPLOYEE_ID, employeeId);
            startActivityForResult(intent, RequestCode.EMPLOYEE_DETAILS);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ResultCode.EMPLOYEE_UPDATE_SUCCESS || resultCode == ResultCode.EMPLOYEE_DELETE_SUCCESS) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    class LoadAllEmployees extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            Employee[] employees = jsonHttpClient.Get(ServiceUrl.EMPLOYEE, nameValuePairs, Employee[].class);
            if (employees.length > 0) {

                for (Employee employee : employees) {
                    HashMap<String, String> mapEmployee = new HashMap<String, String>();
                    mapEmployee.put(Employee.EMPLOYEE_ID, String.valueOf(employee.getId()));
                    mapEmployee.put(Employee.EMPLOYEE_NAME, employee.getName());
                    employeeList.add(mapEmployee);
                }

            } else {
                Intent intent = new Intent(getApplicationContext(), NewEmployeeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();    
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading Employees. Please wait...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(MainActivity.this, employeeList, R.layout.list_item, new String[]{"Id", "Full_Name"}, new int[]{R.id.textViewId, R.id.textViewName});
                    setListAdapter(adapter);
                }
            });
        }
    }
}

