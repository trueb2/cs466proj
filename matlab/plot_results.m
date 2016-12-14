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
    outDir = '../out/all_data';
    folders = dir(outDir);
    folders = folders(cellfun('length', {folders.name})>3);
    
    % Read the benchmarks in each folder
    arrayfun(@(f) read_files(outDir,f), folders)
    
    %% Do some cool graphing and stats
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
    title('Results of Gibbs Sampling Motif Finder over All Parameters')
    
    fid = fopen('stats_output','w');
    print_stats(fid, 'icpc', icpc);
    print_stats(fid, 'ml', ml);
    print_stats(fid, 'sc', sc);
    print_stats(fid, 'overlaps', o);
    print_stats(fid, 'entropy', e);
    print_stats(fid, 'predicted icpc', p);
    fclose(fid);
end

function print_stats(fid, name, asdf)
    fprintf(fid, 'Stats for %s\n', name);
    fprintf(fid, 'min :: %f\n', min(asdf));
    fprintf(fid, 'max :: %f\n', max(asdf));
    fprintf(fid, 'mean :: %f\n', mean(asdf));
    fprintf(fid, 'mode :: %f\n', mode(asdf));
    fprintf(fid, 'standard deviation :: %f\n\n', std(double(asdf)));
    figure();
    histogram(double(asdf));
    title(name);
    saveas(gcf, [name '_histogram.jpg']);
    close();
end
    
    
    
    