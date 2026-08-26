package com.centoria.jobmaroc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MailDto<T>{

	String title = null;
	String mailContent = null;
	String subject = null;
	String host = null;
	T object = null; 
	
	public MailDto(String host, T object, String title) {
		super();
		this.host = host;
		this.object = object;
		this.title = title;
	}


}