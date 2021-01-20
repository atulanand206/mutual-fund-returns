package com.creations.funds.service;

import com.creations.funds.client.MFAPIClient;
import com.creations.funds.exceptions.ServiceException;
import com.creations.funds.models.SchemeLite;
import com.creations.funds.models.SchemeResponse;
import org.springframework.http.HttpStatus;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MFService {

    private final MFAPIClient fMfApiClient;

    private final MFRepository fMfRepository;

    public MFService(final MFAPIClient mfApiClient, final MFRepository mfRepository) {
        fMfApiClient = mfApiClient;
        fMfRepository = mfRepository;
    }

    public List<SchemeLite> getAllFunds() {
        try {
            final var response = fMfApiClient.getFunds().execute();
            validateResponseSuccess(response);
            return response.body();
        } catch (IOException e) {
            throw new ServiceException("funds call failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public SchemeResponse fetchFundByCode(int schemeCode, int period, int horizon) {
        final var fundLite = getAllFunds().stream().filter(x -> x.getSchemeCode() == schemeCode).collect(Collectors.toList());
        if (fundLite.size() != 1) {
            return new SchemeResponse();
        }
        try {
            final var response = fMfApiClient.getFund(schemeCode).execute();
            validateResponseSuccess(response);
            return fMfRepository.getFundResponse(response.body(), period, horizon);
        } catch (IOException e) {
            throw new ServiceException("fund call failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private <T> void validateResponseSuccess(Response<T> response) throws IOException {
        if (!response.isSuccessful()) {
            final var responseErrorBody = response.errorBody();
            throw new IOException(responseErrorBody != null ? responseErrorBody.string() : "Unknown error");
        }
    }
}
