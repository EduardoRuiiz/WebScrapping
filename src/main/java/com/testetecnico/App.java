package com.testetecnico;

public class App {

    public static void main(String[] args) {
        //WebScrapping.run();
        try {
            TransformationData.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
