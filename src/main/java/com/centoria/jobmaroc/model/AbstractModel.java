package com.centoria.jobmaroc.model;

import java.io.Serializable;

import java.time.Instant;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;


/**
 *
 * @author pc
 *
 */
@Getter
@Setter
@Slf4j
public abstract class AbstractModel implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	@BsonId
	@BsonRepresentation(BsonType.OBJECT_ID)
	private String key;

	private Instant creationDate;
	private Instant updateDate;

	private String code;

	private String label;

	private String description;

	private String slug;

	/**
	 * By default object are disabled
	 */
	private boolean enabled = false;

	public AbstractModel() {
		super();
		creationDate = Instant.now();
	}

	public abstract String getCollectionName();


	/**
	 * Convertit l'objet en JSON.
	 */
	public String toJson() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			log.error("Erreur de conversion en JSON", e);
			throw new RuntimeException("Erreur de conversion en JSON", e);
		}
	}


	/**
	 * Convertit un JSON en objet de type `T`.
	 */
	public static <T extends AbstractModel> T fromJson(String json, Class<T> clazz) {
		try {
			return new ObjectMapper().readValue(json, clazz);
		} catch (JsonProcessingException e) {
			log.error("Erreur de conversion JSON vers {}", clazz.getSimpleName(), e);
			throw new RuntimeException("Erreur de conversion JSON vers " + clazz.getSimpleName(), e);
		}
	}

}
