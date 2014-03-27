package com.max.exigo;

import javax.persistence.*;

/**
 * Customer entity bean for MaxReporting (Exigo) customers
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "Customers")
public class Customer
{
    @Id
    @Column(name = "CustomerID")
    private Integer customerId;
    private String firstName;
    private String lastName;
    @Column(name = "Field1")
    private String legalName;
    private Integer customerTypeID;
    private String email;
    private Integer customerStatusID;
    private String mainAddress1;
    private String mainAddress2;
    private String mainCity;
    private String mainState;
    private String mainZip;
    private String mainCountry;
    private String mailAddress1;
    private String mailAddress2;
    private String mailCity;
    private String mailState;
    private String mailZip;
    private String mailCountry;
    private Integer enrollerID;
    private String currencyCode;
    private Integer payableTypeID;


    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Integer customerId)
    {
        this.customerId = customerId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public Integer getCustomerTypeID()
    {
        return customerTypeID;
    }

    public void setCustomerTypeID(Integer customerTypeID)
    {
        this.customerTypeID = customerTypeID;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Integer getCustomerStatusID()
    {
        return customerStatusID;
    }

    public void setCustomerStatusID(Integer customerStatusID)
    {
        this.customerStatusID = customerStatusID;
    }

    public String getMainAddress1()
    {
        return mainAddress1;
    }

    public void setMainAddress1(String mainAddress1)
    {
        this.mainAddress1 = mainAddress1;
    }

    public String getMainAddress2()
    {
        return mainAddress2;
    }

    public void setMainAddress2(String mainAddress2)
    {
        this.mainAddress2 = mainAddress2;
    }

    public String getMainCity()
    {
        return mainCity;
    }

    public void setMainCity(String mainCity)
    {
        this.mainCity = mainCity;
    }

    public String getMainState()
    {
        return mainState;
    }

    public void setMainState(String mainState)
    {
        this.mainState = mainState;
    }

    public String getMainZip()
    {
        return mainZip;
    }

    public void setMainZip(String mainZip)
    {
        this.mainZip = mainZip;
    }

    public String getMainCountry()
    {
        return mainCountry;
    }

    public void setMainCountry(String mainCountry)
    {
        this.mainCountry = mainCountry;
    }

    public String getMailAddress1()
    {
        return mailAddress1;
    }

    public void setMailAddress1(String mailAddress1)
    {
        this.mailAddress1 = mailAddress1;
    }

    public String getMailAddress2()
    {
        return mailAddress2;
    }

    public void setMailAddress2(String mailAddress2)
    {
        this.mailAddress2 = mailAddress2;
    }

    public String getMailCity()
    {
        return mailCity;
    }

    public void setMailCity(String mailCity)
    {
        this.mailCity = mailCity;
    }

    public String getMailState()
    {
        return mailState;
    }

    public void setMailState(String mailState)
    {
        this.mailState = mailState;
    }

    public String getMailZip()
    {
        return mailZip;
    }

    public void setMailZip(String mailZip)
    {
        this.mailZip = mailZip;
    }

    public String getMailCountry()
    {
        return mailCountry;
    }

    public void setMailCountry(String mailCountry)
    {
        this.mailCountry = mailCountry;
    }

    public Integer getEnrollerID()
    {
        return enrollerID;
    }

    public void setEnrollerID(Integer enrollerID)
    {
        this.enrollerID = enrollerID;
    }

    public String getCurrencyCode()
    {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode)
    {
        this.currencyCode = currencyCode;
    }

    public Integer getPayableTypeID()
    {
        return payableTypeID;
    }

    public void setPayableTypeID(Integer payableTypeID)
    {
        this.payableTypeID = payableTypeID;
    }

    public String getLegalName()
    {
        return legalName;
    }

    public void setLegalName(String legalName)
    {
        this.legalName = legalName;
    }
}
