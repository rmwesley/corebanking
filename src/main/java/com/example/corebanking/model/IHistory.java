package com.example.corebanking.model;

public interface IHistory {
  default String history() {
    return this.toString();
  };
}
