package no.kristiania;

import javax.sql.DataSource;

public class PersonDao {

    private Person person;

    // konst
    public PersonDao(DataSource datasource) {

    }

    // skal lagre en person, når den tar inn en person.
    public void save(Person person) {
        this.person = person;
    }

    // gir oss tilbake en person, når vi tar inn en id. liten long fordi id kan ikke være null, kan ikke be om noen som ikke er der.
    public Person retrieve(Long id) {
        return person;
    }
}
