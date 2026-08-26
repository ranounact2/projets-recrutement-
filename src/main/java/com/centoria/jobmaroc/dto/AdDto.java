package com.centoria.jobmaroc.dto;

import com.centoria.jobmaroc.model.security.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class AdDto {

    private static final long serialVersionUID = 1L;

    private String key;

    private Date creationDate;

    private Date updateDate;

    private Date lastActivityAt;

    private String code;

    private String label;

    private String description;

    private String slug;

    private User user;

    private String title;

    private String img;

    private String content;

    private String email;

    private String secretCode;

    private String password;

    private String type;

    private String phone;

    private String city;

    private String domain;

    private String experienceLevel;

    private String formation;

    private int nbrDePostes;

    private String confidentiality;

    private String companyName;

    private String companyCode;

    private String state;

    private int stateRank;
    private String facebook;
    private String twitter;
    private String linkedin;

    private int announcetype;//this is the defaul
    private long totalAds;
}
