function plot_results
    %% Initialize variables for reading benchmark outputs
    global overlap
    global entropy
    global predicted
    global overlapResults
    global entropyResults
    global predIcResults
    global actIcResults
    
    % Benchmark file names
    overlap = 'overlapbenchmark';
    entropy = 'relativeentropybenchmark';
    predicted = 'predictedmotif.txt';

    % Number of predicted sites that overlap with actual sites
    overlapResults = [];
    % Relative Entropy of the predicted motif to the actual motif
    entropyResults = [];
    % Information Content per Column of the predicted sites
    predIcResults = [];
    % information Content per Column of the actual sites
    actIcResults = [];

    %% Read the benchmark outputs in out/data/*
    % Get a list of all the output folders
    outDir = '../out/data';
    folders = dir(outDir);
    folders = folders(cellfun('length', {folders.name})>3);
    
    % Read the benchmarks in each folder
    arrayfun(@(f) read_files(outDir,f), folders)
end