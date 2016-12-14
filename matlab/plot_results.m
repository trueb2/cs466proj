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
    
    %% Do some cool graphing
    icpc = [runs.icpc]';
    ml = [runs.ml]';
    sc = [runs.sc]';
    o = [runs.overlap]';
    e = [runs.entropy]';
    p = [runs.pred_icpc]';
    X = [icpc, ml, sc];
    Y = [o, e, p];
    [~, axes] = plotmatrix(X,Y);
    xlabel(axes(3), 'Actual ICPC');
    xlabel(axes(6), 'Actual ML');
    xlabel(axes(9), 'Actual SC');
    ylabel(axes(1), 'Predicted Overlaps');
    ylabel(axes(2), 'Total Relative Entropy');
    ylabel(axes(3), 'Predicted ICPC');
    title('Parameters versus Benchmark Results of Gibbs Sampling Motif Finder')
end