package ru.fedin.processors;

import ru.fedin.csv.csvdata.CSVData;
import ru.fedin.csv.reader.CSVReader;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;

public class CSVProcessor {

    public static class ProcessCSVRawData {
        private double sum;
        private double addSum;
        private double sumPayment;

        public ProcessCSVRawData(final double sum, final double addSum, final double sumPayment) {
            this.sum = sum;
            this.addSum = addSum;
            this.sumPayment = sumPayment;
        }

        public double getSum() {
            return sum;
        }

        public double getAddSum() {
            return addSum;
        }

        public double getSumPayment() {
            return sumPayment;
        }

    }

    public static ProcessCSVRawData processCSV(final CSVReader csvReader,
                                               final int delayDay,
                                               final int incomeDay,
                                               final YearMonth calculationDate) throws IOException {
        double payment = 0;
        double sum = 0;
        double addSum = 0;
        if(!(csvReader.hasNext())){
            throw new IllegalStateException("Empty file");
        }
        while(csvReader.hasNext()){
            LinkedList<CSVData> csvDataLinkedList = csvReader.readPartCSV();
            for(CSVData csvCurrentData: csvDataLinkedList) {
                YearMonth periodDate = csvCurrentData.getPeriodDate();
                YearMonth creationDate = YearMonth.of(csvCurrentData.getCreationDate().getYear(),
                                                        csvCurrentData.getCreationDate().getMonth());
                LocalDate debtDate = getDebtPeriod(periodDate,delayDay,incomeDay);
                if (compareDatesMonth(YearMonth.of(debtDate.getYear(),debtDate.getMonth()), calculationDate) == -1
                                     && isAccrual(csvCurrentData)) {
                    sum += csvCurrentData.getSum();
                } else if (compareDatesMonth(YearMonth.of(debtDate.getYear(),debtDate.getMonth()), calculationDate) == 0
                                            && isAccrual(csvCurrentData)) {
                    addSum += csvCurrentData.getSum();
                }else if (compareDatesMonth(creationDate, calculationDate) == -1
                                            && isPayment(csvCurrentData)) {
                    payment += csvCurrentData.getSum();
                }
            }
        }

        return new ProcessCSVRawData(sum,addSum,payment);
    }

    private static boolean isPayment(final CSVData data) {
        return CSVData.DocumentType.PAYMENT == data.getDocumentType();
    }
    private static boolean isAccrual(final CSVData data) {
        return CSVData.DocumentType.ACCRUAL == data.getDocumentType();
    }

    private static LocalDate getDebtPeriod(final YearMonth date, final int delayDay, final int incomeDay){
        YearMonth month = YearMonth.from(date);
        LocalDate end   = month.atEndOfMonth();
        int maxDayInMonth = end.getDayOfMonth();
        int pennyDay = incomeDay;

        if(maxDayInMonth < pennyDay){
            pennyDay = maxDayInMonth;
        }

        LocalDate debtDate = LocalDate.of(date.getYear(),date.getMonth(),pennyDay);
        debtDate = debtDate.plusMonths(1);
        debtDate = debtDate.plusDays(delayDay);
        return debtDate;
    }

    private static int compareDatesMonth(final YearMonth date1, final YearMonth date2){
        if(date1.compareTo(date2) < 0){
            return -1;
        }else if(date1.getMonth() == date2.getMonth()){
            return 0;
        }else{
            return 1;
        }
    }
}
