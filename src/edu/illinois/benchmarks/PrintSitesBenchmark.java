package edu.illinois.benchmarks;


import java.io.FileNotFoundException;
import java.util.stream.Collectors;

public class PrintSitesBenchmark extends Benchmark {

    public static void printSitesBenchmark(String outputDirectory) {
        try {
            PrintSitesBenchmark psb = new PrintSitesBenchmark(outputDirectory);
            psb.benchmark();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public PrintSitesBenchmark(String outputDirectory) throws FileNotFoundException {
        super(outputDirectory, "PrintBenchmark");
    }

    @Override
    public void benchmark() {
        StringBuilder sb = new StringBuilder();

        sb.append("============Actual==============\n");
        String actual = sites.stream()
                .map(i -> i.toString())
                .collect(Collectors.joining(" "));
        sb.append(actual + "\n");

        sb.append("============Predicted==============\n");
        String predicted = sites.stream()
                .map(i -> i.toString())
                .collect(Collectors.joining(" "));
        sb.append(predicted + "\n");

        System.out.println(sb.toString());
    }

}
