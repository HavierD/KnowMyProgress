package tech.havier.stringBlockOperator;

import tech.havier.databaseOperator.DatabaseFactory;
import tech.havier.stringBlockDelegate.StringBlockImporter;

public class StringBlockOperatorFactory {
    public StringBlockOperator createStringBlockOperator(StringBlockImporter stringBlockImporter) throws Exception {
        var stringBlockOperatorVersion = DatabaseFactory.databaseType;
        switch (stringBlockOperatorVersion){
            case ORACLE_SQL:
                return new StringBlockOperator1(stringBlockImporter);
            case HIBERNATE:
                return new StringBlockOperator2(stringBlockImporter);
            default:
                throw new RuntimeException("Creating string block operator failed: unknown operator version " + stringBlockOperatorVersion);
        }
    }
}
