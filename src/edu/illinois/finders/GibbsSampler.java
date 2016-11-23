package edu.illinois.finders;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.stream.IntStream.range;

/**
 * Created by jwtrueb on 11/18/16.
 */
public class GibbsSampler extends MotifFinder {
/**   pseudo code
*     Load all sequences from *.fa
*     Load length of Motif from size.txt
*     randomly choose motif sites (theta set) for each sequences
*     recursively do the gibbs sampling
*         randomly choose one sequence as chosen Seq
*             recursively go through every possible motif position in chosen Seq
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
    public GibbsSampler(String faPath, String sizePath, String outputPath) {
        //Read Files
        super(faPath, sizePath);

        // Run Gibbs Sample Algorithm to predict sites
        List<Integer> predictedSites = gibbsSamplingfinder(15, outputPath);
    }
    public GibbsSampler(){

        }
    /**
     * Method implemented by all MotifFinders that
     * will perform the search for the motifs in the sequences
     */
    public void find() {
        System.out.println("============= Input Sequences =============");
        System.out.println(sequences);
        System.out.println("============= Result of Gibbs Sampling Algorithm in each iteration =============");
        // range(0, 15).forEach(i -> System.out.println(gibbsSamp(sequences, motifLength, 10000)));
        // Run Gibbs Sample Algorithm to predict sites
        int itrTimes = 10;
        List<Integer> bestSites = gibbsSamplingfinder(itrTimes,"./");
    }
    /**
     * Calculate motif sites by iteratively running gibbs sampling for @itrTimes Times
     * And find the motif set with the highest Information content and return the sites
     * @param itrTimes iteration times for gibbs sampling
     * @param outputPath Path for output files "predictedmotif.txt" "predictedsites.txt"
     * @return Set of motif sites
     */
    public List<Integer> gibbsSamplingfinder(int itrTimes,String outputPath) {
        List<List<String>> predictedMotifSet = new ArrayList<>();
        List<String> predictedMotif;
        List<Integer> predictedSites;
        List<Integer> bestSites = new ArrayList<>();
        List<String> bestMotifs = new ArrayList<>();
        double bestIC = Double.MIN_VALUE;
        double tempIC;
        // Run 10 times and Find the best Information Content
        for(int times = 0; times < itrTimes; times++){
            predictedSites = gibbsSamp(sequences, motifLength, 10000);
            System.out.println(predictedSites);
            predictedMotif = new ArrayList<>();
            for(int sqIndex = 0; sqIndex < predictedSites.size(); sqIndex++){
                predictedMotif.add( sequences.get(sqIndex).substring( predictedSites.get(sqIndex),predictedSites.get(sqIndex)+ motifLength ) );
            }
            tempIC = icPredictedMotif(predictedMotif);
            if(tempIC > bestIC){
                bestIC = tempIC;
                bestMotifs = predictedMotif;
                bestSites = predictedSites;
            }
        }
        System.out.println("============= Result of Gibbs Sampling Algorithm with highest Information Content =============");
        System.out.println(bestSites);
        //Print files
        // Collect and Print Data
        printFiles(bestSites, bestMotifs,outputPath);

        return bestSites;
    }
    /**
     * Calculate motif sites by iteratively running gibbs sampling for @itrTimes Times
     * And find the motif set with the highest Information content and return the sites
     * @param bestSites best sites set found by gibbs sampling
     * @param bestMotifs best motif string set found by gibbs sampling
     * @param outputPath Path for output files "predictedmotif.txt" "predictedsites.txt"
     */
    private void printFiles(List<Integer> bestSites, List<String> bestMotifs,String outputPath) {
        try {
            List<List<Integer>> pm = getPM(bestMotifs);
            PrintWriter writer = new PrintWriter(outputPath+"predictedmotif.txt", "UTF-8");
            writer.print(">MOTIF1\t" + motifLength);
            for (int i = 0; i < pm.size(); i++) {
                writer.print("\n");
                for (int sup : pm.get(i)) {
                    writer.print(sup + "\t");
                }
            }
            writer.print("<");
            writer.close();
            writer = new PrintWriter(outputPath+"predictedsites.txt", "UTF-8");
            writer.println(bestSites);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
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
                ic += pwm.get(pos).get(b)*( Math.log( pwm.get(pos).get(b) ) - Math.log( 0.25 ) );
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
     * @param seqSet Sets of Gene Sequence String
     * @param motifSize motif size
     * @param recTimes iteration times
     * @return Sets of int predicting the position motifs located in each sequence
     */
    public List<Integer> gibbsSamp(List<String> seqSet, int motifSize, int recTimes){
        // randomly choose motif sites for each sequences
        List<Integer> sites = chooseMotifSites(seqSet, motifSize);
        // recursively do the gibbs sampling
        int i = recTimes;
        int chosenSeq;
        double maxQoverP;
        Random rand = new Random(System.currentTimeMillis());
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
                double p = Math.log(0.25) * motifSize;
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

    // print out all sites recorded for each sequence
    // print out motif weighted array by choosen sites
}
