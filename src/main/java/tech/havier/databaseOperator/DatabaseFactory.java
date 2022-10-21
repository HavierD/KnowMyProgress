package tech.havier.databaseOperator;

import tech.havier.databaseOperator.hibernate.HibernateOperator;
import tech.havier.databaseOperator.sql.SqlOperator;

public class DatabaseFactory {

    private DatabaseType databaseType = DatabaseType.HIBERNATE;

    public DatabaseOperator createDatabaseOperator() throws Exception {
        switch (databaseType) {
            case ORACLE_SQL:
                return SqlOperator.getSqlOperatorInstance();
            case HIBERNATE:
                return new HibernateOperator();
            default:
                throw new RuntimeException("Creating database operator failed: unknown database type: " + databaseType);
        }
    }
}
