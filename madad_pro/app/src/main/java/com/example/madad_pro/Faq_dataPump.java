package com.example.madad_pro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Faq_dataPump {

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> faqListDetail = new HashMap<String, List<String>>();

        List<String> que1 = new ArrayList<String>();
        que1.add("Helpnet is the network of people where people can help others and also request for help from others.");

        List<String> que2 = new ArrayList<String>();
        que2.add("It is helpful for almost any kind of person whether they are professional common or people in emergency");

        faqListDetail.put("What is helpnet?",que1);
        faqListDetail.put("Who can use it?",que2);
        return faqListDetail;
    }

}
