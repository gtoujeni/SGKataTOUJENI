package com.sg.bank.account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client implements Serializable {
    private static final long serialVersionID = 1l;

    private Long clientId;
    private String firstName;
    private String lastName;


}
