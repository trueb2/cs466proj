package edu.illinois.benchmarks;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Benchmark {
    String outputDirectory;

    public List<String> getMotifs() {
        return motifs;
    }

    public void setMotifs(List<String> motifs) {
        this.motifs = motifs;
    }

    public List<Integer> getSites() {
        return sites;
    }

    public void setSites(List<Integer> sites) {
        this.sites = sites;
    }

    List<String> motifs;
    List<Integer> sites;

    public Benchmark(String outputDirectory) throws FileNotFoundException {
        this.outputDirectory = outputDirectory;

        //read sequence information

        //read predictions
        readSitesFile();
    }

    /**
     * performs all benchmarks
     */
    public abstract void benchmark();

    /**
     * reads in the sites file
     */
    public void readSitesFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader(outputDirectory + "/sites.txt"));
        motifs = new ArrayList<>();
        sites = new ArrayList<>();
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] split = line.split(" ");
            motifs.add(split[0]);
            sites.add(Integer.valueOf(split[1]));
        }
        scanner.close();
    }

}
