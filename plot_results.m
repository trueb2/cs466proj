%% Read the benchmark outputs in out/data/*
outDir = 'out/data';
folders = dir(outDir);

overlap = 'overlapbenchmark';
entropy = 'relativeentropybenchmark';
predictedIC = 'predictedmotif.txt';

for folder = folders'
    disp(folder.name)
    files = dir([outDir '/'  folder.name]);
    for file = files'
        if strcmp(file.name,overlap)
            %read overlapbenchmark
            disp(file.name)
        elseif strcmp(file.name,entropy)
            %read relativeentropybenchmark
            disp(file.name)
        elseif strcmp(file.name,predictedIC)
            %read predictedmotif.txt
            disp(file.name)
        end
    end
end