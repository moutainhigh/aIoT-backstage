package com.aiot.aiotbackstage.common.sdk;

import com.aiot.aiotbackstage.common.config.DahuaConfig;
import com.dh.DpsdkCore.*;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Avernus
 */
@Slf4j
@Component
public class DahuaSDK implements ApplicationRunner {

    @Resource
    private DahuaConfig dahuaConfig;

    public static int m_nDLLHandle = -1;

    public static Map<String, String> channelMap = new HashMap<>();

    /**
     * 创建全局唯一SDK句柄
     * @return
     */
    public int init() {
        Return_Value_Info_t res = new Return_Value_Info_t();
        int nRet = IDpsdkCore.DPSDK_Create(dpsdk_sdk_type_e.DPSDK_CORE_SDK_SERVER, res);

        m_nDLLHandle = res.nReturnValue;
        if (m_nDLLHandle > 0) {
            //注册平台状态回调
            nRet = IDpsdkCore.DPSDK_SetDPSDKStatusCallback(m_nDLLHandle, fDPSDKStatusCallback);
            log.info("dahua sdk init success!");
        }

        boolean b = OnLogin();

        if (b) {
            int i = loadGroup();
            getDGroupStr(i);
        }
        return m_nDLLHandle;
    }

    /**
     * 登陆
     */
    public boolean OnLogin() {
        Login_Info_t loginInfo = new Login_Info_t();
        loginInfo.szIp = dahuaConfig.getIp().getBytes();
        loginInfo.nPort = dahuaConfig.getPort();
        loginInfo.szUsername = dahuaConfig.getUsername().getBytes();
        loginInfo.szPassword = dahuaConfig.getPassword().getBytes();
        loginInfo.nProtocol = dpsdk_protocol_version_e.DPSDK_PROTOCOL_VERSION_II;
        loginInfo.iType = 1;
        int nRet = IDpsdkCore.DPSDK_Login(m_nDLLHandle, loginInfo, 10000);
        if (nRet != dpsdk_retval_e.DPSDK_RET_SUCCESS) {
            log.error("登录失败，nRet = {}", nRet);
            return false;
        }
        return true;
    }

    public int loadGroup() {
        Return_Value_Info_t nGroupLen = new Return_Value_Info_t();
        int nRet = IDpsdkCore.DPSDK_LoadDGroupInfo(m_nDLLHandle, nGroupLen, 180000);
        if (nRet != dpsdk_retval_e.DPSDK_RET_SUCCESS) {
            log.error("加载所有组织树失败，nRet = {}", nRet);
        }
        return nGroupLen.nReturnValue;
    }

    public Document getDGroupStr(int nGroupLen) {
        byte[] szGroupBuf = new byte[nGroupLen];
        int nRet = IDpsdkCore.DPSDK_GetDGroupStr(m_nDLLHandle, szGroupBuf, nGroupLen, 10000);
        if (nRet != dpsdk_retval_e.DPSDK_RET_SUCCESS) {
            log.error("获取所有组织树串失败，nRet = {}", nRet);
        }

        /**
         * <Organization>
         *   <Department coding="001" name="根节点" modifytime="" sn="" memo="" deptype="1" depsort="0" chargebooth="0" OrgNum="">
         *     <Device id="1000004"/>
         *     <Channel id="1000004$1$0$0"/>
         *     <Channel id="1000004$3$0$0"/>
         *     <Channel id="1000004$3$0$1"/>
         *     <Channel id="1000004$3$0$2"/>
         *     <Channel id="1000004$3$0$3"/>
         *     <Channel id="1000004$3$0$4"/>
         *     <Channel id="1000004$3$0$5"/>
         *     <Channel id="1000004$3$0$6"/>
         *     <Channel id="1000004$4$0$0"/>
         *     <Channel id="1000004$4$0$1"/>
         *     <Department coding="001001" name="安州" modifytime="" sn="anzhou" memo="" deptype="1" depsort="1" chargebooth="0" OrgNum="">
         *       <Device id="1000005"/>
         *       <Channel id="1000005$1$0$0"/>
         *     </Department>
         *   </Department>
         *   <Devices>
         *     <Device id="1000005" type="1" name="anzhou-001" manufacturer="1" model="" ip="192.168.2.243" port="61001" user="admin" password="jieyun123" desc="" status="1" logintype="" registDeviceCode="00000001" proxyport="0" unitnum="0" deviceCN="" deviceSN="" deviceIp="192.168.2.108" devicePort="39248" devMaintainer="" devMaintainerPh="" deviceLocation="" deviceLocPliceStation="" baudRate="" comCode="" VideoType="" shopName="" address="" firstOwner="" firstPosition="" firstPhone="" firstTel="" serviceType="0" ownerGroup="" belong="" role="0" callNumber="" rights="1000000000000000000000011101000000101100011111111111111111">
         *       <UnitNodes index="0" channelnum="1" streamType="801" subType="0" type="1" zeroChnEncode="0">
         *         <Channel id="1000005$1$0$0" name="anzhou-001_1" desc="" status="0" channelType="1" channelSN="240001" rights="1000000000000000000000011101000000101100011111111111111111" cameraType="2" CtrlId="240001" latitude="" longitude="" viewDomain="" cameraFunctions="0" multicastIp="" multicastPort="0" NvrChnlIp="" channelRemoteType="" subMulticastIp="" subMulticastPort="0"/>
         *       </UnitNodes>
         *     </Device>
         *   </Devices>
         * </Organization>
         */
        Document document = null;
        try {
            SAXReader reader = new SAXReader();
            document = reader.read(new ByteArrayInputStream(szGroupBuf));
            for (Object node : document.selectNodes("//Channel")) {
                Element e = (Element) node;
                if (e.attributeValue("name") != null) {
                    channelMap.put(e.getParent().getParent().attributeValue("name"), e.attributeValue("id"));
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return document;
    }

    public String real(String channelId) {
        Get_ExternalRealStreamUrl_Info_t pExternalRealStreamUrlInfo = new Get_ExternalRealStreamUrl_Info_t();
        pExternalRealStreamUrlInfo.szCameraId = channelId.getBytes();
        pExternalRealStreamUrlInfo.nMediaType = 1;
        pExternalRealStreamUrlInfo.nStreamType = 1;
        pExternalRealStreamUrlInfo.nTrackId = 8011;
        pExternalRealStreamUrlInfo.nTransType = 1;
        pExternalRealStreamUrlInfo.bUsedVCS = 0;
        pExternalRealStreamUrlInfo.nVcsbps = 0;
        pExternalRealStreamUrlInfo.nVcsfps = 0;
        pExternalRealStreamUrlInfo.nVcsResolution = 0;
        pExternalRealStreamUrlInfo.nVcsVideocodec = 0;
        int nRet = IDpsdkCore.DPSDK_GetExternalRealStreamUrl(m_nDLLHandle, pExternalRealStreamUrlInfo, 10000);

        if (nRet != dpsdk_retval_e.DPSDK_RET_SUCCESS) {
            log.error("获取URL失败，nRet = {}", nRet);
        }
        return new String(pExternalRealStreamUrlInfo.szUrl).trim();
    }

    public void ptzDirectCtrl(String channelId, Integer direction) {
        Ptz_Direct_Info_t directInfo = new Ptz_Direct_Info_t();
        directInfo.szCameraId = channelId.getBytes();
        directInfo.nDirect = direction;      //dpsdk_ptz_direct_e
        directInfo.nStep = 3;
        directInfo.bStop = false;
        int nRet = IDpsdkCore.DPSDK_PtzDirection(m_nDLLHandle, directInfo, dpsdk_constant_value.DPSDK_CORE_DEFAULT_TIMEOUT);
        directInfo.bStop = true;
        nRet = IDpsdkCore.DPSDK_PtzDirection(m_nDLLHandle, directInfo, dpsdk_constant_value.DPSDK_CORE_DEFAULT_TIMEOUT);
        if (nRet != dpsdk_retval_e.DPSDK_RET_SUCCESS) {
            log.error("云台方向控制失败，nRet = {}", nRet);
        }
    }


    public fDPSDKStatusCallback fDPSDKStatusCallback = new fDPSDKStatusCallback() {
        @Override
        public void invoke(int i, int nStatus) {
            //上线通知
            if (nStatus == 1) {

            }
            //下线通知
            if (nStatus == 2) {

            }
        }
    };

    @Override
    public void run(ApplicationArguments args) throws Exception {
        init();
    }
}
