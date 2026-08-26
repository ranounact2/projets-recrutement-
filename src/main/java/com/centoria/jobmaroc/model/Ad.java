package com.centoria.jobmaroc.model;

import com.centoria.jobmaroc.model.security.User;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
public class Ad extends AbstractModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final int ANNOUNCE_TYPE_SCRAPPY = 0;
    public static final int ANNOUNCE_TYPE_SCRAPPY_WITH_EMAIL = 1;
    public static final int ANNOUNCE_TYPE_STAR = 2;
    public static final int ANNOUNCE_TYPE_NORMAL = 3;

    public static final String DELETED = "deleted";
    public static final String VALID = "valid";
    public static final String DISABLED = "disabled";
    public static final String NEW_VERIFIED = "new_verified";
    public static final String UPDATED_VERIFIED = "updated_verified";
    public static final String NEW = "new";
    public static final String UPDATED = "updated";

    public static final int NEW_VERIFIED_RANK = 1;
    public static final int UPDATED_VERIFIED_RANK = 1;
    public static final int NEW_RANK = 2;
    public static final int UPDATED_RANK = 2;
    public static final int VALID_RANK = 3;
    public static final int DISABLED_RANK = 4;
    public static final int DELETED_RANK = 5;


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

    private String facebook;
    private String twitter;
    private String linkedin;
    private int stateRank;

    private int announcetype = ANNOUNCE_TYPE_NORMAL;//this is the default

    /**
     * Dernière activité sur l’annonce (création ou modification). Utilisé pour le tri back-office « Toutes ».
     */
    private Instant lastActivityAt;

    @Override
    public String getCollectionName() {
        return "job";
    }
}
