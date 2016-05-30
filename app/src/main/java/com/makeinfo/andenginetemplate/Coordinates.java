package com.makeinfo.andenginetemplate;


public class Coordinates {
    public double x;
    public double y;

    public Coordinates(){
        this.x=0;
        this.y=0;
    }

    Coordinates(double x, double y)
    {
        this.x=x;
        this.y=y;
    }

    public boolean equals(Coordinates coordinates2)
    {
        if (coordinates2.x==x && coordinates2.y==y)return true;
        else return false;
    }

    public double getDistance(Coordinates coordinates2)
    {
        return Math.sqrt((coordinates2.x-x)*(coordinates2.x-x))+((coordinates2.y-y)*(coordinates2.y-y));
    }

    public int getIntX()
    {
        return (int) x;
    }

    public int getIntY()
    {
        return (int) y;
    }
}
