package com.controller;

import java.util.ArrayList;
import java.util.Comparator;
//klasa do sortowania list list
class ListComparator<T extends Comparable<Result>> implements Comparator<ArrayList<Result>> {

    //porownywanie list, po
    @Override
    public int compare(ArrayList<Result> o1, ArrayList<Result> o2) {
        for (int i = 0; i < Math.min(o1.size(), o2.size()); i++) {
            int c = count_sum(o1).compareTo(count_sum(o2));
            if (c != 0) {
                return c;
            }
        }
        return Double.compare(o1.size(), o2.size());
    }

    //funkcja liczy sumaryczny koszt zestawienia
    public Double count_sum(ArrayList<Result> results) {
        Double sum = 0.0;
        for (Result r : results) {
            sum += r.getCost();
        }
        sum += count_shipping(results);
        return sum;
    }


    //zwraca sumaryczny koszt dostawy produktow z listy (uwzgledniajac czy sa z tego samego sklepu)
    public Double count_shipping(ArrayList<Result> listOfResults) {

        ArrayList<ArrayList<Result>> shops = new ArrayList<ArrayList<Result>>(); // w tej liscie trzymamy listy produktow o tych samych shop_id

        for (Result r : listOfResults) {
            if (shops.isEmpty()) {
                shops.add(new ArrayList());  // utworz nowa liste dla tego shop_id
                shops.get(0).add(r);  // i dopisz p do tej listy
            } else {
                int i = 0;
                Boolean addNewList = true;
                while (i < shops.size()) {
                    if (r.getShop_id() == shops.get(i).get(0).getShop_id()) { // jesli lista dla tego shop_id juz istnieje
                        shops.get(i).add(r);
                        addNewList = false;
                        break;
                    }
                    i += 1;
                }
                if (addNewList) {
                    shops.add(new ArrayList());  // utworz nowa liste dla tego shop_id
                    shops.get(shops.size() - 1).add(r);  // i dopisz p do tej listy
                }
            }
        }

        Double summaryShipping = 0.0;
        for (ArrayList<Result> shop : shops) {// i teraz bierzemy maks. koszty dostawy dla kazdego sklepu
            shop.sort(Result::compareTo);
            //Collections.sort(shop,Collections.reverseOrder(Result::compareTo));
            summaryShipping += shop.get(0).getMin_Shipping();
        }

        return summaryShipping;
    }
}