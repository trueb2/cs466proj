function plot_results
    %% Initialize variables for reading benchmark outputs
    global overlap entropy predicted runs;
    
    % Benchmark file names
    overlap = 'overlapbenchmark';
    entropy = 'relativeentropybenchmark';
    predicted = 'predictedmotif.txt';

    runs = struct('icpc', {}, 'ml', {}, 'sl', {}, 'sc', {}, ...
        'overlap', {}, 'entropy', {}, 'pred_icpc', {});

    %% Read the benchmark outputs in out/data/*
    % Get a list of all the output folders
    outDir = '../out/data';
    folders = dir(outDir);
    folders = folders(cellfun('length', {folders.name})>3);
    
    % Read the benchmarks in each folder
    arrayfun(@(f) read_files(outDir,f), folders)
end