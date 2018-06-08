package com.dedykuncoro.kuncorocrud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dedykuncoro.kuncorocrud.app.AppController;
import com.dedykuncoro.kuncorocrud.util.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class InsertData extends AppCompatActivity {

    EditText id, td_nama, td_alamat, td_nohp;
    Button btnsimpan, btnbatal, upload;
    ProgressDialog pd;
    Intent i;

    private static final String TAG = InsertData.class.getSimpleName();

    private static String url_insert 	 = Server.URL + "insert.php";
    private static String url_edit 	     = Server.URL + "edit.php";
    private static String url_update 	 = Server.URL + "update.php";
    //private static String url_delete 	 = Server.URL + "deleteguru.php";

    public static final String TAG_ID       = "id";
    public static final String TAG_NAMA     = "nama";
    public static final String TAG_ALAMAT   = "alamat";
    public static final String TAG_NOHP     = "nohp";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_insertdata);

        Intent data = getIntent();
        final int update = data.getIntExtra("update", 0);
        String intent_id =  data.getStringExtra("id");
        String intent_nama =  data.getStringExtra("nama");
        String intent_alamat =  data.getStringExtra("alamat");
        String intent_nohp =  data.getStringExtra("nohp");

        id = (EditText) findViewById(R.id.et_id);
        td_nama = (EditText) findViewById(R.id.et_nama);
        td_alamat = (EditText) findViewById(R.id.et_alamat);
        td_nohp = (EditText) findViewById(R.id.et_nohp);

        pd = new ProgressDialog(InsertData.this);

        btnsimpan = (Button) findViewById(R.id.btnsimpan);
        upload = (Button) findViewById(R.id.upload);
        btnbatal = (Button) findViewById(R.id.btnbatal);

        if (update == 1)
        {
            btnsimpan.setText("Update Data");
            id.setText(intent_id);
            id.setVisibility(View.GONE);
            td_nama.setText(intent_nama);
            td_alamat.setText(intent_alamat);
            td_nohp.setText(intent_nohp);
        }

        btnsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url;
                if (update == 1)
                {
                    Update_data();
                    url = url_update;
                } else {
                    simpanData();
                    url = url_insert;
                }
            }
        });

        btnbatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent (InsertData.this, MainActivity.class);
                startActivity(main);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InsertData.this, UploadImage.class);
                startActivity(intent);
            }
        });
    }

    private void Update_data()
    {
        pd.setMessage("Update Data");
        pd.setCancelable(false);
        pd.show();

        StringRequest updateReq = new StringRequest(Request.Method.POST, url_edit,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            Toast.makeText(InsertData.this, "pesan : "+   res.getString("message") , Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        startActivity( new Intent(InsertData.this,MainActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Toast.makeText(InsertData.this, "pesan : Gagal Insert Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("id",id.getText().toString());
                map.put("nama",td_nama.getText().toString());
                map.put("alamat",td_alamat.getText().toString());
                map.put("nohp",td_nohp.getText().toString());

                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(updateReq);
    }

    private void simpanData()
    {

        pd.setMessage("Menyimpan Data");
        pd.setCancelable(false);
        pd.show();

        StringRequest sendData = new StringRequest(Request.Method.POST, url_insert,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            Toast.makeText(InsertData.this, "pesan : "+   res.getString("message") , Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        startActivity( new Intent(InsertData.this,MainActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Toast.makeText(InsertData.this, "pesan : Gagal Insert Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("id",id.getText().toString());
                map.put("nama",td_nama.getText().toString());
                map.put("alamat",td_alamat.getText().toString());
                map.put("nohp",td_nohp.getText().toString());

                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(sendData);
    }
}
