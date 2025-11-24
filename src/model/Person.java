package model;

public abstract class Person {
    
    private int id; 
    private String name;
    private int age;
    private String gender;
    private String contact_info;

    // 2. The Constructor passes data to these private fields
    public Person(int id, String name, int age, String gender, String contact_info) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contact_info = contact_info;
    }

    // 3. Child classes MUST use these methods to access the data
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender(){ return gender; }
    public String getContactInfo(){ return contact_info; } // Fixed naming convention
}