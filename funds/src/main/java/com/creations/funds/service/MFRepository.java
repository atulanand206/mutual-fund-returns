package com.creations.funds.service;

import com.creations.funds.models.Scheme;
import com.creations.funds.models.SchemeData;
import com.creations.funds.models.SchemeResponse;
import com.creations.funds.models.SchemeResponseItem;

import java.util.*;

import static com.creations.funds.utils.DateUtils.ONE_DAY_IN_MILLIS;
import static com.creations.funds.utils.DateUtils.filterDatesWithinHorizon;
import static com.creations.funds.utils.DateUtils.getMonth;
import static java.lang.Math.pow;

public class MFRepository {

    private List<SchemeResponseItem> responseItems = new ArrayList<>();

    private List<SchemeData> dataForCalculation = new ArrayList<>();

    public MFRepository() {
    }

    public SchemeResponse getFundResponse(Scheme scheme, int period, int horizon) {
        final var data = scheme.getData();
        if (!data.isEmpty()) {
            this.dataForCalculation = filterDatesWithinHorizon(data, period, horizon);
            fundResponsesForThePeriod(period);
        }
        return new SchemeResponse(scheme.getMeta(), responseItems);
    }

    private void fundResponsesForThePeriod(int period) {
        var startIndex = 0;
        var endIndex = 1;
        var start = dataForCalculation.get(startIndex);
        while (diffMoreThanPeriod(dataForCalculation.get(startIndex).getDate(), dataForCalculation.size() - 1, period * 365)) {
            while (!diffMoreThanPeriod(start.getDate(), endIndex, period * 365)) {
                endIndex++;
            }
            var end = dataForCalculation.get(endIndex);
            responseItems.add(newFundResponseItem(period, start, end));
            startIndex = nextStartIndex(startIndex);
            start = dataForCalculation.get(startIndex);
            endIndex = startIndex + 1;
        }
        Collections.reverse(responseItems);
    }

    private SchemeResponseItem newFundResponseItem(int period, SchemeData start, SchemeData end) {
        return new SchemeResponseItem(getMonth(start), returns(end, start, period), end, start);
    }

    private int nextStartIndex(int startIndex) {
        var endIndex = startIndex + 1;
        final var start = dataForCalculation.get(startIndex).getDate();
        while (!diffMoreThanPeriod(start, endIndex, 30)) {
            endIndex++;
        }
        return endIndex;
    }

    private boolean diffMoreThanPeriod(Date start, int endIndex, int periodInDays) {
        if (dataForCalculation.size() <= endIndex)
            return false;
        final var end = dataForCalculation.get(endIndex).getDate();
        final var diff = start.getTime() - end.getTime();
        return diff >= periodInDays * ONE_DAY_IN_MILLIS;
    }

    public double returns(SchemeData start, SchemeData end, float period) {
        return pow(end.getNav() / start.getNav(), 1 / period) - 1;
    }
}
