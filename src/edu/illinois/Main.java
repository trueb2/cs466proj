package edu.illinois;

public class Main {

    public static void main(String[] args) {
        //information content per column
        double icpc = Double.parseDouble(args[0]);
        //motif length
        int ml = Integer.parseInt(args[1]);
        //sequence length
        int sl = Integer.parseInt(args[2]);
        //sequence count
        int sc = Integer.parseInt(args[3]);

        SequenceGenerator.createAndWrite(icpc, ml, sl, sc,
                "sequences.fa",
                "sites.txt",
                "motif.txt",
                "motiflength.txt");
    }

}
