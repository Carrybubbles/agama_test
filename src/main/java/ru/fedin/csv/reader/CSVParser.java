package ru.fedin.csv.reader;

import ru.fedin.csv.csvdata.CSVData;
import org.apache.commons.csv.CSVRecord;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class CSVParser {
    private static final DateTimeFormatter FORMAT_YM = DateTimeFormatter.ofPattern("MM.yyyy");
    private static final DateTimeFormatter FORMAT_DYM = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter FORMAT_DYMHM = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static CSVData parseCSVRaw(final CSVRecord record) throws ParseException {

        YearMonth periodDate = YearMonth.parse(record.get("За период"),FORMAT_YM);
        YearMonth accountDate =  YearMonth.parse(record.get("Учетный месяц"),FORMAT_YM);
        LocalDate documentDate = LocalDate.parse(record.get("Дата документа"),FORMAT_DYM);
        LocalDate creationDate = LocalDate.parse(record.get("Создан"),FORMAT_DYMHM);
        CSVData.DocumentType documentType = parseDocumentType(record.get("Тип"));
        String usage = record.get("Расход");
        double sum = Double.valueOf(record.get("Сумма"));
        CSVData.Status status = parseStatus(record.get("Статус"));

        return new CSVData(periodDate,accountDate,documentDate,creationDate,documentType,usage,sum,status);
    }

    private static CSVData.DocumentType parseDocumentType(final String documentType){
        switch (documentType){
            case "Начисление":
                return CSVData.DocumentType.ACCRUAL;
            case "Оплата":
                return CSVData.DocumentType.PAYMENT;
            case "Перерасчет":
                return CSVData.DocumentType.RECALCULATION;
            default:
                return CSVData.DocumentType.UNDEFINED;
        }
    }

    private static CSVData.Status parseStatus(final String status){
        switch (status){
            case "Проведён":
                return CSVData.Status.ACCEPT;
            default:
                return CSVData.Status.UNDEFINED;
        }
    }
}
