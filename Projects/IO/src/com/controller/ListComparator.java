package com.controller;

import java.util.ArrayList;
import java.util.Comparator;
//klasa do sortowania list list
class ListComparator<T extends Comparable<Result>> implements Comparator<ArrayList<Result>> {
    @Override
    public int compare(ArrayList<Result> o1, ArrayList<Result> o2) {
    for(int i=0;i<Math.min(o1.size(),o2.size());i++){
        int c = count_sum(o1).compareTo(count_sum(o2));
        if (c != 0) {
            return c;
        }
    }
        return Double.compare(o1.size(), o2.size());
    }
//funkcja liczy sumaryczny koszt zestawienia
    public Double count_sum(ArrayList<Result> results){
        Double sum = 0.0;
        for(Result r: results){
            sum+=r.getSum();
        }
        return sum;
    }
}