package com.maveric.bank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Branch {

    @Id
    private String branchId;
    private String branchName;
    private String branchAddress;
    private double assets;
}
