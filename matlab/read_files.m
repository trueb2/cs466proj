function read_files(outDir, folder)
    global overlap
    global entropy
    global predicted
    global runs
    
    filePath = [outDir '/' folder.name];
    files = dir(filePath);
    if(length(files) ~= 10)
        disp(folder.name)
        return
    end
    
    [icpc, ml, sl, sc] = strread(folder.name, 'seq_%f_%d_%d_%d');
    
    for file = dir(filePath)'
        if strcmp(file.name, overlap)
            o = readOverlap([filePath '/' overlap]);
        elseif strcmp(file.name, entropy)           
            e = readEntropy([filePath '/' entropy]);
        elseif strcmp(file.name, predicted)
            p = readPredicted([filePath '/' predicted]);
        end
    end
    runs(end+1) = struct('icpc', { icpc }, 'ml', { ml }, 'sl', { sl }, ...
        'sc', { sc }, 'overlap', { o }, 'entropy', { e }, 'pred_icpc', { p });
end

function o = readOverlap(fileName)
    fid = fopen(fileName);
    overlaps = textscan(fid, '%d\n%d');
    fclose(fid);
    o = overlaps{1};
end

function e = readEntropy(fileName)
    fid = fopen(fileName);
    entropies = textscan(fid, '%s');
    fclose(fid);
    entropies = entropies{1};
    e = str2double(entropies{length(entropies)});
end

function p = readPredicted(fileName)
    fid = fopen(fileName);
    predMotif = textscan(fid, '%s');
    fclose(fid);
    firstLine = predMotif{1}{1};
    predIc = strread(firstLine, '%s', 'delimiter', '_');
    p = str2double(predIc{6});
end