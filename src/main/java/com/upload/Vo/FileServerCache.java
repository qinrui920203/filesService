package com.upload.Vo;

import com.upload.utils.FileUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* **
 * @Author RUI
 * @Description 缓存对象，想法是文件信息都用缓存对象保存，不要每次list或者分页的时候再去
 *              查文件走IO。此对象为单例，项目唯一。目前缓存内容以list形式保存，后期如果
 *              加入用户概念，则考虑使用 Map<userId, fileVoList>的形式保存，然后全对象以
 *              定时做快照形式存入文件，尽量不考虑使用数据库，保证项目能不依赖其他任何东西
 *              启动。考虑过使用缓存带来的内存消耗，每个文件也就几十字节，服务器就算放1W文
 *              件都用不到1M
 */
@Data
public class FileServerCache {

    @Value("${fileserver.basepath}")
    private String basePath;

    /* ** 文件信息缓存list */
    private List<FileVo> fileInfoList;
    /* ** 分片上传缓存信息 */
    private Map<String, FileBlockVo> blockFileVoMap;

    /* ** 增加一个文件信息到缓存 */
    public FileVo cacheFileInfo(File file){
        FileVo fileInfo = FileUtils.getInfoFromFile(file);

        int addIndex = 0;
        boolean pushed = false;
        for(FileVo fileVo : fileInfoList){
            if(fileVo.getType().equals(fileInfo.getType())){
                fileInfoList.add(addIndex, fileInfo);
                pushed = true;
                break;
            }
            ++addIndex;
        }
        if(!pushed){fileInfoList.add(fileInfo);}

        return fileInfo;
    }

    /* ** 从缓存中删除一个文件 */
    public void removeFromCache(String fileName){
        FileVo fileVo = fileInfoList.stream()
                .filter(node -> node.getName().equals(fileName))
                .findAny()
                .orElse(null);  // 缓存中无此文件
        fileInfoList.remove(fileVo);
    }

    /* **初始化缓存对象 */
    public void initChace(){
        blockFileVoMap = new HashMap<String, FileBlockVo>();
    }

    /* **清空缓存 */
    public void clearCache(){
        fileInfoList.clear();
    }

    /* ** 增加一个分片 */
    public void addFileBlockToCache(String fileName, FileVo blockinfo){
        FileBlockVo fileBlockVo = blockFileVoMap.get(fileName);
        if(null == fileBlockVo){
            fileBlockVo = new FileBlockVo();
            blockFileVoMap.put(fileName, fileBlockVo);
        }
        fileBlockVo.getBlocks().add(blockinfo);
    }

    /* ** 获取文件分片列表 */
    public List<FileVo> getBlockInfoByName(String fileName){
        return blockFileVoMap.get(fileName).getBlocks();
    }

    /* ** 修改分片记录状态 */
    public void changeFileBlockStatu(String fileName, String statu){
        FileBlockVo fileBlockVo = blockFileVoMap.get(fileName);
        fileBlockVo.setStatu(statu);
    }

    /* ** 获取分片记录状态 */
    public String getFileBlockStatu(String fileName, String statu){
        FileBlockVo fileBlockVo = blockFileVoMap.get(fileName);
        return fileBlockVo.getStatu();
    }

    /* ** 删除分片 */
    public void removeFileBlockFromCache(String fileName){
        blockFileVoMap.remove(fileName);
    }
}
