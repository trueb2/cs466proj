package edu.illinois;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * Created by jwtrueb on 11/18/16.
 */
public class MotifFinder {
    // pseudo code? 
    // Load all sequences from *.fa
    // Load length of Motif from size.txt
    // randomly choose motif sites for each sequences
    // recursively do the gibbs sampling
        // randomly choose one sequence chosSq
            // recursively go through every subseq in chosSq
                // calculate Q
                    // 1. gen PWM all choosen sites except chosSq
                    // 2. get Q
                // calculate P
                    // 1. Use background PWM
                    // 2. get P
                // Q/P is largest then record it

            // replace the choosen motif sites of chosSq by the largest one
    // print out all sites recorded for each sequence
    // print out motif weighted array by choosen sites


    // Load all sequences from *.fa
    public List<String> readFAFile(String path){
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            String in = br.readLine();
            StringBuffer sb = new StringBuffer();
            List<String> out = new ArrayList<String>();
            if(in.charAt(0) == '>'){
                    in = br.readLine();
            }
            while(in != null){
                if(in.charAt(0) == '>'){
                    out.add( sb.toString() );
                    sb = new StringBuffer();
                    in = br.readLine();
                }else{
                    sb.append(in);
                    in = br.readLine();
                }
            }
            out.add( sb.toString() );
            return out;
        }catch(IOException e) {
            System.out.println("No Input Sequence");
            return new ArrayList<String>();
        }
    }
    // Load length of Motif from size.txt
    public int readSizeFile(String path){
        // TODO
        // format? test?
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            String in = br.readLine();
            return Integer.valueOf(in);
        }catch(IOException e) {
            System.out.println("No Input Size");
            return 0;
        }
    }
    // randomly choose motif sites for each sequences
    public List<Integer> chooseMotifSites(List<String> seq, int motifSize){
        List<Integer> motifSites = new ArrayList<Integer>();
        Random rand = new Random(System.currentTimeMillis()); 
        int ran;
        for(String motif:seq){
            if(motif.length() >= motifSize ){
                ran = rand.nextInt();
                ran = (ran > 0)? ran%(motif.length() - motifSize + 1):(-ran)%(motif.length() - motifSize + 1);
                motifSites.add(ran);
            }else{
                motifSites.add(-1);
            }
        }
        return motifSites;
    }
    // recursively do the gibbs sampling
        // randomly choose one sequence chosSq
            // recursively go through every subseq in chosSq
                // calculate Q
                    // 1. gen PWM all choosen sites except chosSq
                    // 2. get Q
                // calculate P
                    // 1. Use background PWM
                    // 2. get P
                // Q/P is largest then record it

            // replace the choosen motif sites of chosSq by the largest one
    // print out all sites recorded for each sequence
    // print out motif weighted array by choosen sites
}
