package com.centoria.jobmaroc.dao.impl;

import com.centoria.jobmaroc.dao.AbstractSimpleGenericDao;
import com.centoria.jobmaroc.dao.IContactDao;
import com.centoria.jobmaroc.model.Contact;

public class ContactDao extends AbstractSimpleGenericDao<Contact> implements IContactDao {

    private static IContactDao instance = null;

    private ContactDao() {
        targetClass = Contact.class;
    }

    public static IContactDao getInstance() {
        if (instance == null) {
            instance = new ContactDao();
        }
        return instance;
    }
}
