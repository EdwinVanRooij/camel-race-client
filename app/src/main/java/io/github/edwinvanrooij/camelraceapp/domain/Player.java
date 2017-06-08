package io.github.edwinvanrooij.camelraceapp.domain;

/**
 * Created by eddy on 6/8/17.
 */

/**
 * Created by eddy
 * on 6/5/17.
 */
public class Player {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

