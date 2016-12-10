package edu.illinois.finders;

import edu.illinois.Matrix.SequenceMatrix;
import edu.illinois.Matrix.WeightMatrix;
import edu.illinois.Utils;
import edu.illinois.Writer;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GibbsSampler extends MotifFinder {

    private static final double LOG_2 = Math.log(2);

    public GibbsSampler(String fastaFilePath, String motifLengthPath, String outputDirectory) {
        super(fastaFilePath, motifLengthPath, outputDirectory);
    }

    /**
     * Runs numSamples gibbsSamples to find a prediction on the sites
     * and motifs with the highest information content in the sequences
     * @param maxIterations, maximum number of times to iterate in a Gibbs Sample
     * @param numSamples, number of times to Gibbs Sample
     * @param r, object to provide randomness
     */
    public void find(int maxIterations, int numSamples, Random r) {
        System.out.println("============= Input Sequences =============");
        sequences.forEach(System.out::println);
        System.out.println("============= Result of Gibbs Sampling Algorithm in each iteration =============");
        List<Integer> predictedSites = new ArrayList<>();
        List<String> predictedMotifs = new ArrayList<>();
        final double[] maxInformationContent = {Double.NEGATIVE_INFINITY};
        IntStream.range(0,numSamples).parallel().forEach(j -> {
            List<Integer> sites = gibbsSample(r, maxIterations, new ArrayList<>(sequences));
            String s = sites.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(" "));
            List<String> motifs = getMotifStrings(sequences, sites);
            double informationContent = informationContent(motifs);
            boolean newMax;
            synchronized (maxInformationContent) {
                newMax = informationContent >= maxInformationContent[0];
            }
            if(newMax) {
                synchronized (maxInformationContent) {
                    maxInformationContent[0] = informationContent;
                }
                synchronized (predictedSites) {
                    predictedSites.clear();
                    predictedSites.addAll(sites);
                }
                synchronized (predictedMotifs) {
                    predictedMotifs.clear();
                    predictedMotifs.addAll(motifs);
                }
            }
            System.out.println(informationContent + " :: " + s);
        });

        WeightMatrix motifMatrix = new SequenceMatrix(predictedMotifs);
        Double icpc = maxInformationContent[0] / motifLength;
        System.out.println("======== Maximum Information Content :: " + maxInformationContent[0] / motifLength +" =========\n");

        try {
            Writer.writeSites(sequenceCount, predictedMotifs, predictedSites, outputDirectory + "predictedsites.txt");
            Writer.writeMotif(sequenceCount, motifMatrix, outputDirectory + "predictedmotif.txt", icpc);
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private double informationContent(List<String> motifs) {
        SequenceMatrix sm = new SequenceMatrix(motifs);
        return IntStream.range(0,motifLength)
                .mapToDouble(i ->  IntStream.range(0, 4)
                            .mapToDouble(j -> sm.probability(i, j) * (Math.log(sm.probability(i, j)*4) / LOG_2))
                            .filter(d -> !Double.isNaN(d))
                            .sum())
                .sum();
    }

    public void find() {
        find(500, sequenceLength, new Random());
    }

    /**
     * Implements the Gibbs Sampling algorithm found in the lawrence93.pdf
     * @param maxIterations, maximum number of iterations sampling may take
     * @return Sets of int predicting the position motifs located in each sequence
     */
    private List<Integer> gibbsSample(Random r, int maxIterations, List<String> S) {
        List<Integer> A = getRandomSites(r);
        int i = 0;
        while (i++ < maxIterations) {
            // Choose the next sequence
            int idx = r.nextInt(sequenceCount);
            String z = S.get(idx);

            // Remove the sequence from the sequences and sites
            S.remove(idx);
            A.remove(idx);

            // Run the predictive step on z
            SequenceMatrix q_ij = predictiveUpdateStep(S, A);
            List<Double> P = calculateP(S);

            // Run the sampling step on q_ij
            int a_z = samplingStep(q_ij, z, P);

            // Add z back into the set of sequences and sites
            S.add(idx, z);
            A.add(idx, a_z);
        }
        return A;
    }

    /**
     * Calculates the background probabilities for each base
     * @param S, sequenceCount sequences
     * @return List of Double length 4
     */
    private List<Double> calculateP(List<String> S) {
        double[] P = { 0, 0, 0, 0 };
        IntStream.range(0,sequenceCount-1)
                .forEach(i -> IntStream.range(0,sequenceLength)
                        .forEach(j -> P[Utils.indexOfBase(S.get(i).charAt(j))]++));

        Double sum = Arrays.stream(P).reduce(0, Double::sum);

        return Arrays.stream(P)
                .mapToObj(d -> d/sum)
                .collect(Collectors.toList());
    }

    /**
     * One of the sequenceLength sequences, z,
     * is chosen either at random
     * The pattern description q_{i,j} frequency is
     * then calculated from the current positions a_k
     * in all sequences excluding z
     * @param S, the sequences other than z
     * @param A, the sites for the sequences other than z
     */
    private SequenceMatrix predictiveUpdateStep(List<String> S, List<Integer> A) {
        // Compute q_{i,j} from the current positions a_k
        List<String> a = getMotifStrings(S, A);
        return new SequenceMatrix(a);
    }

    /**
     * Grabs the motif strings of length motifLength
     * from each sequence and site
     * @param S, sequences
     * @param A, sites
     * @return sequenceCount motif strings
     */
    private List<String> getMotifStrings(List<String> S, List<Integer> A) {
        return IntStream.range(0,S.size()).mapToObj(i -> {
               int site = A.get(i);
               String sequence = S.get(i);
               return sequence.substring(site, site + motifLength);
            }).collect(Collectors.toList());
    }

    /**
     * Every possible segment of width motifLength within sequence z
     * is considered as a possible instance of the pattern. The
     * probabilities Q_x of generating each segment x according to
     * the current pattern probabilities q_{i,j} are calculated
     * The weight A_x = Q_x/P_x is assigned to segment x, and
     * with each segment so weighted, a random one is selected.
     * Its position then becomes the new a_z.
     * @param z, sequence we are iterating through
     */
    private int samplingStep(SequenceMatrix q_ij, String z, List<Double> P) {
        List<Double> A = IntStream.range(0,sequenceLength-motifLength)
                .parallel()
                .mapToObj(x -> calculateMotifProbability(q_ij, z, x, P))
                .collect(Collectors.toList());
        List<Double> weightDistribution = smoothProbabilities(A);
        Double choice = weightedChooseIndex(weightDistribution);
        return IntStream.range(0,sequenceLength-motifLength)
                .reduce(0, (a,b) -> A.get(a).equals(choice) ? a : b);
    }

    /**
     * calculates the log probability of a character appearing at a specific index in a motif
     * @param q_ij, motif weight matrix
     * @param z, string of characters
     * @param x, index of site in z
     * @param P, background frequencies
     * @return log probability
     */
    private Double calculateMotifProbability(SequenceMatrix q_ij, String z, int x, List<Double> P) {
        return IntStream.range(0,motifLength)
                .mapToObj(i -> {
                    int baseIdx = Utils.indexOfBase(z.charAt(x + i));
                    double q = q_ij.probability(i, baseIdx);
                    double p = 1 / P.get(baseIdx);
                    return Math.log(q/p);
                })
                .reduce(0.0, Double::sum);
    }

    /**
     * Takes Q a list of log probabilities
     * Replaces negative infinities with 1 less than the minimum log probability
     * @param A, log probabilities
     * @return list of smoothed probabilities
     */
    private List<Double> smoothProbabilities(List<Double> A) {
        // Find the smallest probability greater than 0
        BinaryOperator<Double> minExceptInfinity = (a, b) -> a < b && !b.equals(Double.NEGATIVE_INFINITY) ? b : a;
        Double min = A.stream().reduce(Double.NEGATIVE_INFINITY, minExceptInfinity);

        // Assert that there is some non zero probability so that we may smooth
        if(min <= Double.NEGATIVE_INFINITY + 1)
            return A.stream().map(a -> -100.0).collect(Collectors.toList());
        else
        // Replace the 0 probability indices with (min - 1) log probability
            return A.stream()
                .map(a -> a.equals(Double.NEGATIVE_INFINITY) ? min - 1: a)
                .collect(Collectors.toList());
    }

    /**
     * Currently picks the one with the greatest probability but should be
     * picking randomly from a weighted distribution
     * @param weightDistribution, sequenceCount smoothed log probabilities
     * @return new index of the site
     */
    private Double weightedChooseIndex(List<Double> weightDistribution) {
        return weightDistribution.stream()
                .reduce(Double.NEGATIVE_INFINITY, (a, b) -> a > b ? a : b);
    }

    /**
     * Creates a list of sequenceLength random numbers
     * using the random object supplied
     * the numbers are from 0 to sequenceLength-motifLength-1 inclusive
     * @param r, random object
     * @return sequenceLength random ints
     */
    private List<Integer> getRandomSites(Random r) {
        return IntStream
                .range(0,sequenceCount)
                .mapToObj(i -> r.nextInt(sequenceLength-motifLength))
                .collect(Collectors.toList());
    }
}
