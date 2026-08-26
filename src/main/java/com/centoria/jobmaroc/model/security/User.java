package com.centoria.jobmaroc.model.security;

import com.centoria.jobmaroc.model.AbstractModel;
import com.centoria.jobmaroc.model.Adresse;


public class User extends AbstractModel {

    public final static int SOCIAL_TYPE_FACEBOOK = 1;

    public final static int SOCIAL_TYPE_GOOGLE = 2;

    public final static int SOCIAL_TYPE_CLIENT = 3;


    private String otherId;

    private String firstName;

    private String lastName;

    private String tel;

    private String mail;

    private transient String password;

    private Adresse adresse;

    private String authToken;

    private String profilId;

    private Profil profil;

    private boolean abonne = false;

    private boolean valid = false;

    private String validationKey = null;

    private String lat;

    private String lng;

    private int socialType;

    public User() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAbonne() {
        return abonne;
    }

    public void setAbonne(boolean abonne) {
        this.abonne = abonne;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getValidationKey() {
        return validationKey;
    }

    public void setValidationKey(String validationKey) {
        this.validationKey = validationKey;
    }

    public String getProfilId() {
        return profilId;
    }

    public void setProfilId(String profilId) {
        this.profilId = profilId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public Profil getProfil() {
        return profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    public int getSocialType() {
        return socialType;
    }

    public void setSocialType(int socialType) {
        this.socialType = socialType;
    }

    @Override
    public String getCollectionName() {
        return "user";
    }

}
