package com.revature.loan;

public class Main {
    private static final String DROP_TABLES_SQL = """
        DROP TABLE IF EXISTS loans CASCADE;
        DROP TABLE IF EXISTS users CASCADE;
        """;

    private static final String CREATE_TABLES_SQL = """
            CREATE TABLE IF NO EXISTS users (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                email VARCHAR(100) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                role VARCHAR(20) NOT NULL DEFAULT 'user' CHECK (role IN ('admin', 'user'))
            );
            
            CREATE TABLE IF NO EXISTS loans(
                id SERIAL PRIMARY KEY,
                quantity DECIMAL(10,2) NOT NULL CHECK (quantity > 0),
                status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'approved', 'rejected')),
                user_id INT NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            
            );
            
            """;
    private static final String INSERT_DATA_SQL = """
            INSERT INTO users (name, email, password)
            VALUES
            ('Joe', 'juanito@email.com', 'unapassword');
            ('Sabrina','sabs@outlook.com', '1234578')
            """;

    public static void main(String[] args) {
        // Database credentials
        String jdbcUrl = "jdbc:postgresql://localhost:5432/project0";
        String dbUser = "postgres";
        String dbPassword = "1234";

    }
}
