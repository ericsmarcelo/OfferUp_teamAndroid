package com.teamandroid.offerup;

/**
 * Created by suridx on 3/4/2018.
 */

public class OffersDat
{
    public String offerby, offerfor;
    public long offerprice;

    public OffersDat()
    {

    }

    public OffersDat(String ob, String of, long op)
    {
        this.offerby = ob;
        this.offerprice = op;
        this.offerfor = of;
    }

}