package com.abter.springmvc;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

import java.io.*;

public class TestTask2 {
    private final static Integer mainCowId = 1;
    private static Integer sequence = 2;
    private static CustomMap<Integer, DataList<CustomMap<Integer, String>>> cowsList = new CustomMap();
    private static Jedis jedis = new Jedis("http://localhost:6379");

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

        sequence++;
        giveBirth(childId, sequence, "NickName" + sequence);

        endLifeSpan(childId);
        printCowsData();
    }

    private static void addCow(){
        String data = jedis.get("cowInfo");
        if(data == null){
            jedis.set("cowInfo", Integer.toString(1));
        }else{
            jedis.set("cowInfo", Integer.toString(Integer.valueOf(data) + 1));
        }
    }

    private static void addParentCow(Integer parentCowId){
        if(jedis.get(Integer.toString(parentCowId)) == null) {
            String data = jedis.get("parentCowInfo");
            if (data == null) {
                jedis.set("parentCowInfo", Integer.toString(1));
            } else {
                jedis.set("parentCowInfo", Integer.toString(Integer.valueOf(data) + 1));
            }
        }
    }

    private static class CustomMap<K, V> implements Serializable{
        K key;
        V value;

        public CustomMap(){

        }

        public CustomMap(K key, V value){
            this.key = key;
            this.value = value;
        }

        public void put(K key, V value){
            jedis.set(key.toString(), value.toString());
        }

        public V get(K key){
            String data = jedis.get(key.toString());
            Object b = new Gson().fromJson(data, CustomMap.class);
            return (V) b;
        }

        public void remove(K key){
            jedis.del(key.toString());
        }

        public boolean containsKey(K key){
            String data = jedis.get(key.toString());
            if(data != null){
                return true;
            }else {
                return false;
            }
        }

        public void printData(String key)
        {
            System.out.println(jedis.get(key));
        }
    }

    public static class DataList<E> implements Serializable
    {
        //Size of list
        private int size = 0;

        private int idx = -1;

        //Default constructor
        public DataList() {

        }

        //Add method
        public void add(E e) {
            System.out.println("add this.hashCode(): " + this.hashCode());
            size++;
            idx++;
            String data = new Gson().toJson(e);
            jedis.set(this.hashCode() + "_" + idx, data);
        }

        //Get method
        @SuppressWarnings("unchecked")
        public E get(int i) {
            if (i >= size || i < 0) {
                throw new IndexOutOfBoundsException("Index: " + i + ", Size " + i);
            }
            System.out.println("get this.hashCode(): " + this.hashCode());
            String data = jedis.get(this.hashCode() + "_" + i);
            return (E) new Gson().fromJson(data, CustomMap.class);
        }

        //Remove method
        @SuppressWarnings("unchecked")
        public void remove(int i) {
            if (i >= size || i < 0) {
                throw new IndexOutOfBoundsException("Index: " + i + ", Size " + i);
            }
            jedis.del(String.valueOf(this.hashCode() + i));
        }

        //Get Size of list
        public int size() {
            return size;
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append('#');
            for(int i = 0; i < size ;i++) {
                sb.append(jedis.get(Integer.toString(this.hashCode()) + "_" + i));
                if(i<size-1){
                    sb.append(",");
                }
            }
            sb.append('#');
            return sb.toString();
        }
    }

    private static boolean giveBirth(final Integer parentCowId, final Integer childCowId, final String childNickName){
        CustomMap cow = new CustomMap(childCowId, childNickName);
        CustomMap cowParent = new CustomMap(parentCowId, cow);
        cowParent.put(parentCowId, cow);
        cow.put(childCowId, parentCowId);

        if(cowsList.containsKey(parentCowId)){
            cowsList.get(parentCowId).add(cow);
        }else{
            DataList list = new DataList();
            list.add(cow);
            cowsList.put(parentCowId, list);
        }

        addCow();
        addParentCow(parentCowId);
        return true;
    }

    private static boolean endLifeSpan(final Integer cowId){
        Integer parentId = Integer.valueOf(jedis.get(Integer.toString(cowId)));
        jedis.del(Integer.toString(parentId));
        cowsList.remove(parentId);
        return true;
    }


    //api for print data
    private static void printCowsData(){
        System.out.println("parentCowCount: "  + jedis.get("parentCowInfo") + "; cowCount: " + jedis.get("cowInfo"));
    }
}
