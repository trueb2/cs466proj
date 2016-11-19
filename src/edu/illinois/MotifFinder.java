package edu.illinois;

/**
 * Created by jwtrueb on 11/18/16.
 */
public class MotifFinder {
    // TODO
    // Load all sequences from *.fa
    // Load length of Motif from size.txt
    // randomly choose motif sites for each sequences
    // recursive doing gibbs sampling
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
