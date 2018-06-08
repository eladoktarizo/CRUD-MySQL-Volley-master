package com.dedykuncoro.kuncorocrud;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.dedykuncoro.kuncorocrud.adapter.AdapterGuru;
import com.dedykuncoro.kuncorocrud.app.AppController;
import com.dedykuncoro.kuncorocrud.data.DataGuru;
import com.dedykuncoro.kuncorocrud.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main2Activity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Toolbar toolbar;
    FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataGuru> itemList = new ArrayList<DataGuru>();
    AdapterGuru adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_id_guru, txt_namalengkap, txt_alamat, txt_pendidikan, txt_ttl_guru;
    String id_guru, namalengkap, alamat, pendidikan, ttl_guru;

    private static final String TAG = Main2Activity.class.getSimpleName();

    private static String url_selectguru 	 = Server.URL + "selectguru.php";
    private static String url_insertguru 	 = Server.URL + "insertguru.php";
    private static String url_edit 	     = Server.URL + "editguru.php";
    private static String url_update 	 = Server.URL + "updateguru.php";
    private static String url_delete 	 = Server.URL + "deleteguru.php";

    public static final String TAG_ID_GURU          = "id_guru";
    public static final String TAG_NAMALENGKAP      = "namalengkap";
    public static final String TAG_ALAMAT           = "alamat";
    public static final String TAG_PENDIDIKAN       = "pendidikan";
    public static final String TAG_TTL_GURU         = "ttl_guru";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // menghubungkan variablel pada layout dan pada java
        fab     = (FloatingActionButton) findViewById(R.id.fab);
        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list    = (ListView) findViewById(R.id.listguru);

        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new AdapterGuru(Main2Activity.this, itemList);
        list.setAdapter(adapter);

        // menamilkan widget refresh
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           itemList.clear();
                           adapter.notifyDataSetChanged();
                           callVolley();
                       }
                   }
        );

        // fungsi floating action button memanggil form biodata
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm("", "", "","","", "SIMPAN");
            }
        });

        // listview ditekan lama akan menampilkan dua pilihan edit atau delete data
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                           final int position, long id) {
                // TODO Auto-generated method stub
                final String idx_guru = itemList.get(position).getId_guru();

                final CharSequence[] dialogitem = {"Edit", "Delete"};
                dialog = new AlertDialog.Builder(Main2Activity.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                edit(idx_guru);
                                break;
                            case 1:
                                delete(idx_guru);
                                break;
                        }
                    }
                }).show();
                return false;
            }
        });

    }

    @Override
    public void onRefresh() {
    }

    // untuk mengosongi edittext pada form
    private void kosong(){
        txt_id_guru.setText(null);
        txt_namalengkap.setText(null);
        txt_alamat.setText(null);
        txt_pendidikan.setText(null);
        txt_ttl_guru.setText(null);
    }

    // untuk menampilkan dialog form biodata
    private void DialogForm(String idx_guru, String namalengkapx, String alamatx, String pendidikanx, String ttlx_guru, String button) {
        dialog = new AlertDialog.Builder(Main2Activity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_bioguru, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Form Biodata Guru");

        txt_id_guru      = (EditText) dialogView.findViewById(R.id.txt_idguru);
        txt_namalengkap    = (EditText) dialogView.findViewById(R.id.txt_namalengkap);
        txt_alamat  = (EditText) dialogView.findViewById(R.id.txt_alamat);
        txt_pendidikan    = (EditText) dialogView.findViewById(R.id.txt_pendidikan);
        txt_ttl_guru    = (EditText) dialogView.findViewById(R.id.txt_ttl_guru);

        if (!idx_guru.isEmpty()){
            txt_id_guru.setText(idx_guru);
            txt_namalengkap.setText(namalengkapx);
            txt_alamat.setText(alamatx);
            txt_pendidikan.setText(pendidikanx);
            txt_ttl_guru.setText(ttlx_guru);
        } else {
            kosong();
        }

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                id_guru      = txt_id_guru.getText().toString();
                namalengkap    = txt_namalengkap.getText().toString();
                alamat      = txt_alamat.getText().toString();
                pendidikan    = txt_pendidikan.getText().toString();
                ttl_guru    = txt_ttl_guru.getText().toString();

                simpan_update();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                kosong();
            }
        });

        dialog.show();
    }

    // untuk menampilkan semua data pada listview
    private void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_selectguru, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        DataGuru item = new DataGuru();

                        item.setId_guru(obj.getString(TAG_ID_GURU));
                        item.setNamalengkap(obj.getString(TAG_NAMALENGKAP));
                        item.setAlamat(obj.getString(TAG_ALAMAT));
                        //item.setNohp(obj.getString(TAG_NOHP));

                        // menambah item ke array
                        itemList.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();

                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    // fungsi untuk menyimpan atau update
    private void simpan_update() {
        String url;
        // jika id kosong maka simpan, jika id ada nilainya maka update
        if (id_guru.isEmpty()){
            url = url_insertguru;
        } else {
            url = url_update;
        }

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                        callVolley();
                        kosong();

                        Toast.makeText(Main2Activity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(Main2Activity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(Main2Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update
                if (id_guru.isEmpty()){
                    params.put("namalengkap", namalengkap);
                    params.put("alamat", alamat);
                    params.put("pendidikan", pendidikan);
                    params.put("ttl_guru", ttl_guru);
                } else {
                    params.put("id_guru", id_guru);
                    params.put("namalengkap", namalengkap);
                    params.put("alamat", alamat);
                    params.put("pendidikan", pendidikan);
                    params.put("ttl_guru", ttl_guru);
                }

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk get edit data
    private void edit(final String idx_guru){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        String idx_guru         = jObj.getString(TAG_ID_GURU);
                        String namalengkapx     = jObj.getString(TAG_NAMALENGKAP);
                        String alamatx          = jObj.getString(TAG_ALAMAT);
                        String pendidikanx      = jObj.getString(TAG_PENDIDIKAN);
                        String ttlx_guru        = jObj.getString(TAG_TTL_GURU);

                        DialogForm(idx_guru, namalengkapx, alamatx, pendidikanx, ttlx_guru, "UPDATE");

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(Main2Activity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(Main2Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idx_guru);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk menghapus
    private void delete(final String idx_guru){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("delete", jObj.toString());

                        callVolley();

                        Toast.makeText(Main2Activity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(Main2Activity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(Main2Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idx_guru);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
}
