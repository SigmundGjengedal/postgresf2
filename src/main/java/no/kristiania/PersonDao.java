package no.kristiania;

import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PersonDao {

    private final DataSource dataSource;

    // konst
    public PersonDao(DataSource dataSource) {
        this.dataSource=dataSource;
    }

    public static DataSource createDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://localhost:5432/people_db");
        dataSource.setUser("people_dbuser");
        dataSource.setPassword("k%3'`(?Qu?");

        // Flyway bibliotek som migrere dataene til den siste versjonen av mine tabelldefinisjoner.
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
        return dataSource;
    }

    // skal lagre en person, når den tar inn en person.
    public void save(Person person) throws SQLException {
        try (Connection connection = dataSource.getConnection() ) { // For å connecte til db trenger vi å getConnection. Try for å close
            // preperer statement og setter variabler på det:
            try (PreparedStatement statement = connection.prepareStatement("insert into person (first_name, last_name) values (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1,person.getFirstName());
                statement.setString(2,person.getLastName());
                // utfører statement:
                statement.executeUpdate();
                // må hente ut PK som ble autogenerert og også sette denne som en variabel i person objektet.
                try (ResultSet rsKeys = statement.getGeneratedKeys()) {
                    rsKeys.next(); // må lese første rad, starter utenfor av en retard grunn.
                    person.setId(rsKeys.getLong("id"));
                }
            }
        }
    }

    // gir oss tilbake en person, når vi tar inn en id. liten long fordi id kan ikke være null, kan ikke be om noen som ikke er der.
    public Person retrieve(Long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from person where id = ? ")) {
                statement.setLong(1,id);
                // henter og setter data fra et resultset fra query
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return mapPersonFromResultSet(resultSet);

                }
            }
        }
    }

    private Person mapPersonFromResultSet(ResultSet resultSet) throws SQLException {
        Person person = new Person();
        person.setId(resultSet.getLong("id")); // setter id og navn i objektet
        person.setFirstName(resultSet.getString("first_name"));
        person.setLastName(resultSet.getString("last_name"));
        return person;
    }

    public ArrayList<Person> listByLastName(String lastName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from person where last_name = ? ")) {
                statement.setString(1, lastName);
                // henter og setter data fra et resultset fra query
                try (ResultSet resultSet = statement.executeQuery()) {
                    ArrayList<Person> matchingPersonsList = new ArrayList<>();
                    while(resultSet.next()){
                        matchingPersonsList.add(mapPersonFromResultSet(resultSet));
                    }
                    return matchingPersonsList;
                }
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        // prøver å liste ut personer som finnes fra main
        PersonDao dao = new PersonDao(createDataSource());
        System.out.println("Please enter a last name: ");
        Scanner scanner =  new Scanner(System.in);
        String lastname = scanner.nextLine().trim();
        System.out.println(dao.listByLastName(lastname));
    }
}
