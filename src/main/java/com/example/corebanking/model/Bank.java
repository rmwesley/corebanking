package com.example.corebanking.model;

import java.util.ArrayList;
import java.util.List;

class Bank {
	private static Bank singleInstance;
	private List<Client> clients;

	private Bank(){
		this.clients = new ArrayList<Client>();
	}
	public static Bank init(){
		if (singleInstance == null) singleInstance = new Bank();
		return singleInstance;
	}
	public void addAccount(String name){
		Client client = new Client(name);
		this.clients.add(client);
	}

	public Client findClient(int id){
        Client searchedClient = clients.stream()
            .filter(client -> client.getId() == id)
            .findAny()
            .orElse(null);
        if(searchedClient == null){
            throw new RuntimeException("Not found");
        }
        return searchedClient;
	}

	public Client findClient(String name){
        Client searchedClient = clients.stream()
            .filter(client -> client.name.equals(name))
            .findAny()
            .orElse(null);
        if(searchedClient == null){
            throw new RuntimeException("Not found");
        }
        return searchedClient;
	}
	public Account findAccount(int id){
        Account searchedAccount = clients.stream()
            .flatMap(client -> client.getAccounts().stream())
            .filter(account -> account.getId() == id)
            .findAny()
            .orElse(null);
        if(searchedAccount == null){
            throw new RuntimeException("Not found");
        }
        return searchedAccount;
	}
}