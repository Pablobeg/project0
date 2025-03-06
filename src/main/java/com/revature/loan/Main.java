package com.revature.loan;

import com.revature.loan.controller.UserController;
import com.revature.loan.dao.UserDao;
import com.revature.loan.service.UserService;
import io.javalin.Javalin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    private static final String DROP_TABLES_SQL = """
        DROP TABLE IF EXISTS loans CASCADE;
        DROP TABLE IF EXISTS users CASCADE;
        """;

    private static final String CREATE_TABLES_SQL = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                email VARCHAR(100) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                role VARCHAR(20) NOT NULL DEFAULT 'user' CHECK (role IN ('admin', 'user'))
            );
            
            CREATE TABLE IF NOT EXISTS loans(
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
            ('Joe', 'juanito@email.com', 'unapassword'),
            ('Sabrina','sabs@outlook.com', '12345678');
            
            INSERT INTO loans (quantity, user_id)
            VALUES
            (1000,1),
            (144800,2),
            (51616,1);
            
            INSERT INTO users(name, email, password, role)
            VALUES
            ('Bob', 'rene@email.com', 'ten','admin');
            
            """;

    public static void main(String[] args) {
        // Database credentials
        String jdbcUrl = "jdbc:postgresql://localhost:5432/project0";
        String dbUser = "postgres";
        String dbPassword = "1234";

        // 4) Initialize DB
        resetDatabase(jdbcUrl, dbUser, dbPassword);

        // 5) Create DAOs, Services, Controllers
        UserDao userDao = new UserDao(jdbcUrl, dbUser, dbPassword);


        UserService userService = new UserService(userDao);


        UserController userController = new UserController(userService);


        // 6) Start Javalin (we will use Javalin version 4+)
        Javalin app = Javalin.create(config -> {
            // If needed, you can configure plugins, CORS, etc. here.
            // For example: config.plugins.enableCors(cors -> cors.add(anyOriginAllowed));
            // This is needed when working with web app on a browser.
        }).start(7000);

        // 7) Define routes using the new {param} syntax
        app.post("/auth/register", userController::register);
        app.post("/auth/login", userController::login);
        app.post("/auth/logout",userController::logout);


    }

    private static void resetDatabase(String jdbcUrl, String dbUser, String dbPassword) {
        runSql(DROP_TABLES_SQL, jdbcUrl, dbUser, dbPassword);
        runSql(CREATE_TABLES_SQL, jdbcUrl, dbUser, dbPassword);
        runSql(INSERT_DATA_SQL, jdbcUrl, dbUser, dbPassword);
    }

    private static void runSql(String sql, String jdbcUrl, String dbUser, String dbPassword) {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Executed SQL:\n" + sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
