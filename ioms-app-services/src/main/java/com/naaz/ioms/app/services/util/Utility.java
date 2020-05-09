package com.naaz.ioms.app.services.util;

import com.naaz.ioms.data.access.entities.Files;

import java.sql.Timestamp;

public interface Utility {
    static Files createFilesFromFileInfo(FileInfo fileInfo) {
        final Files files = new Files();
        files.setFileName(fileInfo.getFileName());
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        files.setCreatedOn(timestamp);
        files.setModifiedOn(timestamp);
        files.setId(fileInfo.getFileId());
        files.setFileData(fileInfo.getFileData().getBytes());
        return files;
    }
}
