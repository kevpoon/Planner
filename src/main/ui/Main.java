package ui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    public static void main(String[] args) {
        try {
            new CalendarDrawer();
        } catch (FileNotFoundException e) {
            System.out.println("No save found, please create a new Calendar next time.");
        }

//        System.out.println("<._.<");
//        System.out.println(">._.>");
//        System.out.println("^._.^");
//        System.out.println("v._.v");

    }

}