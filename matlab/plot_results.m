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
    outDir = '../all_default_data';
    folders = dir(outDir);
    folders = folders(cellfun('length', {folders.name})>3);
    
    % Read the benchmarks in each folder
    arrayfun(@(f) read_files(outDir,f), folders);
    
    %% Do some cool graphing and stats
    icpc = double([runs.icpc]');
    ml = double([runs.ml]');
    sc = double([runs.sc]');
    o = double([runs.overlap]');
    op = o ./ sc;
    e = double([runs.entropy]');
    p = double([runs.pred_icpc]');
    X = [icpc, ml, sc];
    Y = [o, op, e, p];
    [~, axes] = plotmatrix(X,Y, 'o');
    xlabel(axes(4), 'Actual ICPC');
    xlabel(axes(8), 'Actual ML');
    xlabel(axes(12), 'Actual SC');
    ylabel(axes(1), 'Predicted Overlaps');
    ylabel(axes(2), 'Percent Sites Predicted');
    ylabel(axes(3), 'Total Relative Entropy');
    ylabel(axes(4), 'Predicted ICPC');
    title('Results of Gibbs Sampling Motif Finder over All Default Parameters')
    
    fid = fopen('stats_output','w');
    print_stats(fid, 'icpc', icpc);
    print_stats(fid, 'ml', ml);
    print_stats(fid, 'sc', sc);
    print_stats(fid, 'overlaps', o);
    print_stats(fid, 'percent sites predicted', op);
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
    
    
    
    