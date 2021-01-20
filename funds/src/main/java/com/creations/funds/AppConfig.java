package com.creations.funds;

import com.creations.funds.client.MFAPIClient;
import com.creations.funds.gson.ISODateAdapter;
import com.creations.funds.service.MFRepository;
import com.creations.funds.service.MFService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Date;

@Configuration
public class AppConfig {

    @Bean
    MFService configureMFService(final MFAPIClient mfapiClient, final MFRepository mfRepository) {
        return new MFService(mfapiClient, mfRepository);
    }

    @Bean
    MFAPIClient configureMFAPIClient(@Value("${mutual_fund_returns.api.url}") final String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(new Gson().newBuilder().registerTypeAdapter(Date.class, new ISODateAdapter()).create()))
                .build().create(MFAPIClient.class);
    }

    @Bean
    MFRepository configureMFRepository() {
        return new MFRepository();
    }
}
