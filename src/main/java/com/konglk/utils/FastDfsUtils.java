package com.konglk.utils;


import com.github.tobato.fastdfs.domain.FileInfo;
import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import com.github.tobato.fastdfs.service.GenerateStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by konglk on 2018/9/1.
 */
@Component
public class FastDfsUtils {


    public static void upload(byte[] buffer) {
        GenerateStorageClient client = new DefaultFastFileStorageClient();
        FileInfo fileInfo = client.queryFileInfo("group1", "M00/00/00/rBET9luKjWWAYY3AAAABPj7c3sE2681_big.sh");
        System.out.println(fileInfo);
    }
}
