package com.redxun.system.util.kettle;

import com.redxun.common.tool.BeanUtil;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.RepositoryDirectory;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryElementMetaInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 资源库操作工具类。
 */
public class RepositoryUtil {


    /**
     * 遍历资源库获取资源的JOB和转换。
      * @param kettleDatabaseRepository
     * @param path
     * @param allRepositoryTreeList
     * @return
     * @throws KettleException
     */
    public static List<RepositoryTree> getAllDirectoryTreeList(KettleDatabaseRepository kettleDatabaseRepository, String path, List<RepositoryTree> allRepositoryTreeList) throws KettleException {
        List<RepositoryTree> repositoryTreeList = getJobAndTrans(kettleDatabaseRepository, path);
        if (repositoryTreeList.size() != 0){
            for (RepositoryTree repositoryTree : repositoryTreeList) {
                if (!repositoryTree.isLasted()) {
                    getAllDirectoryTreeList(kettleDatabaseRepository, repositoryTree.getPath(), allRepositoryTreeList);
                    allRepositoryTreeList.add(repositoryTree);
                    continue;
                }
                allRepositoryTreeList.add(repositoryTree);
            }
        }

        return allRepositoryTreeList;
    }

    /**
     * 获取资源库的JOB和转换。
     * @param repository
     * @param path
     * @return
     * @throws KettleException
     */
    public static List<RepositoryTree> getJobAndTrans(KettleDatabaseRepository repository, String path) throws KettleException {
        RepositoryDirectoryInterface rDirectory = repository.loadRepositoryDirectoryTree().findDirectory(path);
        List<RepositoryTree> repositoryTreeList = getDirectory(repository, rDirectory);
        List<RepositoryElementMetaInterface> list = repository.getJobAndTransformationObjects(rDirectory.getObjectId(), false);
        if(BeanUtil.isEmpty(list)){
            return Collections.emptyList();
        }

        for (RepositoryElementMetaInterface repel : list) {
            if ("job".equals(repel.getObjectType().toString())) {
                RepositoryTree repositoryTree = new RepositoryTree();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("job").append(rDirectory.getObjectId().toString()).append("@").append(repel.getObjectId().toString());
                repositoryTree.setId(stringBuilder.toString());
                repositoryTree.setParent(rDirectory.getObjectId().toString());
                repositoryTree.setText(repel.getName());
                repositoryTree.setType("job");
                repositoryTree.setLasted(true);
                repositoryTreeList.add(repositoryTree);
                continue;
            }
            if ("transformation".equals(repel.getObjectType().toString())) {
                RepositoryTree repositoryTree = new RepositoryTree();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("transformation").append(rDirectory.getObjectId().toString()).append("@").append(repel.getObjectId().toString());
                repositoryTree.setId(stringBuilder.toString());
                repositoryTree.setParent(rDirectory.getObjectId().toString());
                repositoryTree.setText(repel.getName());
                repositoryTree.setType("transformation");
                repositoryTree.setLasted(true);
                repositoryTreeList.add(repositoryTree);
            }
        }

        return repositoryTreeList;
    }

    private static List<RepositoryTree> getDirectory(KettleDatabaseRepository repository, RepositoryDirectoryInterface rDirectory) throws KettleException {
        List<RepositoryTree> repositoryTreeList = new ArrayList<>();
        if(repository==null ||rDirectory==null || rDirectory.getNrSubdirectories()==0){
            return repositoryTreeList;
        }

        RepositoryDirectoryInterface tree = repository.loadRepositoryDirectoryTree().findDirectory(rDirectory.getObjectId());

        for (int i = 0; i < rDirectory.getNrSubdirectories(); i++) {
            RepositoryDirectory subTree = tree.getSubdirectory(i);
            RepositoryTree repositoryTree = new RepositoryTree();
            repositoryTree.setId(subTree.getObjectId().toString());
            repositoryTree.setParent(rDirectory.getObjectId().toString());
            repositoryTree.setText(subTree.getName());
            repositoryTree.setPath(subTree.getPath());
            List<RepositoryElementMetaInterface> RepositoryElementMetaInterfaceList =
                    repository.getJobAndTransformationObjects(subTree.getObjectId(), false);
            if (subTree.getNrSubdirectories() > 0 || RepositoryElementMetaInterfaceList.size() > 0) {
                repositoryTree.setLasted(false);
            } else {
                repositoryTree.setLasted(true);
            }
            repositoryTreeList.add(repositoryTree);
        }


        return repositoryTreeList;
    }
}
