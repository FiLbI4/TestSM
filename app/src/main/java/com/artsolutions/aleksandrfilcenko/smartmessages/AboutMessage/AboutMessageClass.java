package com.artsolutions.aleksandrfilcenko.smartmessages.AboutMessage;

/**
 * Created by aleksandrfilcenko on 06.05.16.
 */

public class AboutMessageClass {

    private String id;

    private String companyId;

    private String logo;

    private String text;

    private String address;

    private String company;

    private String name;

    private String images;

    private String date;

    private String type;

    private String phone;


    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getLogo ()
    {
        return logo;
    }

    public void setLogo (String logo)
    {
        this.logo = logo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getCompany ()
    {
        return company;
    }

    public void setCompany (String company)
    {
        this.company = company;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getImages ()
    {
        return images;
    }

    public String getDate ()
    {
        return date;
    }

    public String getType ()
    {
        return type;
    }

    public String getCompanyId ()
    {
        return companyId;
    }

    @Override
    public String toString()
    {
        return "id = "+id+", logo = "+logo+", text = "+text+", address = "+address+", company = "+company+", name = "+name+", images = "+images+", date = "+date+"]";
    }
}
