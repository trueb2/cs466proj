function read_files(outDir, folder)
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