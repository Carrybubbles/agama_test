package ru.fedin.csv.reader;

import org.apache.log4j.Logger;
import ru.fedin.calculators.Penalty;
import ru.fedin.csv.csvdata.CSVData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedList;

import static ru.fedin.csv.reader.CSVParser.parseCSVRaw;


public class CSVReader {
    private static final int MAX_READ_SIZE = 20;
    private static final String[] HEADERS = {"За период", "Учетный месяц","Дата документа","Создан","Тип", "Расход","Сумма","Статус"};
    private static final Logger log = Logger.getLogger(CSVReader.class);

    private final CSVParser csvParser;
    private Iterator<CSVRecord> iterator;

    public CSVReader(final String path) throws IOException {
        BufferedReader bf = Files.newBufferedReader(Paths.get(path));
        csvParser = new CSVParser(bf, CSVFormat.newFormat(';')
                .withHeader(HEADERS)
                .withFirstRecordAsHeader()
        );
        iterator = csvParser.iterator();
    }

    public LinkedList<CSVData> readPartCSV() throws IOException{
        LinkedList<CSVData> csvDataLinkedList = new LinkedList<>();
        int counter = 0;

        while(iterator.hasNext() && counter++ != MAX_READ_SIZE){
            CSVData currentCSVData = null;
            CSVRecord currentRecord = iterator.next();
            try {
                currentCSVData = parseCSVRaw(currentRecord);
            } catch (ParseException e) {
                log.error(e);
            }
            if(currentCSVData != null) {
                csvDataLinkedList.addLast(currentCSVData);
            }
        }
        return csvDataLinkedList;
    }

    public boolean hasNext(){
        return iterator.hasNext();
    }
}
