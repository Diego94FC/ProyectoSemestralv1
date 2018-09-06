package com.tareas.diego.proyectosemestralv1;

public class Item {

    public String name;
    public String price;
    public String imageurl;

    Item( String name, String price, String imageurl)
    {
        this.name = name;
        this.price = price;
        this.imageurl = imageurl;
    }

    public String getImageurl() {
        return imageurl;
    }
}
