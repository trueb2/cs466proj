package edu.illinois.finders;
import edu.illinois.Matrix.SequenceMatrix;
import edu.illinois.Matrix.WeightMatrix;
import edu.illinois.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by jwtrueb on 11/18/16.
 */
public class GibbsSampler extends MotifFinder {

    private List<Integer> sites;

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

    public void find(int maxIterations) {
        System.out.println("============= Input Sequences =============");
        sequences.stream().forEach(s -> System.out.println(s));
        System.out.println("============= Result of Gibbs Sampling Algorithm in each iteration =============");
        gibbsSample(new Random(), maxIterations);
    }

    public void find() {
        find(100);
    }

    /**
     * Implements the Gibbs Sampling algorithm found in the lawrence93.pdf
     * @param maxIterations, maximum number of iterations sampling may take
     * @return Sets of int predicting the position motifs located in each sequence
     */
    public List<Integer> gibbsSample(Random r, int maxIterations) {
        sites = getRandomSites(r);
        int i = 0;
        while (i < maxIterations) {
            // Choose the next sequence
            int idx = r.nextInt(sequenceLength);
            String z = sequences.get(idx);

            // Remove the sequence from the sequences and sites
            sequences.remove(idx);
            sites.remove(idx);

            // Run the predictive step on z
            SequenceMatrix q_ij = predictiveUpdateStep(z);

            // Run the sampling step on q_ij
            int a_z = samplingStep(q_ij, z);

            // Add z back into the set of sequences and sites
            sequences.add(z);
            sites.add(a_z);
        }
        return sites;
    }

    /**
     * One of the sequenceLength sequences, z,
     * is chosen either at random
     * The pattern description q_{i,j} frequency is
     * then calculated from the current positions a_k
     * in all sequences excluding z
     * @param z, sequence chosen for step
     */
    private SequenceMatrix predictiveUpdateStep(String z) {
        // Compute q_{i,j} from the current positions a_k
        List<String> a = IntStream.range(0,sequenceCount-1).mapToObj(i -> {
           int site = sites.get(i);
           String sequence = sequences.get(i);
           return sequence.substring(site, site + motifLength);
        }).collect(Collectors.toList());
        SequenceMatrix q_ij = new SequenceMatrix(a);
        return q_ij;
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
        List<Double> Q = IntStream.range(0,sequenceLength)
                .parallel()
                .mapToObj(x -> calculateMotifProbability(q_ij, z, x))
                .collect(Collectors.toList());
        Double maxQ_x = Q.stream().reduce(Double.MIN_VALUE, Math::max);
        double a_x =  Q.stream().reduce(0.0, (a,b) -> a == maxQ_x ? a : b);
        return (int) a_x;
    }

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







//    /**
//     * Calculate motif sites by iteratively running gibbs sampling for @itrTimes Times
//     * And find the motif set with the highest Information content and return the sites
//     * @param itrTimes iteration times for gibbs sampling
//     * @param outputPath Path for output files "predictedmotif.txt" "predictedsites.txt"
//     * @return Set of motif sites
//     */
//    public List<Integer> gibbsSamplingfinder(int itrTimes,String outputPath) {
//        final List<Integer>[] bestSites = new List[]{new ArrayList<>()};
//        final List<String>[] bestMotifs = new List[]{new ArrayList<>()};
//        final double[] bestIC = {Double.MIN_VALUE};
//        final double[] tempIC = new double[1];
//
//        // Run 10 times and Find the best Information Content
//        IntStream.range(0,itrTimes).parallel().forEach(t -> {
//            List<Integer> predictedSites = gibbsSamp(sequences, motifLength, 10000);
//            System.out.println(predictedSites);
//            ArrayList<String> predictedMotif = new ArrayList<>();
//            for(int sqIndex = 0; sqIndex < predictedSites.size(); sqIndex++){
//                predictedMotif.add( sequences.get(sqIndex).substring( predictedSites.get(sqIndex),predictedSites.get(sqIndex)+ motifLength ) );
//            }
//            tempIC[0] = icPredictedMotif(predictedMotif);
//            if(tempIC[0] > bestIC[0]){
//                bestIC[0] = tempIC[0];
//                bestMotifs[0] = predictedMotif;
//                bestSites[0] = predictedSites;
//            }
//        });
//
//        System.out.println("============= Result of Gibbs Sampling Algorithm with highest Information Content =============");
//        System.out.println(bestSites[0]);
//        //Print files
//        // Collect and Print Data
//        printFiles(bestSites[0], bestMotifs[0],outputPath);
//
//        return bestSites[0];
//    }
