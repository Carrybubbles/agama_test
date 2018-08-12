package ru.fedin.outputs;

import ru.fedin.calculators.Penalty;

import java.util.List;

public class Writer {

    public static void outputResult(final List<Penalty.ResultContainer> resultContainer){
        double amountPenny = 0;
        for(Penalty.ResultContainer currentItem: resultContainer){
            amountPenny += currentItem.getPenny();
            System.out.println(String.format("День=%s , Сумма=%.2f , Пени=%.3f ",currentItem.getDate(),
                                                                                 currentItem.getSum(),
                                                                                 currentItem.getPenny()));
        }
        System.out.println(String.format("Итого %.3f", amountPenny));
    }
}
