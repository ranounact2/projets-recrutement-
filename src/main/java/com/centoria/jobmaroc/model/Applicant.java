package com.centoria.jobmaroc.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Applicant extends AbstractModel {

	public static final String NEW = "new";
	public static final int NEW_RANK = 2;
	
	private static final long serialVersionUID = 1L;

	
	private String nom;

	private String prenom;

	private String email;

	private String password;

	private String motivation;

	private String experienceLevel;

	private String formation;

	private String phone;

	private String state;
	
	private String jobId;

	private int stateRank;

	/** Métadonnées du CV (pas de binaire : fichier sur OVH). */
	private Cv cv;

	@Override
	public String getCollectionName() {
		return "applicant";
	}

	

}
