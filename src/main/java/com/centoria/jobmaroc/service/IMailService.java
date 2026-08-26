package com.centoria.jobmaroc.service;


public interface IMailService {

	void sendMail(String toMail, String mailCc, String object, String text, byte[] dataBytes, String dataType);
	
}
