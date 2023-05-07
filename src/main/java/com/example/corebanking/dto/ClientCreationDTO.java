package com.example.corebanking.dto;

public class ClientCreationDTO {
    public String name;

    public ClientCreationDTO(){
    }

    public ClientCreationDTO(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
