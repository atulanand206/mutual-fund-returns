package com.creations.funds.client;

import com.creations.funds.models.Scheme;
import com.creations.funds.models.SchemeLite;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface MFAPIClient {

    @GET("/mf")
    Call<List<SchemeLite>> getFunds();

    @GET("/mf/{fundCode}")
    Call<Scheme> getFund(@Path("fundCode") int fundCode);
}
