/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public abstract class Person {
    // "protected" means child classes (Doctor/Patient) can use these
    protected int id; 
    protected String name;
    protected int age;

    public Person(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    // Getters (Right-click -> Insert Code -> Getters in NetBeans to generate these automatically)
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
}