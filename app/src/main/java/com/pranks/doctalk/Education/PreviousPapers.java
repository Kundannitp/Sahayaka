package com.pranks.doctalk.Education;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.pranks.doctalk.Home;
import com.pranks.doctalk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class PreviousPapers extends Fragment {
    View v;
    TextView content;
    EditText name,email;
    Button req_btn;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_previous_papers, container, false);
        ((Home) getActivity()).getSupportActionBar().setTitle("Practice Papers");
        name=v.findViewById(R.id.name_test);
        email=v.findViewById(R.id.email_test);
        content=v.findViewById(R.id.content_res_test);
        req_btn=v.findViewById(R.id.req_btn_test);
        req_btn.setOnClickListener(v->{
            new SetDataToTrain().execute();
        });
        return v;
    }


    public class SetDataToTrain extends AsyncTask<Void, Void, Void> {
        String datato="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{

                // CALL GetText method to make post method call
                datato=GetText();
            }
            catch(Exception ex)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setTextToval(datato);
        }

    }

    void setTextToval(String data){
        content.setText(data);
    }

    public  String  GetText()  throws UnsupportedEncodingException
    {
        // Get user defined values
        String Name = name.getText().toString();
        String Email   = email.getText().toString();

        // Create data variable for sent values to server

        String data = URLEncoder.encode("name", "UTF-8")
                + "=" + URLEncoder.encode(Name, "UTF-8");

        data += "&" + URLEncoder.encode("email", "UTF-8") + "="
                + URLEncoder.encode(Email, "UTF-8");

        String text = "";
        BufferedReader reader=null;
        Log.i("query text",data);

        // Send data
        try
        {
            Log.i("before url","hii");

            // Defined URL  where to send data
            URL url = new URL("http://192.168.43.103:3000/testing");

            Log.i("after url","hiii");

            // Send POST data request

            List<NameValuePair> params = new ArrayList<>();
            params.add(new NameValuePair("name", Name));
            params.add(new NameValuePair("email", Email));
            String datatosend=getQuery(params);
            Log.i("data to send",datatosend);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(datatosend);
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }


            text = sb.toString();
        }
        catch(Exception ex)
        {
            Log.i("exception in url",ex.toString());
        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {
                Log.i("exception hai",ex.toString());
            }
        }

        // Show response on activity
        return text;

    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
