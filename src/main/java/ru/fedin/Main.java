package ru.fedin;

import ru.fedin.runners.PennyRunner;

import java.io.IOException;
import java.text.ParseException;
import java.util.InputMismatchException;

public class Main {

    public static void main(String... args) throws IOException, ParseException {
        if(args.length != 6){
            throw new InputMismatchException("Check your input, must have 6 arguments (path to scv file, refine rate, income date, delay day, reduce, period)");
        }else{
            new PennyRunner(args).run();
        }
    }
}
