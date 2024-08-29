package com.example.divedeep;

import java.util.ArrayList;

public class BubbleSort {
    private ArrayList<StoredScore> listViewItems;

    public BubbleSort(ArrayList<StoredScore> listViewItems) {
        this.listViewItems = listViewItems;
    }

    // Logic to sort the elements by score (descending), and then by name (alphabetical) for ties
    public void bubbleSort() {
        int n = listViewItems.size();
        for (int m = n; m > 1; m--) {
            for (int i = 0; i < m - 1; i++) {
                // compare gold, if they are the same compare deepest dive
                if(listViewItems.get(i).getGoldMined() < listViewItems.get(i + 1).getGoldMined()) {
                    //swap items
                    StoredScore temp = listViewItems.get(i);
                    listViewItems.set(i, listViewItems.get(i + 1));
                    listViewItems.set(i + 1, temp);
                }
                else if(listViewItems.get(i).getGoldMined() == listViewItems.get(i+1).getGoldMined()) {
                    if(listViewItems.get(i).getDeepestDive() < listViewItems.get(i + 1).getDeepestDive()) {
                        //swap items
                        StoredScore temp = listViewItems.get(i);
                        listViewItems.set(i, listViewItems.get(i + 1));
                        listViewItems.set(i + 1, temp);
                    }
                    else if(listViewItems.get(i).getDeepestDive() == listViewItems.get(i + 1).getDeepestDive()){
                        if(listViewItems.get(i).getName().compareTo(listViewItems.get(i + 1).getName()) > 0){
                            //swap items
                            StoredScore temp = listViewItems.get(i);
                            listViewItems.set(i, listViewItems.get(i + 1));
                            listViewItems.set(i + 1, temp);
                        }
                    }
                }
            }
        }
    }

    public ArrayList<StoredScore> getSortedListViewItems() {
        return listViewItems;
    }
}
