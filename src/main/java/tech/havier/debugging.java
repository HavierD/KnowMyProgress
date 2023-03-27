package tech.havier;

import tech.havier.databaseOperator.ConfigHavi1;
import tech.havier.databaseOperator.DatabaseFactory;
import tech.havier.stringBlockOperator.StringBlockOperator;
import tech.havier.stringBlockOperator.StringBlockOperator2;
import tech.havier.timeToolkit.HavierTimer;

import javax.print.DocFlavor;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class debugging {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        BigInteger a = BigInteger.valueOf(1);
        BigInteger b = BigInteger.valueOf(2);

        System.out.println(a.add(b));

    }

}

