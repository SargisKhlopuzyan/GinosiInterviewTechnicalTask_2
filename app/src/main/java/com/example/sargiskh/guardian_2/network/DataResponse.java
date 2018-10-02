package com.example.sargiskh.guardian_2.network;

import com.example.sargiskh.guardian_2.model.Response;
import com.google.gson.annotations.SerializedName;

public class DataResponse {

    @SerializedName("response") private Response response;

    public Response getResponse() {
        return response;
    }

}
