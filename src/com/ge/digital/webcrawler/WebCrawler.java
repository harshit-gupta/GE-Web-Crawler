package com.ge.digital.webcrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class WebCrawler {
    private static ArrayList<Page> internet;
    
    private static final String INTERNET_ONE = "{\"pages\":[{\"address\":\"http://" +
            "foo.bar.com/p1\",\"links\":[\"http://foo.bar.com/p2\",\"http://f" +
            "oo.bar.com/p3\",\"http://foo.bar.com/p4\"]},{\"address\":\"http:" +
            "//foo.bar.com/p2\",\"links\":[\"http://foo.bar.com/p2\",\"http:/" +
            "/foo.bar.com/p4\"]},{\"address\":\"http://foo.bar.com/p4\",\"lin" +
            "ks\":[\"http://foo.bar.com/p5\",\"http://foo.bar.com/p1\",\"http" +
            "://foo.bar.com/p6\"]},{\"address\":\"http://foo.bar.com/p5\",\"l" +
            "inks\":[]},{\"address\":\"http://foo.bar.com/p6\",\"links\":[\"h" +
            "ttp://foo.bar.com/p7\",\"http://foo.bar.com/p4\",\"http://foo.ba" +
            "r.com/p5\"] } ] }";
    private static final String INTERNET_TWO = "{\"pages\":[{\"address\":\"http://" +
            "foo.bar.com/p1\",\"links\":[\"http://foo.bar.com/p2\"]},{\"addre" +
            "ss\":\"http://foo.bar.com/p2\",\"links\":[\"http://foo.bar.com/p" +
            "3\"]},{\"address\":\"http://foo.bar.com/p3\",\"links\":[\"http:/" +
            "/foo.bar.com/p4\"]},{\"address\":\"http://foo.bar.com/p4\",\"lin" +
            "ks\":[\"http://foo.bar.com/p5\"]},{\"address\":\"http://foo.bar." +
            "com/p5\",\"links\":[\"http://foo.bar.com/p1\"]},{\"address\":\"h" +
            "ttp://foo.bar.com/p6\",\"links\":[\"http://foo.bar.com/p1\"] } ] }";
	private static final String INTERNET_THREE = "{\"pages\":[{\"address\":\"http://" +
            "foo.bar.com/p1\",\"links\":[\"http://foo.bar.com/p999999999\"]}," +
			"{\"address\":\"http://foo.bar.com/p2\",\"links\":[\"http://foo.b" +
            "ar.com/p3\"]},{\"address\":\"http://foo.bar.com/p3\",\"links\":[" +
			"\"http://foo.bar.com/p4\"]},{\"address\":\"http://foo.bar.com/p4" +
            "\",\"links\":[\"http://foo.bar.com/p5\"]},{\"address\":\"http://" +
			"foo.bar.com/p5\",\"links\":[\"http://foo.bar.com/p1\"]},{\"addre" +
            "ss\":\"http://foo.bar.com/p6\",\"links\":[\"http://foo.bar.com/p1\"]}]}";

	/*Main Method Starts*/
    public static void main(String[] args) throws IOException {
        feed("JSON TEST 1");
        run(INTERNET_ONE);
        feed("JSON TEST 2");
        run(INTERNET_TWO);
        feed("JSON TEST 3");
        run(INTERNET_THREE);
    }

    private static void feed(String testName) {
        System.out.print("\nPlease press <ENTER> to run " + testName);
        new Scanner(System.in).nextLine();
    }

    private static void run(String json) throws IOException {
        internet = new ArrayList<>();
        parse(json);
        crawlInternet();
    }

    private static void parse(String json) throws IOException {
        Page pg = new Page();
        JsonParser parser = new JsonFactory().createJsonParser(json);
        parser.nextToken();
        while (parser.hasCurrentToken()) {
            String element = parser.getCurrentName();
            if ("address".equals(element)) {
                parser.nextToken();
                pg.setAddress(parser.getText());
            }
            
            if ("links".equals(element)) {
                parser.nextToken();
                ObjectMapper objMapper = new ObjectMapper();
                ArrayNode node = objMapper.readTree(parser);
                Iterator<JsonNode> iterator = node.elements();
                String[] array = new String[node.size()];
                for (int i = 0; i < node.size(); i++) {
                    if (iterator.hasNext()) {
                        array[i] = iterator.next().asText();
                    }
                }
                pg.setLinks(array);
                internet.add(pg);
                pg = new Page();
            }
            parser.nextToken();
        }
    }

    private static void crawlInternet() throws IOException {
        ArrayList<String> address = new ArrayList<>();
        ArrayList<String> links = new ArrayList<>();
        
        for (Page p : internet) {
            String a = p.getAddress();
            address.add(a);
            Collections.addAll(links, p.getLinks());
        }
        
        TreeSet<String> visitedLinks = new TreeSet<>();
        TreeSet<String> skippedLinks = new TreeSet<>();
        TreeSet<String> invalidLinks = new TreeSet<>();
        visitedLinks.add(address.get(0));
        
        for (String str : links) {
            if (address.contains(str)) {
                if (!visitedLinks.contains(str)) {
                    visitedLinks.add(str);
                } else {
                    skippedLinks.add(str);
                }
            } else {
                invalidLinks.add(str);
            }
        }
        
        /*Printing Passed Links*/
        System.out.println("\nSUCCESS:");
        for (String s : visitedLinks) {
            System.out.println(s);
        }
        
        /*Printing Skipped Links*/
        System.out.println("\nSKIPPED:");
        if (skippedLinks.isEmpty()) {
            System.out.println("NONE");
        } else {
            for (String s : skippedLinks) {
                System.out.println(s);
            }
        }
        
        /*Printing Erroneous Links*/
        System.out.println("\nERRORS:");
        if (invalidLinks.isEmpty()) {
            System.out.println("NONE");
        } else {
            for (String s : invalidLinks) {
                System.out.println(s);
            }
        }
        System.out.println();
    }

}