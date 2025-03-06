package ru.bichevoy.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.bichevoy.ProgramProperties;

import java.util.List;

@Data
@Slf4j
public class Discount {
    private int minCountVisit;
    private int maxCountVisit;
    private double minPercent;
    private double maxPercent;

    public Discount() {
        try {
            minCountVisit = Integer.parseInt(ProgramProperties.getProperties().getProperty("discount.minCountVisit"));
            minPercent = Double.parseDouble(ProgramProperties.getProperties().getProperty("discount.minPercent"));
            maxCountVisit = Integer.parseInt(ProgramProperties.getProperties().getProperty("discount.maxCountVisit"));
            maxPercent = Double.parseDouble(ProgramProperties.getProperties().getProperty("discount.maxPercent"));
            log.info(String.format("Установлена система скидок[При посещении от %d раз скидка %.1f%%, при посещении от %d раз скидка %.1f%%]",
                    minCountVisit, minPercent, maxCountVisit, maxPercent));
        } catch (NumberFormatException e) {
            log.info(e.getStackTrace().toString());
            throw e;
        }
    }

    public double makeDiscount(List<VisitHistory> visitHistoryList, double cost) {
        int size = visitHistoryList.size();
        if (size >= maxCountVisit) {
            return cost - cost * maxPercent;
        }
        if (size >= minCountVisit) {
            return cost - cost * minPercent;
        }
        return cost;
    }
}
