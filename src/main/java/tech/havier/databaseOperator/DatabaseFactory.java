package tech.havier.databaseOperator;

public class DatabaseFactory {

    private DatabaseType databaseType = DatabaseType.ORACLE_SQL;

    public DatabaseOperator createDatabaseOperator() throws Exception {
        switch (databaseType) {
            case ORACLE_SQL:
                return SqlOperator.getSqlOperatorInstance();
            default:
                throw new RuntimeException("Creating database operator failed: unknown database type: " + databaseType);
        }
    }
}
