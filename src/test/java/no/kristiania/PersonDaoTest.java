package no.kristiania;

import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonDaoTest {
    @Test
    void shouldRetrieveSavedPersonFromDatabase() throws SQLException {
        PersonDao dao = new PersonDao(createDatasource() );
        Person person = examplePerson(); // hp for å lage random person.
        dao.save(person);

        assertThat(dao.retrieve(person.getId()))
                // hva vi forventer
                .usingRecursiveComparison()
                .isEqualTo(person)
        ;
    }

    // hjelpe metoder
    private Person examplePerson() {
        Person person = new Person();
        person.setFirstName(pickOne());

        return person;
    }

    private DataSource createDatasource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://localhost:5432/people_db");
        dataSource.setUser("people_dbuser");
        dataSource.setPassword("k%3'`(?Qu?");
        return dataSource;
    }
}
