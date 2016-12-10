%% Read the benchmark outputs in out/data/*
outDir = 'out/data';
folders = dir(outDir);

overlap = 'overlapbenchmark';
entropy = 'relativeentropybenchmark';

for folder = folders'
    disp(folder.name)
    files = dir([outDir '/'  folder.name]);
    for file = files'
        if strcmp(file.name,overlap)
            %read it
            disp(file.name)
        elseif strcmp(file.name,entropy)
            %read it
            disp(file.name)
        end
    end
end