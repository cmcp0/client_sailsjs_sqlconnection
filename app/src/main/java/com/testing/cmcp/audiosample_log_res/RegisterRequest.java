package com.testing.cmcp.audiosample_log_res;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://yaexiste.herokuapp.com/user/new";
    //private static final String REGISTER_REQUEST_URL = "http://192.168.0.4:1337/user/new";

    private Map<String, String> params;

    public RegisterRequest(String name, String email, String aDefault, String password, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        //params.put("age", age + "");
        params.put("email", email);
        params.put("ut", aDefault);
        params.put("password", password);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}