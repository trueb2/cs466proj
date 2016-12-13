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
    outDir = 'out/data';
    folders = dir(outDir);
    folders = folders(cellfun('length', {folders.name})>3);
    
    % Read the benchmarks in each folder
    arrayfun(@(f) readFiles(outDir,f), folders)
end

%% Helper Functions
function readOverlap(fileName)
    global overlapResults
    fid = fopen(fileName);
    overlaps = textscan(fid, '%d\n%d');
    overlapResults = [overlapResults; overlaps{1}];
    fclose(fid);
end

function readEntropy(fileName)
    global entropyResults
    fid = fopen(fileName);
    entropies = textscan(fid, '%s');
    entropies = entropies{1};
    totalEntropy = str2double(entropies{length(entropies)});
    entropyResults = [entropyResults; totalEntropy];
    fclose(fid);
end

function readPredicted(fileName)
    global predIcResults
    fid = fopen(fileName);
    predMotif = textscan(fid, '%s');
    firstLine = predMotif{1}{1};
    predIc = strread(firstLine, '%s', 'delimiter', '_');
    predIcResults = [predIcResults; str2double(predIc{6})];
    fclose(fid);
end

function readFiles(outDir, folder)
    global overlap
    global entropy
    global predicted
    global actIcResults
    
    filePath = [outDir '/' folder.name];
    files = dir(filePath);
    if(length(files) ~= 10)
        disp(folder.name)
    else
        [actualIc, ~] = strread(folder.name, 'seq_%f%s');
        actIcResults = [actIcResults; actualIc];
    end

    for file = dir(filePath)'
        if strcmp(file.name, overlap)
            readOverlap([filePath '/' overlap]);
        elseif strcmp(file.name, entropy)           
            readEntropy([filePath '/' entropy]);
        elseif strcmp(file.name, predicted)
            readPredicted([filePath '/' predicted]);
        end
    end 
end