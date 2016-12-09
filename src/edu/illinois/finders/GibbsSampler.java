package edu.illinois.finders;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by jwtrueb on 11/18/16.
 */
public class GibbsSampler extends MotifFinder {

    private final double LOG_25 = Math.log(0.25);
    private int maxIterations;

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
        gibbsSample(maxIterations);
    }

    public void find() {
        find(100);
    }

    /**
     * Calculate Information content for input PWM
     * @param predictedMotif Predicted Motif String set
     * @return Sets of Gene Sequence String
     */
    public double icPredictedMotif(List<String> predictedMotif){
        List<List<Integer>> pm = getPM(predictedMotif);
        List<List<Double>> pwm = pm2PWM(pm);
        double ic = 0;
        for(int pos = 0; pos < pwm.size(); pos++){
            for(int b = 0; b < 4; b++){
                ic += pwm.get(pos).get(b)*( Math.log( pwm.get(pos).get(b) ) - LOG_25);
            }
        }
        return ic;
    }
    /**
     * Randomly choose position in each seq to be motif sites
     * @param seqSet Sets of Gene Sequence String
     * @param motifSize motif size
     * @return Set of int indicate position of motif in each seq
     */
    public List<Integer> chooseMotifSites(List<String> seqSet, int motifSize){
        List<Integer> motifSites = new ArrayList<Integer>();
        Random rand = new Random(System.currentTimeMillis()); 
        int ran;
        for(String motif:seqSet){
            if(motif.length() >= motifSize ){
                ran = rand.nextInt( motif.length() - motifSize + 1 );
                motifSites.add(ran);
            }else{
                motifSites.add(-1);
            }
        }
        return motifSites;
    }
    /**
     * Generate probability depending on theta set ( predicted motifs except the chosen seq ) for recSite position in chosen seq
     * @param seqSet Sets of Gene Sequence String
     * @param chosenSeq the chosen seq in which finding best subsequence to match theta set
     * @param sites all start index of predicted motifs in seqSet
     * @param motifSize motif size
     * @return Log of Probability based on theta set
     */
    public double genLogProbbyT( List<String> seqSet ,int chosenSeq,int recSite ,List<Integer> sites, int motifSize){
        List<String> thetaSet = new ArrayList<>();
        for(int i = 0 ; i < seqSet.size(); i++){
            if(i != chosenSeq){
                thetaSet.add( seqSet.get(i).substring(sites.get(i),sites.get(i) + motifSize ) );
            }
        }
        List<List<Integer>> pm = getPM( thetaSet );
        List<List<Double>> pwm = pm2PWM(pm);
        double prob = 0;
        for(int pos = 0; pos < motifSize; pos++){
            if ( seqSet.get(chosenSeq).charAt( recSite + pos ) == 'A' ){
                prob += Math.log( pwm.get(pos).get(0) );
            }else if ( seqSet.get(chosenSeq).charAt( recSite + pos ) == 'C' ){
                prob += Math.log( pwm.get(pos).get(1));
            }else if ( seqSet.get(chosenSeq).charAt( recSite + pos ) == 'T' ){
                prob += Math.log( pwm.get(pos).get(2));
            }else if ( seqSet.get(chosenSeq).charAt( recSite + pos ) == 'G' ){
                prob += Math.log( pwm.get(pos).get(3));
            }
        }
        return prob;
    }
    /**
     * Generate Probability Weighted Matrix based on Profile Matrix
     * If sup = 0, assign it a probability < 0.001 to avoid infinity problem
     * @param pm Profile Matrix
     * @return PWM list which is a ( List of position to (List of ACTG to Probability) )
     */
    public List<List<Double>> pm2PWM(List<List<Integer>> pm) {
        List<Integer> char2Sup;
        List<List<Double>> pwm = new ArrayList<>(pm.size());
        for(int i = 0; i <pm.size();i++ ) pwm.add(new ArrayList());
        List<Double> prob; // index => 0:A 1:C 2:T 3:G
        // Profile Matrix to PWM
        int totalSup = pm.get(0).get(0) +pm.get(0).get(1) +pm.get(0).get(2) +pm.get(0).get(3);
        for (int pos = 0; pos < pm.size(); pos++) {
            char2Sup = pm.get(pos);
            prob = new ArrayList<>(4);
            for(int i = 0; i <4;i++ ) prob.add(0.0);
            if(char2Sup.contains(0)){
                int num_zero = 0;
                for(int sup =0 ; sup < 4; sup++){
                    if (char2Sup.get(sup) == 0){
                        num_zero ++;
                    }
                }
                for(int sup =0 ; sup < 4; sup++){
                    if (char2Sup.get(sup) == 0){
                        if(num_zero == 1){
                            prob.set(sup, ( 3.0/totalSup/1000 ));
                        }else if(num_zero == 2){
                            prob.set(sup, ( 1.0/totalSup/1000 ));
                        }else{
                            prob.set(sup, ( 1.0/totalSup/1000 ));
                        }
                    }else{
                        if(num_zero == 1){
                            prob.set(sup, ( (double)(char2Sup.get(sup) * 1000 - 1)/totalSup/1000));
                        }else if(num_zero == 2){
                            prob.set(sup, ( (double)(char2Sup.get(sup) * 1000 - 1)/totalSup/1000));
                        }else{
                            prob.set(sup, ( (double)(char2Sup.get(sup) * 1000 - 3)/totalSup/1000));
                        }
                    }
                }
            }else{
                for(int charNum = 0; charNum < 4; charNum++){
                    prob.set(charNum, ( (double)char2Sup.get(charNum) /totalSup ) );
                }
            }
            pwm.set(pos,prob);
        }
        return pwm;
    }

    /**
     * Generate Profile Matrix according to theta set for each position
     * @param seqIn  predicted motifs String set (not index Set)
     * @return Profile Matrix list ( List of position to (List of ACTG to Sup) )
     */
    public List<List<Integer>> getPM(List<String> seqIn) {
        List<List<Integer>> pos2CharNSup = new ArrayList<>( seqIn.get(0).length() );
        for(int i = 0; i <seqIn.get(0).length();i++ ) pos2CharNSup.add(new ArrayList());
        List<Integer> char2Sup; // index => 0:A 1:C 2:T 3:G
        // accu ATCG Info by position of motif ( Profile Matrix )
        for (int pos = 0; pos < seqIn.get(0).length(); pos++) {
            char2Sup = new ArrayList();
            for(int i = 0 ; i < 4 ; i ++) char2Sup.add(0);
            for (int seq = 0; seq < seqIn.size(); seq++) {
                if(seqIn.get(seq).charAt(  pos ) == 'A'){
                    char2Sup.set(0, char2Sup.get(0)+1 );
                }else if(seqIn.get(seq).charAt( pos ) == 'C'){
                    char2Sup.set(1, char2Sup.get(1)+1 );
                }else if(seqIn.get(seq).charAt( pos ) == 'T'){
                    char2Sup.set(2, char2Sup.get(2)+1 );
                }else if(seqIn.get(seq).charAt( pos ) == 'G'){
                    char2Sup.set(3, char2Sup.get(3)+1 );
                }
            }
            pos2CharNSup.set(pos,char2Sup);
        }
        return pos2CharNSup;
    }
    /**
     * gibbs sampling, recursively choose a seq in seqSet and find best subSeq to match theta set all
     * @param maxIterations, maximum number of iterations sampling may take
     * @return Sets of int predicting the position motifs located in each sequence
     */
    public List<Integer> gibbsSample(int maxIterations){
        // randomly choose motif sites for each sequences
        List<Integer> sites = getRandomSites();
        int chosenSeq;
        double maxQoverP;
        while(i > 0) {
            // randomly choose one sequence chosenSeq
            chosenSeq = rand.nextInt(seqSet.size());
            // recursively go through every subseq in chosenSeq and find max Q/P for each subSeq
            maxQoverP =  Double.MIN_VALUE;
            for ( int recSite = 0; recSite <= seqSet.get(0).length()- motifSize; recSite++ ) {
                // calculate Q
                // 1. gen PWM all choosen sites except chosSq
                // 2. get Log Q
                double q = genLogProbbyT( seqSet , chosenSeq, recSite , sites,  motifSize);
                // calculate P
                // Use background PWM => get Log P
                double p = LOG_25 * motifSize;
                // Log Q - Log P (Q/P) is largest then record it
                // replace the choosen motif sites of chosSq by the largest one
                if( q - p > maxQoverP){
                    sites.set(chosenSeq, recSite );
                    maxQoverP = q - p;
                }
            }
            i--;
        }
        return sites;
    }

    private List<Integer> getRandomSites() {
        Random r = new Random();
        return IntStream
                .range(0,sequenceCount)
                .mapToObj(i -> r.nextInt(sequenceLength-motifLength))
                .collect(Collectors.toList());
    }

    // print out all sites recorded for each sequence
    // print out motif weighted array by choosen sites
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
