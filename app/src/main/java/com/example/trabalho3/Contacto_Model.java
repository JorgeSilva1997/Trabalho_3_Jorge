package com.example.trabalho3;

import androidx.appcompat.app.AppCompatActivity;

public class Contacto_Model {

    public String id;
    public String name;
    public String lastname;
    public String personal_number;
    public String company_number;
    public String mail;
    public String postalCode;
    //public String user_id;

    public Contacto_Model(String id, String name, String lastname, String personal_number, String company_number,
                          String mail, String postalCode/*, String user_id*/){

        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.personal_number = personal_number;
        this.company_number = company_number;
        this.mail = mail;
        this.postalCode = postalCode;
        //this.user_id = user_id;
    }

    public String getId_contacto() {return id;}

    public void setId_contacto(String id){this.id = id;}


    public String getName() {return name;}

    public void setName(String name){this.name = name;}


    public String getLastname(){return lastname;}

    public void setLastname(String lastname){this.lastname = lastname;}


    public String getPersonal_number(){return personal_number;}

    public void setPersonal_number(String personal_number){this.personal_number = personal_number;}


    public String getCompany_number(){return company_number;}

    public void setCompany_number(String company_number){this.company_number = company_number;}


    public String getMail(){return mail;}

    public void setMail(String mail){this.mail = mail;}


    public String getPostalCode(){return postalCode;}

    public void setPostalCode(String postalCode){this.postalCode = postalCode;}

/*
    public String getUser_id(){return user_id;}

    public void setUser_id(String user_id){this.user_id = user_id;}*/
}
