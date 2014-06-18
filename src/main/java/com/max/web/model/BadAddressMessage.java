package com.max.web.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * POJO Defining the JSON structure of a Message
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BadAddressMessage extends MaxMessage<BadAddressMessage, BadAddressMessage.Subject, MaxMessage.Actor>
{
    @Override
    public BadAddressMessage generateTestInstance()
    {
        return null;
    }

    public class Subject extends MaxMessage.Subject
    {
        private String addy1;
        private String addy2;
        private String city;
        private String state;
        private String zip;
        private String country;

        public String getAddy1()
        {
            return addy1;
        }

        public void setAddy1(String addy1)
        {
            this.addy1 = addy1;
        }

        public String getAddy2()
        {
            return addy2;
        }

        public void setAddy2(String addy2)
        {
            this.addy2 = addy2;
        }

        public String getCity()
        {
            return city;
        }

        public void setCity(String city)
        {
            this.city = city;
        }

        public String getState()
        {
            return state;
        }

        public void setState(String state)
        {
            this.state = state;
        }

        public String getZip()
        {
            return zip;
        }

        public void setZip(String zip)
        {
            this.zip = zip;
        }

        public String getCountry()
        {
            return country;
        }

        public void setCountry(String country)
        {
            this.country = country;
        }
    }
}
