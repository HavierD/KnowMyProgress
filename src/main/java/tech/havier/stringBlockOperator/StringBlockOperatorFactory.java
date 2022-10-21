package tech.havier.stringBlockOperator;

import tech.havier.stringBlockDelegate.StringBlockImporter;

public class StringBlockOperatorFactory {
    private int stringBlockOperatorVersion;
    public StringBlockOperator createStringBlockOperator(StringBlockImporter stringBlockImporter) throws Exception {
        stringBlockOperatorVersion = 2;
        switch (stringBlockOperatorVersion){
            case 1:
                return new StringBlockOperator1(stringBlockImporter);
            case 2:
                return new StringBlockOperator2(stringBlockImporter);
            default:
                throw new RuntimeException("Creating string block operator failed: unknown operator version " + stringBlockOperatorVersion);
        }
    }
}
