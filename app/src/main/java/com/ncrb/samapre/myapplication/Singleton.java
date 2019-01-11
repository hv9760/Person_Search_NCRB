package com.ncrb.samapre.myapplication;


/**
 * Created by Lenovo on 13-04-2016.
 */
public class Singleton {

    private static Singleton singleton = new Singleton( );

    public String username ;


    public String firregnum;
    public String district_cd;
    public String ps_cd;
    public String year;
    public String coco_seed_cd = "SEED";
    public String sedition_token = "";

    /**
     *
     * A private Constructor prevents any other
     * class from instantiating.
     *
     */
    private Singleton(){ }

    /* Static 'instance' method */
    public static Singleton getInstance( ) {
        return singleton;
    }

    /* Other methods protected by singleton-ness */
    protected static void demoMethod( ) {
        System.out.println("demoMethod for singleton");
    }

}// end main singleton
