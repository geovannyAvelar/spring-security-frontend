package br.com.avelar.frontend.controllers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.avelar.backend.model.Person;
import br.com.avelar.backend.service.PersonService;

@ViewScoped
@Named
public class PersonController implements Serializable {

    private static final long serialVersionUID = 1034988024648479274L;

    private static final String THIS = "index.xhtml?faces-redirect=true";

    private Person person;

    private List<Person> peopleList;

    @Inject
    private PersonService personService;

    @PostConstruct
    public void init() {
        person = new Person();
        peopleList = personService.findAll();
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<Person> getPeopleList() {
        return peopleList;
    }

    public void setPeopleList(List<Person> peopleList) {
        this.peopleList = peopleList;
    }

    public String savePerson() {
        personService.savePerson(person);
        return THIS;
    }

}
