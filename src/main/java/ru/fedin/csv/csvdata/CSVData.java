package ru.fedin.csv.csvdata;

import java.time.LocalDate;
import java.time.YearMonth;

public class CSVData {
    public enum DocumentType {ACCRUAL,PAYMENT, RECALCULATION, UNDEFINED}
    public enum Status {ACCEPT, UNDEFINED;}

    private YearMonth periodDate;
    private YearMonth accountDate;
    private LocalDate documentDate;
    private LocalDate creationDate;
    private DocumentType documentType;
    private String usage;
    private double sum;
    private Status status;

    public CSVData(final YearMonth periodDate,
                   final YearMonth accountDate,
                   final LocalDate documentDate,
                   final LocalDate creationDate,
                   final DocumentType documentType,
                   final String usage,
                   final double sum,
                   final Status status) {
        this.periodDate = periodDate;
        this.accountDate = accountDate;
        this.documentDate = documentDate;
        this.creationDate = creationDate;
        this.documentType = documentType;
        this.usage = usage;
        this.sum = sum;
        this.status = status;
    }

    public YearMonth getPeriodDate() {
        return periodDate;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public double getSum() {
        return sum;
    }

}

