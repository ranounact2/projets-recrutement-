package com.centoria.jobmaroc.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import java.util.List;

@Getter
@Setter
public class Cv extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String email;

	/** Non persisté en MongoDB (fichier sur OVH ou uniquement en mémoire pour l’upload / mail). */
	@BsonIgnore
	private byte[] cvFile;

	/** Clé de l'objet dans le bucket OVH S3 (null si non uploadé). */
	private String storageKey;

	/** URL complète de l'objet dans le bucket OVH S3 (null si non uploadé). */
	private String storageUrl;

	/** Contenu textuel extrait du CV pour recherche sémantique */
	private String parsedText;

	/** Vector embedding du contenu du CV */
	private List<Double> embedding;

		
	public Cv() {
	}

	public Cv(String email, byte[] cvFile) {
		super();
		this.email = email;
		this.cvFile = cvFile;
	}


	@Override
	public String getCollectionName() {
		return "cv";
	}

	

}
