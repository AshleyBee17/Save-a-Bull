package edu.usf.cse.labrador.save_a_bull.sqlite.database.model;

import java.util.StringTokenizer;

public class Address
{
    private String addr1;
    private String addr2;
    private String city;
    private String state;
    private String zipcode;
    private String line;
    public Address(String a1, String a2, String c, String s, String z)
    {
        addr1 = a1;
        addr2 = a2;
        city = c;
        state = s;
        zipcode = z;

    }

    public Address(String ln)
    {
        line = ln;
        StringTokenizer st = new StringTokenizer(line, ",");
        if(st.countTokens() == 4)
        {
            this.addr1 = st.nextToken();
            this.addr2 = null;
            this.city  = st.nextToken();
            this.state = st.nextToken();
            this.zipcode = st.nextToken();
        }

        else if(st.countTokens() == 5)
        {
            this.addr1 = st.nextToken();
            this.addr2 = st.nextToken();
            this.city  = st.nextToken();
            this.state = st.nextToken();
            this.zipcode = st.nextToken();
        }

    }

    public String toString()
    {
        if(this.addr2 == null)
        {
            return this.addr1 + ", " + this.city + ", " + this.state + " " + this.zipcode;
        }

        else
        {
            return this.addr1 + " " +this.addr2 + ", " + this.city + ", " + this.state + " " + this.zipcode;
        }
    }

}