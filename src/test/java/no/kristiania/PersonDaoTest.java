package no.kristiania;

import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonDaoTest {
    private final PersonDao dao = new PersonDao(createDatasource() );
    @Test
    void shouldRetrieveSavedPersonFromDatabase() throws SQLException {

        Person person = examplePerson(); // hp for å lage random person.
        dao.save(person);

        assertThat(dao.retrieve(person.getId()))
                // hva vi forventer
                .usingRecursiveComparison()
                .isEqualTo(person)
        ;
    }
    @Test
    void shouldListPeopleByLastName() throws SQLException {
        // må simulere tre personer, der to av tre skal listes ut av metoden.
        // 1
        Person matchingPerson = examplePerson(); // hp for å lage random person.
        matchingPerson.setLastName("Testperson"); //  overskriver etternavn for testen
        dao.save(matchingPerson);
        // 2
        Person anotherMatchingPerson = examplePerson(); // hp for å lage random person.
        anotherMatchingPerson.setLastName(matchingPerson.getLastName()); // samme som forrige
        dao.save(anotherMatchingPerson);
        // 3 lager ikke matchende for å teste metoden:
        Person nonMatchingPerson = examplePerson();
        dao.save(nonMatchingPerson);

        // kaller metoden vi skal teste.
        assertThat(dao.listByLastName(matchingPerson.getLastName()))
                .extracting(Person::getId)
                .contains(matchingPerson.getId(),anotherMatchingPerson.getId())
                .doesNotContain(nonMatchingPerson.getId()) ;

    }
    // hjelpe metoder
    private Person examplePerson() {
        Person person = new Person();
        person.setFirstName(pickOne("Sigmund", "Sigurd", "Harald", "Harry"));
        person.setLastName(pickOne("Persson", "Olsen", "Stølsen", "Geiranger"));
        return person;
    }

    public static String pickOne(String... alternatives){
         return alternatives[new Random().nextInt(alternatives.length)];
    }

    private DataSource createDatasource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://localhost:5432/people_db");
        dataSource.setUser("people_dbuser");
        dataSource.setPassword("k%3'`(?Qu?");
        return dataSource;
    }
}
