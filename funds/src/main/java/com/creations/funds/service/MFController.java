package com.creations.funds.service;

import com.creations.funds.models.SchemeLite;
import com.creations.funds.models.SchemeResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MFController {

    private final MFService fMfService;

    public MFController(MFService fMfService) {
        this.fMfService = fMfService;
    }

    @GetMapping("/funds")
    public List<SchemeLite> fetchFunds() {
        return fMfService.getAllFunds();
    }

    @GetMapping("/funds/{fundCode}")
    public SchemeResponse fetchSchemeByCode(
            @PathVariable("fundCode") final int fundCode,
            @RequestParam("period") final int period,
            @RequestParam("horizon") final int horizon) {
        return fMfService.fetchFundByCode(fundCode, period, horizon);
    }
}
