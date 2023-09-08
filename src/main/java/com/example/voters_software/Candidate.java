package com.example.voters_software;

public class Candidate {

    private String imagePath;
    private String name;
    private String party;
    private String position;

     public Candidate(String imagePath, String name, String party, String position) {
        this.imagePath = imagePath;
        this.name = name;
        this.party = party;
        this.position = position;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getPosition() {
        return position;
    }
}

