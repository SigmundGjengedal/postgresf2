package no.kristiania;

import org.junit.jupiter.api.Test;

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
        return new Person();
    }

    private DataSource createDatasource() {
        return null;
    }
}
