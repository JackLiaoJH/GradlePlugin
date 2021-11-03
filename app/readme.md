### 生产签名文件
```
    keytool -genkey -v -keystore demo.jks -alias jackson  -storepass 123456 -keypass a123456 -keyalg RSA -validity 14000

    备注:
    -keystore：设置生成的文件名称，包含后缀；
    -alias：设置别名
    -storepass：设置文件的密码
    -keypass：设置key的密码
    -keyalg：设置使用的加密算法，一般写RSA
    -validity：设置有效期，尽可能长啦
```

### jks迁移到行业标准格式PKCS12
```
keytool -importkeystore -srckeystore demo.jks -destkeystore demo.jks -deststoretype pkcs12

// 指定别名
keytool -importkeystore -srckeystore demo.jks -destkeystore demo.jks -destkeypass a123456 -deststoretype pkcs12
```

https://www.jianshu.com/p/eaae3c1e0796
