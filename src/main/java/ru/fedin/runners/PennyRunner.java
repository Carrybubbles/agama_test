package ru.fedin.runners;

import org.apache.log4j.Logger;
import ru.fedin.calculators.Penalty;
import ru.fedin.outputs.Writer;

import java.io.IOException;
import java.text.ParseException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PennyRunner implements Runnable {

    private static final Logger log = Logger.getLogger(PennyRunner.class);

    private String args[];

    public PennyRunner(final String[] args) {
        this.args = args;
    }

    @Override
    public void run() {
        final DateTimeFormatter format = DateTimeFormatter.ofPattern("MM.yyyy");
        final YearMonth date = YearMonth.parse(args[5],format);
        try {
            Penalty penalty = new Penalty(args[0],
                    Float.valueOf(args[1]),
                    Integer.valueOf(args[2]),
                    Integer.valueOf(args[3]),
                    Integer.valueOf(args[4]),
                    date);
            List<Penalty.ResultContainer> resultContainerList = penalty.calculate();
            Writer.outputResult(resultContainerList);
        } catch (IOException | ParseException e) {
            log.error(e);
        }
    }
}
