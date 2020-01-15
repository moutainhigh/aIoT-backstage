package com.aiot.aiotbackstage.service.impl;
import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.common.util.FileUploadUtils;
import com.aiot.aiotbackstage.mapper.SysXingseMapper;
import com.aiot.aiotbackstage.model.entity.SysXingseEntity;
import com.aiot.aiotbackstage.model.vo.ImageRecognitionVo;
import com.aiot.aiotbackstage.service.IImageRecognitionService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description 形色实现
 * @Author xiaowenhui
 * @CreateTime 2020/1/7 17:29
 */
@Service
public class ImageRecognitionServiceImpl implements IImageRecognitionService {

    @Autowired
    private FileUploadUtils fileUploadUtils;

    @Autowired
    private SysXingseMapper xingseMapper;

    @Override
    public List<ImageRecognitionVo> fileUpload(MultipartFile multipartFile, String userId) {

        //待形色照片上传到OBS
        JSONObject jsonObject = fileUploadUtils.obsFileUpload(multipartFile);
        String name = (String)jsonObject.get("name");
        String url = (String)jsonObject.get("url");

        //查看该照片在DB是否存在
        SysXingseEntity xingseEntity = xingseMapper.selectOne(Wrappers.<SysXingseEntity>lambdaQuery()
                .eq(SysXingseEntity::getUserId, userId)
                .eq(SysXingseEntity::getParentId, 0L)
                .eq(SysXingseEntity::getName, name));

        List<SysXingseEntity> sysXingseEntities = null;
        //如果存在直接返回
        if(!ObjectUtils.isEmpty(xingseEntity)){
            Long xingseId = xingseEntity.getXingseId();
            sysXingseEntities = xingseMapper.selectList(Wrappers.<SysXingseEntity>lambdaQuery()
                    .eq(SysXingseEntity::getParentId, xingseId));
        }else{
            //如果不存在，则调用阿里云形色接口获取相似照片并存库
            sysXingseEntities = addImageRecognition(fileUploadUtils.imageRecognition(url), name, url);
        }
        //构建返回出参
        List<ImageRecognitionVo> imageRecognitionVos=new ArrayList<>();
        sysXingseEntities.stream().forEach(sysXingseEntity -> {
            ImageRecognitionVo recognitionVo=new ImageRecognitionVo();
            recognitionVo.setReferenceUrl(sysXingseEntity.getReferenceUrl());
            recognitionVo.setName(sysXingseEntity.getName());
            recognitionVo.setDesc(sysXingseEntity.getDesc());
            recognitionVo.setProbability(sysXingseEntity.getProbability());
            recognitionVo.setDetailUrl(sysXingseEntity.getDetailUrl());
            imageRecognitionVos.add(recognitionVo);
        });
        return imageRecognitionVos;
    }

    /**
     * 添加形色照片入DB
     * @param identifyResults
     * @param name
     * @param url
     * @return
     */
    private List<SysXingseEntity>  addImageRecognition(List<Map<String,Object>> identifyResults,String name,String url){
        //新增待形色的照片
        SysXingseEntity xingseEntity=new SysXingseEntity();
        xingseEntity.setUserId(0L);
        xingseEntity.setParentId(0L);
        xingseEntity.setReferenceUrl(url);
        xingseEntity.setName(name);
        xingseEntity.setDesc(name);
        xingseEntity.setProbability(new BigDecimal("0"));
        xingseEntity.setDetailUrl(url);
        xingseEntity.setCreateTime(new Date());
        xingseEntity.setUpdateTime(new Date());
        int count = xingseMapper.insert(xingseEntity);
        if (count < 1) {
            throw new MyException(ResultStatusCode.DB_ERR);
        }
        //新增相似的照片
        List<SysXingseEntity> sysXingseEntities=new ArrayList<>();
        identifyResults.stream().forEach(stringObjectMap -> {
            SysXingseEntity xingseEntityNew=new SysXingseEntity();
            xingseEntityNew.setUserId(0L);
            xingseEntityNew.setParentId(xingseEntity.getXingseId());
            xingseEntityNew.setReferenceUrl((String)stringObjectMap.get("reference_url"));
            xingseEntityNew.setName((String)stringObjectMap.get("name"));
            xingseEntityNew.setDesc((String)stringObjectMap.get("desc"));
            xingseEntityNew.setProbability((BigDecimal)stringObjectMap.get("probability"));
            xingseEntityNew.setDetailUrl((String)stringObjectMap.get("detail_url"));
            xingseEntityNew.setCreateTime(new Date());
            xingseEntityNew.setUpdateTime(new Date());
            int countNew = xingseMapper.insert(xingseEntityNew);
            if (countNew < 1) {
                throw new MyException(ResultStatusCode.DB_ERR);
            }
            sysXingseEntities.add(xingseEntityNew);
        });
        return sysXingseEntities;
    }


}
