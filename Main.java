package com.example.internal;

import com.example.internal.src.applications.App;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
       SwingUtilities.invokeLater(new Runnable() {
           @Override
           public void run() {
               App app = new App();

           }
       })
       ;
    }

}
