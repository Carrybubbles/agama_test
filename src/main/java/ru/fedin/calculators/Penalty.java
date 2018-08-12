package ru.fedin.calculators;

import ru.fedin.csv.reader.CSVReader;
import ru.fedin.processors.CSVProcessor;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

public class Penalty {

    private static final Logger log = Logger.getLogger(Penalty.class);

    private CSVReader csvReader;
    private float refineRate;
    private int incomeDay;
    private int delayDay;
    private int reduce;
    private YearMonth calculationDate;

    public class ResultContainer{
        private LocalDate date;
        private double sum;
        private double penny;

        public ResultContainer(final LocalDate date, final double sum, final double penny) {
            this.date = date;
            this.sum = sum;
            this.penny = penny;
        }

        public LocalDate getDate() {
            return date;
        }

        public double getSum() {
            return sum;
        }

        public double getPenny() {
            return penny;
        }
    }

    public Penalty(final String pathToCSV,
                   final float refineRate,
                   final int incomeDay,
                   final int delayDay,
                   final int reduce,
                   final YearMonth calculationDate) throws IOException, ParseException {
        this.csvReader = new CSVReader(pathToCSV);
        this.refineRate = refineRate;
        if(incomeDay <= 0 || incomeDay > 31){
            throw new IllegalArgumentException("income day must be from 0 to 31");
        }
        this.incomeDay = incomeDay;
        this.delayDay = delayDay;
        this.reduce = reduce;
        this.calculationDate = calculationDate;
    }

    private double calculatePenny(final double reduceDebt){
        return refineRate * reduceDebt / 100. / reduce;
    }

    public List<ResultContainer> calculate(){
        List<ResultContainer> resultList = new LinkedList<>();
        LocalDate startDate = LocalDate.of(calculationDate.getYear(),calculationDate.getMonth(),1);
        LocalDate endDate = calculationDate.atEndOfMonth();
        try {
            CSVProcessor.ProcessCSVRawData outputData = CSVProcessor.processCSV(csvReader,delayDay,incomeDay,calculationDate);
            double sum = outputData.getSum();
            double addSum = outputData.getAddSum();
            double payment = -outputData.getSumPayment();
            double reduceDebt;
            if(sum - payment <= 0){
                reduceDebt = 0;
            }else{
                reduceDebt = sum - payment;
            }
            for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
                if(d.getDayOfMonth() == incomeDay + 1){
                    reduceDebt += addSum;
                    if(reduceDebt - payment > 0){
                        reduceDebt -= payment;
                    }
                }
                double penny = calculatePenny(reduceDebt);
                resultList.add(new ResultContainer(d,reduceDebt,penny));
            }
        } catch (IOException e) {
            log.error(e);
        }
        return resultList;
    }
}
