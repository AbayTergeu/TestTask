package com.abter.springmvc;

import java.util.*;

public class TestTask {
    private final static Integer mainCowId = 1;
    private static Integer sequence = 2;
    private static HashMap<Integer, List<HashMap<Integer, String>>> cowsList = new HashMap<Integer, List<HashMap<Integer, String>>>();
    private static HashMap<Integer, Integer> parentIds = new HashMap<Integer, Integer>();

    /*
    A dairy farmer wishes to manage the breeding of the cows in his farm. For this reason, he defines the Cow entity (cowId, nickName). Cows can either give birth to calves by insemination or end their life span.
    Our mission is to create a data structure to support the dairy farm and the following operations on it (assume you start with a single cow that is always alive, and that all calves are born female):
    1. GiveBirth (parentCowId, childCowId, childNickName) – adds a new female calf to the farm.
    2. EndLifeSpan (cowId) – removes the cow from the farm.
    3. Print farm data – outputs entire farm to the standard output in a readable manner.
     */
    public static void main(String[] args){
        //Add several cows, start from main cow

        Integer childId = sequence++;
        giveBirth(mainCowId, childId, "NickName" + childId);
        //printCowsData();

        childId = sequence++;
        giveBirth(mainCowId, childId, "NickName" + childId);
        printCowsData();

        Integer parent2 = childId;
        childId = sequence++;
        giveBirth(parent2, childId, "NickName" + childId);
        printCowsData();

        //removes the cow from the farm
        System.out.println("endLifeSpan");
        endLifeSpan(childId);

        //Print farm data
        printCowsData();

    }

    //api for add cow
    private static boolean giveBirth(final Integer parentCowId, final Integer childCowId, final String childNickName){
        HashMap<Integer, String> cow = new HashMap<Integer, String>();
        cow.put(childCowId, childNickName);
        if(cowsList.containsKey(parentCowId)) {
            cowsList.get(parentCowId).add(cow);
        }else {
            List<HashMap<Integer, String>> list = new ArrayList<HashMap<Integer, String>>();
            list.add(cow);
            cowsList.put(parentCowId, list);
        }
        parentIds.put(childCowId, parentCowId);

        return true;
    }

    //api for remove cow
    private static boolean endLifeSpan(final Integer cowId){
        for(Map.Entry<Integer, List<HashMap<Integer, String>>> e : cowsList.entrySet()){
            Iterator<HashMap<Integer, String>> itr = e.getValue().iterator();
            while(itr.hasNext()){
                if(itr.next().containsKey(cowId))
                    itr.remove();
            }
        }
        return true;
    }

    //api for print data
    private static void printCowsData(){
        Integer parentCowCount = 0;
        Integer cowCount = 0;
        for (Map.Entry<Integer, List<HashMap<Integer, String>>> entry : cowsList.entrySet()) {
            Integer parentId = entry.getKey();
            List<HashMap<Integer, String>> childList = entry.getValue();
            if(!childList.isEmpty()){
                parentCowCount ++;
            }
            for(HashMap<Integer, String> map: childList) {
                cowCount ++;
                for (Map.Entry<Integer, String> childEntry : map.entrySet()) {
                    System.out.println("Cow Nickname: " + childEntry.getValue() + "; cowId: " + childEntry.getKey() + "; parent cowId: " + parentId);
                }
            }
        }
        System.out.println("parentCowCount: "  + parentCowCount + "; cowCount: " + cowCount);
    }
}
