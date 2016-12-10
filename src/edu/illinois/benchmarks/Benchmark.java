package edu.illinois.benchmarks;

import edu.illinois.Matrix.MotifMatrix;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public abstract class Benchmark {
    String BENCHMARK;
    private List<String> motifs;
    private List<String> predictedMotifs;
    List<Integer> sites;
    List<Integer> predictedSites;
    private MotifMatrix motifMatrix;
    private MotifMatrix predictedMotifMatrix;
    String outputDirectory;
    int motifLength;

    Benchmark(String outputDirectory, String BENCHMARK) throws FileNotFoundException {
        this.outputDirectory = outputDirectory;

        //read sequence information
        readMotifFile(false);
        readSitesFile(false);
        readMotifLengthFile();

        //read predictions
        readMotifFile(true);
        readSitesFile(true);

        //print that we are starting a benchmark
        this.BENCHMARK = BENCHMARK;
        printBenchmarkName();
    }

    /**
     * performs all benchmarks
     */
    public abstract void benchmark();

    /**
     * Prints the name of the benchmark being performed
     */
    private void printBenchmarkName() {
        System.out.println("Performing :: " + BENCHMARK);
    }

    /**
     * reads the motif weight matrix from a file
     * @param predicted, which file to read and field members to set
     */
    private void readMotifFile(boolean predicted) throws FileNotFoundException {
        String fileName = predicted ? "/predictedmotif.txt" : "/motif.txt";

        MotifMatrix motifMatrix;
        List<List<Integer>> countsLists = new ArrayList<>();

        Scanner scanner = new Scanner(new FileReader(outputDirectory + fileName));
        scanner.nextLine();
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.startsWith("<"))
                break;
            String[] split = line.split(" ");
            List<Integer> counts = Arrays.stream(split)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            countsLists.add(counts);
        }
        scanner.close();

        motifMatrix = new MotifMatrix(countsLists);

        if(predicted) {
            this.predictedMotifMatrix = motifMatrix;
        } else {
            this.motifMatrix = motifMatrix;
        }
    }

    /**
     * Reads in the sites file
     * @param predicted, which file to read and field members to set
     */
    private void readSitesFile(boolean predicted) throws FileNotFoundException {
        String fileName = predicted ? "/predictedsites.txt" : "/sites.txt";

        List<String> motifs = new ArrayList<>();
        List<Integer> sites = new ArrayList<>();
        Scanner scanner = new Scanner(new FileReader(outputDirectory + fileName));
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] split = line.split(" ");
            motifs.add(split[0]);
            sites.add(Integer.valueOf(split[1]));
        }
        scanner.close();

        if(predicted) {
            this.predictedMotifs = motifs;
            this.predictedSites = sites;
        } else {
            this.motifs = motifs;
            this.sites = sites;
        }
    }

    /**
     * Reads the length of the motif from a file
     * @throws FileNotFoundException, must have motiflength.txt file
     */
    private void readMotifLengthFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader(outputDirectory + "/motiflength.txt"));
        motifLength = Integer.parseInt(scanner.nextLine());
        scanner.close();
    }

    public List<String> getMotifs() {
        return motifs;
    }

    public List<String> getPredictedMotifs() {
        return predictedMotifs;
    }

    public List<Integer> getSites() {
        return sites;
    }

    public List<Integer> getPredictedSites() {
        return predictedSites;
    }

    public MotifMatrix getMotifMatrix() {
        return motifMatrix;
    }

    public MotifMatrix getPredictedMotifMatrix() {
        return predictedMotifMatrix;
    }

    public int getMotifLength() {
        return motifLength;
    }
}
