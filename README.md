## 应用启动前导入.dll/.so ##
========================
1. linux 
        - 将/so文件夹中的文件拷贝到系统文件夹/usr/lib下
        - 在/etc/ld.so.conf目录下新增配置文件 XXX.conf,文件内容为.so所在目录 (/usr/lib)
        - 用/sbin/ldconfig命令更新配置文件缓存
2. windows
        - 将/dll文件夹中的文件拷贝到系统文件夹C:\Windows\System32下        