package edu.illinois.finders;
import edu.illinois.Matrix.SequenceMatrix;
import edu.illinois.Matrix.WeightMatrix;
import edu.illinois.Utils;
import edu.illinois.Writer;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by jwtrueb on 11/18/16.
 */
public class GibbsSampler extends MotifFinder {

    public static final double LOG_25 = Math.log(.25);

    /**   pseudo code
*     Load all sequences from *.fa
*     Load length of Motif from size.txt
*     randomly choose motif sites (theta set) for each sequences
*     recursively do the gibbs sampling
*         randomly choose one sequence as chosen Seq
*             iteratively go through every possible motif position in chosen Seq
*                 calculate Q
*                     1. gen PM and PWM of theta set except chosen Seq
*                     2. get Q probability based on PWM of theta set
*                 calculate P
*                     1. Log 0.25 * motif_length
*                 Q/P is the largest then record the position to the chosen motif sites
*     return the motif sites set
*
*     ? print out all sites recorded for each sequence
*     ? print out motif weighted array by choosen sites
*/
    public GibbsSampler(String faPath, String sizePath, String outputDirectory) {
        //Read Files
        super(faPath, sizePath, outputDirectory);

    }

    public void find(int maxIterations, Random r) {
        System.out.println("============= Input Sequences =============");
        sequences.stream().forEach(s -> System.out.println(s));
        System.out.println("============= Result of Gibbs Sampling Algorithm in each iteration =============");
        List<Integer> predictedSites = new ArrayList<>();
        List<String> predictedMotifs = new ArrayList<>();
        final double[] maxInformationContent = {Double.NEGATIVE_INFINITY};
        IntStream.range(0,10).forEach(j -> {
            List<Integer> sites = gibbsSample(r, maxIterations, new ArrayList<>(sequences));
            String s = sites.stream()
                    .map(i -> i.toString())
                    .collect(Collectors.joining(" "));
            List<String> motifs = getMotifStrings(sequences, sites);
            double informationContent = informationContent(motifs);
            if(informationContent >= maxInformationContent[0]) {
                maxInformationContent[0] = informationContent;
                predictedSites.clear();
                predictedSites.addAll(sites);
                predictedMotifs.clear();
                predictedMotifs.addAll(motifs);
            }
            System.out.println(informationContent + " :: " + s);
        });

        System.out.println("============= End =============");

        try {
            Writer.writeSites(sequenceCount, predictedMotifs, predictedSites, outputDirectory + "/predictedsites.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private double informationContent(List<String> motifs) {
        SequenceMatrix sm = new SequenceMatrix(motifs);
        Double informationContent = IntStream.range(0,motifLength)
                .mapToDouble(i ->  IntStream.range(0, 4)
                            .mapToDouble(j -> sm.probability(i, j) * (Math.log(sm.probability(i, j)) - LOG_25))
                            .filter(d -> !Double.isNaN(d))
                            .sum())
                .sum();
        return informationContent;
    }

    public void find() {
        find(100000, new Random());
    }

    /**
     * Implements the Gibbs Sampling algorithm found in the lawrence93.pdf
     * @param maxIterations, maximum number of iterations sampling may take
     * @return Sets of int predicting the position motifs located in each sequence
     */
    public List<Integer> gibbsSample(Random r, int maxIterations, List<String> S) {
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

            // Run the sampling step on q_ij
            int a_z = samplingStep(q_ij, z);

            // Add z back into the set of sequences and sites
            S.add(idx, z);
            A.add(idx, a_z);
        }
        return A;
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
        SequenceMatrix q_ij = new SequenceMatrix(a);
        return q_ij;
    }

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
    private int samplingStep(SequenceMatrix q_ij, String z) {
        List<Double> Q = IntStream.range(0,sequenceLength-motifLength)
                .parallel()
                .mapToObj(x -> calculateMotifProbability(q_ij, z, x))
                .collect(Collectors.toList());
        List<Double> weightDistribution = smoothProbabilities(Q);
        Double choice = weightedChooseIndex(weightDistribution);
        int a_x = IntStream.range(0,sequenceLength-motifLength)
                .reduce(0, (a,b) -> Q.get(a).equals(choice) ? a : b);
        return a_x;
    }

    /**
     * Currently picks the one with the greatest probability but should be
     * picking randomly from a weighted distribution
     * @param weightDistribution
     * @return new index of the site
     */
    private Double weightedChooseIndex(List<Double> weightDistribution) {
        return weightDistribution.stream()
                    .reduce(Double.NEGATIVE_INFINITY, (a, b) -> a > b ? a : b);
    }

    /**
     * Takes Q a list of log probabilities
     * Replaces negative infinities with 1 less than the minimum log probability
     * @param Q, log probabilities
     * @return list of smoothed probabilities
     */
    private List<Double> smoothProbabilities(List<Double> Q) {
        // Find the smallest probability greater than 0
        BinaryOperator<Double> minExceptInfinity = (a, b) -> a < b && !b.equals(Double.NEGATIVE_INFINITY) ? b : a;
        Double min = Q.stream().reduce(Double.NEGATIVE_INFINITY, minExceptInfinity);

        // Assert that there is some non zero probability so that we may smooth
        if(min <= Double.NEGATIVE_INFINITY + 1)
            return Q.stream().map(a -> -100.0).collect(Collectors.toList());
        else
        // Replace the 0 probability indices with (min - 1) log probability
            return Q.stream()
                .map(a -> a.equals(Double.NEGATIVE_INFINITY) ? min - 1: a)
                .collect(Collectors.toList());
    }

    /**
     * calculates the log probability of a character appearing at a specific index in a motif
     * @param q_ij, motif weight matrix
     * @param z, string of characters
     * @param x, index of site in z
     * @return log probability
     */
    private Double calculateMotifProbability(SequenceMatrix q_ij, String z, int x) {
        return IntStream.range(0,motifLength)
                .mapToObj(i -> q_ij.probability(i, Utils.indexOfBase(z.charAt(x + i))))
                .map(p -> Math.log(p))
                .reduce(0.0, (a, b) -> a + b);
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
